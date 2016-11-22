package pl.code_zone.praca_licencjacka.webservice;

import okhttp3.ResponseBody;
import pl.code_zone.praca_licencjacka.webservice.credentials.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by MSI on 2016-11-13.
 */

public interface EventService {

    @POST("events/add")
    Call<ResponseBody> addNewEvent(@Body Token event);

}
