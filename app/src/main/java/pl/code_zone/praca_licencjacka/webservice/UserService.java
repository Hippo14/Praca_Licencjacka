package pl.code_zone.praca_licencjacka.webservice;

import pl.code_zone.praca_licencjacka.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by MSI on 2016-10-14.
 */

public interface UserService {

    @GET("user/get/{email}/{password}")
    Call<User> getUserByEmailAndPassword(@Path("email") String name, @Path("password") String password);

}
