package pl.code_zone.praca_licencjacka.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by MSI on 2016-11-29.
 */

public class LocationUtils {

    public static Address getCityName(LatLng latLng, Context context) {
        Address cityName = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

}
