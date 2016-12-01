package pl.code_zone.praca_licencjacka.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MSI on 2016-10-14.
 */

public class ActivityUtils {

    public static void change(Context actualContext, Class nextClazz) {
        Intent intent = new Intent(actualContext, nextClazz);
        actualContext.startActivity(intent);
    }

    public static void change(Context actualContext, Class nextClazz, HashMap<String, String> list) {
        Intent intent = new Intent(actualContext, nextClazz);
        for (Map.Entry<String, String> item : list.entrySet()) {
            String key = item.getKey();
            String value = item.getValue();
            intent.putExtra(key, value);
        }
        actualContext.startActivity(intent);
    }

}
