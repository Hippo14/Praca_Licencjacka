package pl.code_zone.praca_licencjacka.webservice;

import java.util.List;

import pl.code_zone.praca_licencjacka.model.Category;
import pl.code_zone.praca_licencjacka.webservice.credentials.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by MSI on 2017-05-01.
 */

public interface CategoryService {

    @POST("category/")
    Call<List<Category>> getAll(@Body Token token);

}
