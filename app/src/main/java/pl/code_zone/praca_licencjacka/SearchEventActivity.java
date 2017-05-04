package pl.code_zone.praca_licencjacka;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import pl.code_zone.praca_licencjacka.config.ApiClient;
import pl.code_zone.praca_licencjacka.model.Event;
import pl.code_zone.praca_licencjacka.utils.ActivityUtils;
import pl.code_zone.praca_licencjacka.utils.LocationUtils;
import pl.code_zone.praca_licencjacka.utils.SessionManager;
import pl.code_zone.praca_licencjacka.webservice.EventService;
import pl.code_zone.praca_licencjacka.webservice.credentials.EventCredentials;
import pl.code_zone.praca_licencjacka.webservice.credentials.TokenEventCred;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SearchEventActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private boolean isRunning = false;
    private boolean onMapReady = false;
    private ScheduledThreadPoolExecutor scheduler = null;
    private LatLng location = null;
    private String cityName;

    private HashMap<Marker, pl.code_zone.praca_licencjacka.model.Marker> markerList;

    private static final String TAG = SearchEventActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("EventService", "onCreate()");
        setContentView(R.layout.activity_search_event);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markerList = new LinkedHashMap<>();

        String latitude = null;
        String longitude = null;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                latitude = extras.getString("latitude");
                longitude = extras.getString("longitude");
            }
        } else {
            latitude = (String) savedInstanceState.getSerializable("latitude");
            longitude = (String) savedInstanceState.getSerializable("longitude");
        }

        location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        Log.d("EventService", "onCreate() Location: " + location.latitude + ", " + location.longitude);
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

        cityName = LocationUtils.getCityName(location, getApplicationContext()).getLocality();

        if (location != null) {
            CameraUpdate center = CameraUpdateFactory.newLatLngZoom(location, 12.0f);
            mMap.animateCamera(center);
        }

        mMap.setOnCameraMoveListener(this);
        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);

        if (!isRunning) {
            onMapReady = true;
            scheduleAlarm();
        }
    }

    private void scheduleAlarm() {
        Log.d("EventService", "Start Task");
        isRunning = true;

        final LatLng params = location;

        scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(3);
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Log.d("EventService", "run() Location: " + params.longitude + ", " + params.latitude);
                getEvents(params);
            }
        }, 1, 15, TimeUnit.SECONDS);

    }

    private void addMarkers(List<Event> body) {
        Log.d("EventService", "Adding markers to map");

        mMap.clear();

        for (Event event : body) {
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
        String cityName = LocationUtils.getCityName(location, getApplicationContext()).getLocality();
        double latitude = location.latitude;
        double longitude = location.longitude;

        getEventsTask(cityName, latitude, longitude);
    }

    private void getEventsTask(String cityName, double latitude, double longitude) {
        Retrofit retrofit = ApiClient.getInstance().getClient();

        Map<String, Object> request = new HashMap<>();
        request.put("token", SessionManager.getToken());

        Map<String, Object> body = new HashMap<>();
        body.put("cityName", cityName);
        body.put("latitude", latitude);
        body.put("longitude", longitude);
        body.put("actualDate", new Date());

        request.put("body", body);

        EventService service = retrofit.create(EventService.class);
        Call<List<Event>> userCall = service.getEvents(request);
        userCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    Log.d("EventService", "Successfully download eventList...");

                    addMarkers(response.body());
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
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("EventService", "onStart()");
        if (!isRunning && onMapReady)
            scheduleAlarm();
    }

    @Override
    public View getInfoWindow(final Marker marker) {
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
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        HashMap<String, String> params = new HashMap<>();
        params.put("latitude", Double.toString(marker.getPosition().latitude));
        params.put("longitude", Double.toString(marker.getPosition().longitude));
        params.put("context", "SearchEventActivity");
        ActivityUtils.change(this, EventDetailsActivity.class, params);
    }

}
