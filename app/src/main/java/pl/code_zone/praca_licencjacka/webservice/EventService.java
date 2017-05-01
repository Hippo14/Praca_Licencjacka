package pl.code_zone.praca_licencjacka.webservice;


import java.util.List;
import java.util.Map;

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
    Call<String> addNewEvent(@Body Token event);

    @POST("events/get")
    Call<List<Event>> getEvents(@Body TokenEventCred eventCredentials);

    @POST("events/marker")
    Call<Marker> getMarkerDetails(@Body TokenEventCred cred);

    @POST("events/details")
    Call<Event> getEventDetails(@Body TokenEventCred cred);

    @POST("events/board")
    Call<Map<String, Map<String, String>>> getBoard(@Body Map<String, Object> params);

    @POST("events/getByUser")
    Call<Map<String,Map<String,String>>> getEventsByUser(@Body Map<String, Object> params);

    @POST("events/getUserListEvent")
    Call<Map<String,Map<String,String>>> getUserListEvent(@Body Map<String, Object> params);

    @POST("events/addToEvent")
    Call<String> addUserToEvent(@Body Map<String, Object> params);
}
