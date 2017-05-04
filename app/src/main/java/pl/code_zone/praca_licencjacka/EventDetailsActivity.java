package pl.code_zone.praca_licencjacka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.code_zone.praca_licencjacka.adapter.EventDetailsAdapter;
import pl.code_zone.praca_licencjacka.config.ApiClient;
import pl.code_zone.praca_licencjacka.model.Event;
import pl.code_zone.praca_licencjacka.model.UsersEvents;
import pl.code_zone.praca_licencjacka.row.EventDetailsRow;
import pl.code_zone.praca_licencjacka.utils.LocationUtils;
import pl.code_zone.praca_licencjacka.utils.SessionManager;
import pl.code_zone.praca_licencjacka.webservice.EventService;
import pl.code_zone.praca_licencjacka.webservice.credentials.EventCredentials;
import pl.code_zone.praca_licencjacka.webservice.credentials.TokenEventCred;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = EventDetailsActivity.class.getSimpleName();

    TextView markerCityName;
    TextView markerAddress;

    TextView markerTitle;
    TextView markerUsername;
    TextView markerDescription;
    TextView markerDateCreated;
    TextView markerDateEnd;
    TextView markerCategory;

    TextView numberOfUsers;

    Button mButton;

    ListView userList;
    EventDetailsAdapter adapter;

    String context;
    Double latitude;
    Double longitude;
    ProgressDialog progressDialog;
    GoogleMap mMap;
    FrameLayout mMapHolder;
    ImageView snapshotHolder;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markerCityName = (TextView) findViewById(R.id.cityName);
        markerAddress = (TextView) findViewById(R.id.address);
        markerTitle = (TextView) findViewById(R.id.title);
        markerUsername = (TextView) findViewById(R.id.username);
        markerDescription = (TextView) findViewById(R.id.description);
        markerDateCreated = (TextView) findViewById(R.id.dateCreated);
        markerDateEnd = (TextView) findViewById(R.id.endDate);
        markerCategory = (TextView) findViewById(R.id.category);
//        snapshotHolder = (ImageView) findViewById(R.id.snapshot);


