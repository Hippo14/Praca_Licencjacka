package pl.code_zone.praca_licencjacka;

import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import pl.code_zone.praca_licencjacka.model.Event;
import pl.code_zone.praca_licencjacka.utils.ActivityUtils;
import pl.code_zone.praca_licencjacka.utils.GsonUtils;
import pl.code_zone.praca_licencjacka.utils.LocationUtils;
import pl.code_zone.praca_licencjacka.utils.SessionManager;
import pl.code_zone.praca_licencjacka.webservice.EventService;
import pl.code_zone.praca_licencjacka.webservice.credentials.EventCredentials;
import pl.code_zone.praca_licencjacka.webservice.credentials.TokenEventCred;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SearchEventActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;
    private boolean isRunning = false;
    private ScheduledThreadPoolExecutor scheduler = null;
    private LatLng location;
    private String cityName;

    private List<Event> eventList;
    private HashMap<Marker, pl.code_zone.praca_licencjacka.model.Marker> markerList;
    private Marker marker;

    private static final String TAG = SearchEventActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_event);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventList = new ArrayList<>();
        markerList = new LinkedHashMap<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //FIXME fix synchronization with marker details
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);


        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        location = SessionManager.getLocation();

        cityName = LocationUtils.getCityName(location, getApplicationContext());

        if (location != null) {
            CameraUpdate center = CameraUpdateFactory.newLatLngZoom(location, 12.0f);
            mMap.animateCamera(center);
        }

        mMap.setOnCameraMoveListener(this);
        mMap.setInfoWindowAdapter(this);
    }

    private void scheduleAlarm() {
        Log.d("EventService", "Start Task");
        isRunning = true;

        scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                getEvents(location);
            }
        }, 1, 15, TimeUnit.SECONDS);

    }

    private void addMarkers() {
        Log.d("EventService", "Adding markers to map");

        //mMap.clear();

        for (Event event : eventList) {
            MarkerOptions options = new MarkerOptions();
            options.title(event.getName());
            options.position(new LatLng(event.getLatitude(), event.getLongitude()));

            Marker marker = mMap.addMarker(options);
            pl.code_zone.praca_licencjacka.model.Marker userMarker = new pl.code_zone.praca_licencjacka.model.Marker();
            userMarker.setTitle(event.getName());
            userMarker.setUsername(event.getUser().getName());
            userMarker.setDescription(event.getDescription());
            markerList.put(marker, userMarker);
        }
    }

    private void getEvents(LatLng location) {
        String cityName = LocationUtils.getCityName(new LatLng(location.latitude, location.longitude), getApplicationContext());
        double latitude = location.latitude;
        double longitude = location.longitude;
        getEventsTask(cityName, latitude, longitude);
    }

    private void getEventsTask(String cityName, double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://217.61.2.26:8080/resteasy/rest/")
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.create()))
                .build();

        EventService service = retrofit.create(EventService.class);
        TokenEventCred cred = new TokenEventCred();
        EventCredentials eventCredentials = new EventCredentials();
        eventCredentials.setCityName(cityName);
        eventCredentials.setLatitude(latitude);
        eventCredentials.setLongitude(longitude);
        cred.setToken(SessionManager.getToken());
        cred.setBody(eventCredentials);

        Call<List<Event>> userCall = service.getEvents(cred);
        userCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    Log.d("EventService", "Successfully download eventList...");
                    eventList = response.body();
                    addMarkers();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.d("EventService", "Error when download eventList...");
                Log.e(TAG, t.toString());
            }
        });
    }

    public void cancelAlarm() {
        isRunning = false;

        if (scheduler != null) {
            scheduler.shutdown();
            try {
                scheduler.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("Event Service", "Shutdown Task");
        }
    }

    @Override
    public void onCameraMove() {
        CameraPosition cameraPosition = mMap.getCameraPosition();
        location = cameraPosition.target;
    }

    @Override
    public void onBackPressed() {
        Log.d("EventService", "onBackPressed()");

        cancelAlarm();

        ActivityUtils.change(this, MainActivity.class);
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("EventService", "onStop()");
        cancelAlarm();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("EventService", "onResume()");
        if (!isRunning)
            scheduleAlarm();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("EventService", "onStart()");
        if (!isRunning)
            scheduleAlarm();
    }

    private pl.code_zone.praca_licencjacka.model.Marker getMarkerDetails(Marker marker) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://217.61.2.26:8080/resteasy/rest/")
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.create()))
                .build();

        EventService service = retrofit.create(EventService.class);
        EventCredentials eventCredentials = new EventCredentials();
        eventCredentials.setCityName(LocationUtils.getCityName(marker.getPosition(), getApplicationContext()));
        eventCredentials.setLatitude(marker.getPosition().latitude);
        eventCredentials.setLongitude(marker.getPosition().longitude);

        TokenEventCred tokenEventCred = new TokenEventCred();

        tokenEventCred.setToken(SessionManager.getToken());
        tokenEventCred.setBody(eventCredentials);

        Call<pl.code_zone.praca_licencjacka.model.Marker> userCall = service.getMarkerDetails(tokenEventCred);
        pl.code_zone.praca_licencjacka.model.Marker userMarker = null;
        try {
            userMarker = userCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userMarker;
//        Call<pl.code_zone.praca_licencjacka.model.Marker> userCall = service.getMarkerDetails(tokenEventCred);
//        userCall.enqueue(new Callback<pl.code_zone.praca_licencjacka.model.Marker>() {
//            @Override
//            public void onResponse(Call<pl.code_zone.praca_licencjacka.model.Marker> call, Response<pl.code_zone.praca_licencjacka.model.Marker> response) {
//                if (response.isSuccessful()) {
//                    retrofitCallback.onSuccess(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<pl.code_zone.praca_licencjacka.model.Marker> call, Throwable t) {
//                Log.e(TAG, t.toString());
//            }
//        });
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        SearchEventActivity.this.marker = marker;

        // Getting view from the layout file
        final View v = getLayoutInflater().inflate(R.layout.marker_layout, null);

        pl.code_zone.praca_licencjacka.model.Marker userMarker = markerList.get(marker);
//        pl.code_zone.praca_licencjacka.model.Marker userMarker = getMarkerDetails(marker);

        TextView markerTitle = (TextView) v.findViewById(R.id.title);
        TextView markerUsername = (TextView) v.findViewById(R.id.username);
        TextView markerDescription = (TextView) v.findViewById(R.id.description);

        markerTitle.setText(userMarker.getTitle());
        markerUsername.setText(userMarker.getUsername());
        markerDescription.setText(userMarker.getDescription());

        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
//        if (SearchEventActivity.this.marker != null && SearchEventActivity.this.marker.isInfoWindowShown() && detailsStatus == DETAILS_DOWNLOADED) {
//            detailsStatus = DETAILS_REFRESHED;
//            SearchEventActivity.this.marker.showInfoWindow();
//        }

        return null;
    }

    public interface RetrofitCallback {
        void onSuccess(pl.code_zone.praca_licencjacka.model.Marker result);
    }
}
