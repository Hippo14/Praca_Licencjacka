package pl.code_zone.praca_licencjacka;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import pl.code_zone.praca_licencjacka.model.Category;
import pl.code_zone.praca_licencjacka.model.Event;
import pl.code_zone.praca_licencjacka.model.User;
import pl.code_zone.praca_licencjacka.utils.Config;
import pl.code_zone.praca_licencjacka.utils.GsonUtils;
import pl.code_zone.praca_licencjacka.utils.SessionManager;
import pl.code_zone.praca_licencjacka.webservice.EventService;
import pl.code_zone.praca_licencjacka.webservice.credentials.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private Marker marker;

    private EditText mLocation;
    private EditText mDescription;

    private EditText mFromDate;
    private EditText mToDate;

    private Button mAddEvent;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private Date beforeDate;
    private Date afterDate;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        marker = null;
        mProgressDialog = new ProgressDialog(AddEventActivity.this);
        mAddEvent = (Button) findViewById(R.id.event_add);
        mLocation = (EditText) findViewById(R.id.location);
        mDescription = (EditText) findViewById(R.id.description);
        mFromDate = (EditText) findViewById(R.id.fromDate);
        mToDate = (EditText) findViewById(R.id.toDate);

        DialogTest fromDate = new DialogTest(mFromDate, "From date");
        DialogTest toDate = new DialogTest(mToDate, "To date");

        mFromDate.setOnFocusChangeListener(fromDate);
        mFromDate.setOnClickListener(fromDate);
        mToDate.setOnFocusChangeListener(toDate);
        mToDate.setOnClickListener(toDate);



        mLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onLocationChange(v, hasFocus);
            }
        });

        mAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddEventClick();
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null)
                    marker.remove();
                marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
                marker.setTitle("");
                marker.showInfoWindow();
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            }
        });
    }

    private void onAddEventClick() {
        if (beforeDate != null && afterDate != null && beforeDate.before(afterDate) &&
                marker != null && marker.getTitle() != null && mDescription.getText() != null && beforeDate != null) {
                addEvent(marker, mDescription);
        }
    }

    private void addEvent(Marker marker, EditText mDescription) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.URL_WEBSERVICE)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.create()))
                .build();

        EventService service = retrofit.create(EventService.class);
        Event event = createEvent(marker, mDescription, beforeDate, afterDate);

        String token = SessionManager.getToken();

        Token credentials = new Token();
        credentials.setToken(token);
        credentials.setBody(event);

        Call<String> eventCall = service.addNewEvent(credentials);
        eventCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String bodyString = new String(response.body());

                    SessionManager.setMessage(bodyString);
                    onBackPressed();
                } else {
                    try {
                        Snackbar.make(findViewById(R.id.activity_add_event), response.errorBody().string(), Snackbar.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // TODO Snackbar error
                Snackbar.make(findViewById(R.id.activity_add_event), t.toString(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private Event createEvent(Marker marker, EditText mDescription, Date dateCreation, Date dateEnding) {
        Event event = new Event();
        event.setName(marker.getTitle());
        event.setDescription(mDescription.getText().toString());
        event.setLongitude(marker.getPosition().longitude);
        event.setLatitude(marker.getPosition().latitude);

        User user = new User();
        event.setUser(user);

        Category category = new Category();
        category.setId(1);
        category.setName("Main category");
        event.setCategory(category);

        event.setDeleted((byte) 0);
        event.setActive((byte) 1);
        event.setDateCreation(dateCreation.getTime());
        event.setDateEnding(dateEnding.getTime());
        return event;
    }

    private void onLocationChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            String location = mLocation.getText().toString();
            if (location != null && !location.equals("")) {
                new GeoCoderTask().execute(location);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AddEvent Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class GeoCoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... params) {
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = new ArrayList<>();

            try {
                addresses = geocoder.getFromLocationName(params[0], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            if (addresses == null || addresses.size() == 0) {
                Snackbar.make(findViewById(R.id.activity_add_event), "No location found", Snackbar.LENGTH_LONG).show();
            } else {
                LatLng location = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                if (marker != null)
                    marker.remove();
                marker = mGoogleMap.addMarker(new MarkerOptions().position(location).title(addresses.get(0).getAddressLine(0)));
                marker.showInfoWindow();
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()), 12.0f));
            }
        }
    }

    private class DialogTest implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

        private Calendar calendar;
        private EditText label;
        private String title;

        public DialogTest(EditText label, String title) {
            this.calendar = Calendar.getInstance();
            this.label = label;
            this.title = title;
        }

        @Override
        public void onClick(View v) {
            onFocusChange(v, true);
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                DatePickerDialog dpd = new DatePickerDialog(AddEventActivity.this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                dpd.setTitle(title);
            }
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

        private void updateLabel() {
            String format = "MM/dd/yy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.GERMAN);
            label.setText(simpleDateFormat.format(calendar.getTime()));
            if ("From date".equals(title)) {
                beforeDate = calendar.getTime();
            } else {
                afterDate = calendar.getTime();
            }
        }

    }

}