//        mMapHolder = (FrameLayout) findViewById(R.id.frame_layout);

        numberOfUsers = (TextView) findViewById(R.id.number_of_users);

        userList = (ListView) findViewById(R.id.userList);

        mButton = (Button) findViewById(R.id.event_sign_in);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mButton.getText().toString();
                String deleted = getResources().getString(R.string.event_sign_in);

                if (deleted.equals(text)) {
                    addUserToEvent();
                } else {
                    deleteUserFromEvent();
                }
            }
        });



        context = (String) getIntent().getExtras().get("context");
        latitude = Double.parseDouble((String) getIntent().getExtras().get("latitude"));
        longitude = Double.parseDouble((String) getIntent().getExtras().get("longitude"));

        progressDialog = new ProgressDialog(this);
        populate();
    }

    private void deleteUserFromEvent() {
        Retrofit retrofit = ApiClient.getInstance().getClient();
        EventService service = retrofit.create(EventService.class);

        Map<String, String> body = new HashMap<>();
        body.put("latitude", Double.toString(latitude));
        body.put("longitude", Double.toString(longitude));

        Map<String, Object> params = new HashMap<>();
        params.put("token", SessionManager.getToken());
        params.put("body", body);

        Call<Map<String, String>> userCall = service.deleteUserFromEvent(params);
        userCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    //Snackbar.make(findViewById(R.id.activity_event_details), "Deleted!", Snackbar.LENGTH_LONG).show();
                    mButton.setText(getResources().getString(R.string.event_sign_in));
                    reloadActivity();
                }
                else {
                    try {
                        Snackbar.make(findViewById(R.id.activity_event_details), response.errorBody().string(), Snackbar.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Snackbar.make(findViewById(R.id.activity_event_details), t.toString(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void addUserToEvent() {
        Retrofit retrofit = ApiClient.getInstance().getClient();
        EventService service = retrofit.create(EventService.class);

        Map<String, String> body = new HashMap<>();
        body.put("latitude", Double.toString(latitude));
        body.put("longitude", Double.toString(longitude));

        Map<String, Object> params = new HashMap<>();
        params.put("token", SessionManager.getToken());
        params.put("body", body);

        Call<Map<String, String>> userCall = service.addUserToEvent(params);
        userCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    //Snackbar.make(findViewById(R.id.activity_event_details), "Added to favourite!", Snackbar.LENGTH_LONG).show();
                    mButton.setText(getResources().getString(R.string.event_sign_out));
                    reloadActivity();
                }
                else {
                    try {
                        Snackbar.make(findViewById(R.id.activity_event_details), response.errorBody().string(), Snackbar.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Snackbar.make(findViewById(R.id.activity_event_details), t.toString(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void getDetails(final double latitude, final double longitude) {
        Retrofit retrofit = ApiClient.getInstance().getClient();

        EventService service = retrofit.create(EventService.class);
        TokenEventCred cred = new TokenEventCred();
        EventCredentials eventCred = new EventCredentials();
        eventCred.setLatitude(latitude);
        eventCred.setLongitude(longitude);
        cred.setToken(SessionManager.getToken());
        cred.setBody(eventCred);

        Call<Event> userCall = service.getEventDetails(cred);
        userCall.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful()) {
                    markerTitle.setText(response.body().getName());
                    markerUsername.setText(response.body().getUser().getName());
                    markerDescription.setText(response.body().getDescription());

                    Date dateCreated = new Date(response.body().getDateCreation());
                    Date dateEnd = new Date(response.body().getDateEnding());
                    SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    markerDateCreated.setText(dt1.format(dateCreated));
                    markerDateEnd.setText(dt1.format(dateEnd));
                    markerCategory.setText(response.body().getCategory().getName());


                    Address address = LocationUtils.getCityName(new LatLng(latitude, longitude), getApplicationContext());
                    markerCityName.setText(address.getLocality());
                    markerAddress.setText(address.getAddressLine(0));


                    Byte isActive = response.body().getActive();

                    if (isActive.intValue() == 1) {
                        mButton.setVisibility(View.VISIBLE);
                    } else {
                        mButton.setVisibility(View.GONE);
                    }
                }
                else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.e(TAG, t.toString());
                progressDialog.dismiss();
            }
        });
    }

    public void getUserListFromEvent() {
        Retrofit retrofit = ApiClient.getInstance().getClient();

        EventService service = retrofit.create(EventService.class);

        Map<String, String> body = new HashMap<>();
        body.put("latitude", Double.toString(latitude));
        body.put("longitude", Double.toString(longitude));

        Map<String, Object> params = new HashMap<>();
        params.put("token", SessionManager.getToken());
        params.put("body", body);

        Call<List<UsersEvents>> eventCall = service.getUserListEvent(params);
        eventCall.enqueue(new Callback<List<UsersEvents>>() {
            @Override
            public void onResponse(Call<List<UsersEvents>> call, Response<List<UsersEvents>> response) {
                if (response.isSuccessful()) {
                    List<EventDetailsRow> eventDetailsRows = new ArrayList<>();
                    for (UsersEvents elem : response.body()) {
                        eventDetailsRows.add(new EventDetailsRow(elem));
                    }
                    adapter = new EventDetailsAdapter(getApplicationContext(), eventDetailsRows);
                    userList.setAdapter(adapter);
                    progressDialog.dismiss();
                    numberOfUsers.setText(Integer.toString(eventDetailsRows.size()));
                }
            }

            @Override
            public void onFailure(Call<List<UsersEvents>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void populate() {
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Get event details
        getDetails(latitude, longitude);

        // Check if user signed
        getUserStatusEvent();

        // Get user list from event
        getUserListFromEvent();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        final LatLng location = new LatLng(
                Double.parseDouble((String) getIntent().getExtras().get("latitude")),
                Double.parseDouble((String) getIntent().getExtras().get("longitude"))
        );

        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(location, 15.0f);
        mMap.animateCamera(center);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.clear();
                MarkerOptions options = new MarkerOptions();
                options.position(location);
                mMap.addMarker(options);
            }
        });
    }

    public void getUserStatusEvent() {
        Retrofit retrofit = ApiClient.getInstance().getClient();

        EventService service = retrofit.create(EventService.class);

        Map<String, String> body = new HashMap<>();
        body.put("latitude", Double.toString(latitude));
        body.put("longitude", Double.toString(longitude));

        Map<String, Object> params = new HashMap<>();
        params.put("token", SessionManager.getToken());
        params.put("body", body);

        Call<Boolean> eventCall = service.getUserStatusEvent(params);
        eventCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        mButton.setText(getResources().getString(R.string.event_sign_out));
                    }
                    else {
                        mButton.setText(getResources().getString(R.string.event_sign_in));
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void reloadActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!"SearchEventActivity".equals(context)) {
            Intent intent = new Intent(EventDetailsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            finish();
            startActivity(intent);
        }
    }
}
