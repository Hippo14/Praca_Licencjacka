package pl.code_zone.praca_licencjacka.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by MSI on 2016-12-01.
 */

public class GpsManager {

    public static final int GPS_LOCATION = 1;
    public static final int GPS_ON = 1;
    public static final int GPS_OFF = 0;
    private static Activity activity;
    private static LocationManager locationManager;
    private static LatLng latLng = new LatLng(0.0, 0.0);

    public static void init(Activity activity1, LocationManager locationManager1) {
        activity = activity1;
        locationManager = locationManager1;
    }

    public static void dialog() {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(activity);
        adBuilder
                .setMessage("GPS is disabled in your device. Enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent gps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        activity.startActivity(gps);
                        activity.startActivityForResult(gps, GPS_LOCATION);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = adBuilder.create();
        alertDialog.show();
    }

    public static void findLocation() {
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);

        HandlerThread t = new HandlerThread("GPS HandlerThread");
        t.start();

        try {
            locationManager.requestSingleUpdate(provider, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    synchronized (latLng) {
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        SessionManager.setLocation(latLng);
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            }, t.getLooper());
        }  catch (SecurityException e) {
            e.printStackTrace();
        }
    }

}
