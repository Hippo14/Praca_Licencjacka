package pl.code_zone.praca_licencjacka.webservice;

import java.util.Map;

import pl.code_zone.praca_licencjacka.model.User;
import pl.code_zone.praca_licencjacka.webservice.credentials.EmailPassCred;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by MSI on 2016-10-14.
 */

public interface UserService {

    @POST("user/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<String> getUserByEmailAndPassword(@Body EmailPassCred emailPassCred);

    @POST("user/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<String> registerNewUser(@Body User user);

    @POST("user/token")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<User> getUserByToken(@Body Map<String, Object> params);

    @POST("user/logo")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Map<String, String>> getUserLogo(@Body Map<String, Object> params);

    @POST("user/logo")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Boolean> setUserLogo(@Body Map<String, Object> params);
}
