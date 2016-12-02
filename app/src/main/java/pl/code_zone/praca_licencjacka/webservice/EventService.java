package pl.code_zone.praca_licencjacka.webservice;


import java.util.List;

import okhttp3.ResponseBody;
import pl.code_zone.praca_licencjacka.model.Event;
import pl.code_zone.praca_licencjacka.model.Marker;
import pl.code_zone.praca_licencjacka.webservice.credentials.Token;
import pl.code_zone.praca_licencjacka.webservice.credentials.TokenEventCred;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by MSI on 2016-11-13.
 */

public interface EventService {

    @POST("events/add")
    Call<ResponseBody> addNewEvent(@Body Token event);

    @POST("events/get")
    Call<List<Event>> getEvents(@Body TokenEventCred eventCredentials);

    @POST("events/marker")
    Call<Marker> getMarkerDetails(@Body TokenEventCred cred);

}
