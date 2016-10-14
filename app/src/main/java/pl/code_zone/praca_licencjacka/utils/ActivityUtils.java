package pl.code_zone.praca_licencjacka.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by MSI on 2016-10-14.
 */

public class ActivityUtils {

    public static void change(Context actualContext, Class nextClazz) {
        Intent intent = new Intent(actualContext, nextClazz);
        actualContext.startActivity(intent);
    }

}
