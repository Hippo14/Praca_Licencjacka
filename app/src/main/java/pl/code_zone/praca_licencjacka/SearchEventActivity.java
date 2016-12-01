package pl.code_zone.praca_licencjacka;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import pl.code_zone.praca_licencjacka.utils.ActivityUtils;
import pl.code_zone.praca_licencjacka.utils.LocationUtils;
import pl.code_zone.praca_licencjacka.utils.SessionManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SearchEventActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener {

    private GoogleMap mMap;
    private boolean isRunning = false;
    private ScheduledThreadPoolExecutor scheduler = null;
    private LatLng location;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_event);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    private void getEvents(LatLng location) {

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
}
