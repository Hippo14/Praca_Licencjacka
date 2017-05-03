package pl.code_zone.praca_licencjacka.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.code_zone.praca_licencjacka.R;
import pl.code_zone.praca_licencjacka.model.User;
import pl.code_zone.praca_licencjacka.utils.Config;
import pl.code_zone.praca_licencjacka.utils.GsonUtils;
import pl.code_zone.praca_licencjacka.utils.ImageConverter;
import pl.code_zone.praca_licencjacka.utils.SessionManager;
import pl.code_zone.praca_licencjacka.webservice.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

/**
 * Created by MSI on 2016-10-30.
 */

public class UserFragment extends Fragment {

    private static final String TAG = UserFragment.class.getSimpleName();

    private static final int IMAGE_REQUEST = 1;
    private static final int PIC_CROP = 2;
    private ImageView imageView;

    private Button addBox;

    private TextView username;
    private TextView email;
    private TextView dateCreation;

    private LruCache<String, Bitmap> memoryCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int memory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = memory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    public void loadBitmap(String imageKey, ImageView imageView) {
        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            getUserLogo();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        username = (TextView) view.findViewById(R.id.username);
        email = (TextView) view.findViewById(R.id.email);
        dateCreation = (TextView) view.findViewById(R.id.dateCreation);

        imageView = (ImageView) view.findViewById(R.id.imageView);

        addBox = (Button) view.findViewById(R.id.add_box);


        addBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        });

        getUser();

        return view;
    }

    public void getUser() {
        getUserTask();
        loadBitmap("logo", imageView);
    }

    public void getUserTask() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.URL_WEBSERVICE)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.create()))
                .build();

        UserService service = retrofit.create(UserService.class);

        Map<String, String> body = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("token", SessionManager.getToken());
        params.put("body", body);

        Call<User> userCall = service.getUserByToken(params);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    User user = response.body();
                    username.setText(user.getName());
                    email.setText(user.getEmail());

                    Date date = new Date(user.getDateCreation());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    dateCreation.setText(dateFormat.format(date));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    public void getUserLogo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.URL_WEBSERVICE)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.create()))
                .build();

        UserService service = retrofit.create(UserService.class);

        Map<String, String> body = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("token", SessionManager.getToken());
        params.put("body", body);

        Call<Map<String, String>> userCall = service.getUserLogo(params);
        userCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.body() != null) {
                    String imageBase64 = response.body().get("image");

                    byte[] decodedString = Base64.decode(imageBase64, Base64.NO_WRAP);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imageView.setImageBitmap(ImageConverter.getRoundedCornerBitmap(decodedByte, 100));
                    addBitmapToMemoryCache("logo", decodedByte);
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
//                    try {
                        final Uri imageUri = data.getData();
                        perfCrop(imageUri);
//                        final InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
//                        imageView.setImageBitmap(selectedImage);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
                }
            break;
            case PIC_CROP:
                try {
                    Bundle extras = data.getExtras();
                    Bitmap picture = extras.getParcelable("data");
                    picture = ImageConverter.getRoundedCornerBitmap(picture, 100);
                    // TODO Send to server
                    setUserLogo(picture);

                    imageView.setImageBitmap(picture);
                } catch(Exception e) {
                }
            break;
        }
    }

    private void perfCrop(Uri imageUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(imageUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, PIC_CROP);

    }

    public void setUserLogo(Bitmap userLogo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.URL_WEBSERVICE)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.create()))
                .build();

        UserService service = retrofit.create(UserService.class);

        // Bitmap to Base64
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        userLogo.compress(Bitmap.CompressFormat.JPEG, 75, out);
        byte[] byteArray = out.toByteArray();

        Map<String, String> body = new HashMap<>();
        body.put("image", Base64.encodeToString(byteArray, Base64.NO_WRAP));

        Map<String, Object> params = new HashMap<>();
        params.put("token", SessionManager.getToken());
        params.put("body", body);

        Call<Boolean> userCall = service.setUserLogo(params);
        userCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
}