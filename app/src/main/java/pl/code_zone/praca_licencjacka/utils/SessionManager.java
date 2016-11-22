package pl.code_zone.praca_licencjacka.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by MSI on 2016-11-13.
 */
public class SessionManager {

    // Instance
    private static SessionManager ourInstance;

    // Shared Preferences
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    // Context
    private static Context context;

    // Shared pref mode
    private static final int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "PracaLicencjacka";

    //Shared preferences keys
    private static final String KEY_IS_TOKEN = "token";
    private static final String KEY_IS_MESSAGE = "message";

    private SessionManager() { }

    private static void init(Context context) {
        SessionManager.context = context;
        SessionManager.sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SessionManager.editor = sharedPreferences.edit();
    }

    public static SessionManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SessionManager();
            ourInstance.init(context);
        }
        return SessionManager.ourInstance;
    }

    public static void setToken(String token) {
        editor.putString(KEY_IS_TOKEN, token);
        editor.commit();
    }

    public static String getToken() {
        return sharedPreferences.getString(KEY_IS_TOKEN, "empty");
    }

    public static void setMessage(String message) {
        editor.putString(KEY_IS_MESSAGE, message);
        editor.commit();
    }

    public static String getMessaage() {
        String msg = sharedPreferences.getString(KEY_IS_MESSAGE, null);
        setMessage(null);
        return msg;
    }

    public static void clearSession() {
        editor.clear();
        editor.commit();
    }
}
