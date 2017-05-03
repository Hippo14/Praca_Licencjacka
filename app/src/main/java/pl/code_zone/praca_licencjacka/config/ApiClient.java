package pl.code_zone.praca_licencjacka.config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import pl.code_zone.praca_licencjacka.utils.Config;
import pl.code_zone.praca_licencjacka.utils.GsonUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MSI on 2017-05-03.
 */
public class ApiClient {

    public static final String BASE_URL = Config.URL_WEBSERVICE;

    private static ApiClient ourInstance = new ApiClient();

    public static ApiClient getInstance() {
        if (ourInstance == null)
            ourInstance = new ApiClient();
        return ourInstance;
    }

    private ApiClient() {
    }

    private Retrofit retrofit = null;

    public Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.URL_WEBSERVICE)
                    .client(getRequestHeader())
                    .addConverterFactory(GsonConverterFactory.create(GsonUtils.create()))
                    .build();
        }
        return retrofit;
    }

    public OkHttpClient getRequestHeader() {
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }

}
