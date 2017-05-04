package pl.code_zone.praca_licencjacka.webservice;


import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import pl.code_zone.praca_licencjacka.model.Event;
import pl.code_zone.praca_licencjacka.model.Marker;
import pl.code_zone.praca_licencjacka.model.UsersEvents;
import pl.code_zone.praca_licencjacka.webservice.credentials.Token;
import pl.code_zone.praca_licencjacka.webservice.credentials.TokenEventCred;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by MSI on 2016-11-13.
 */

public interface EventService {

    @POST("events/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<String> addNewEvent(@Body Token event);

    @POST("events/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<Event>> getEvents(@Body Map<String, Object> params);

    @POST("events/marker")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Marker> getMarkerDetails(@Body TokenEventCred cred);

    @POST("events/details")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Event> getEventDetails(@Body TokenEventCred cred);

    @POST("events/board")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Map<String, Map<String, String>>> getBoard(@Body Map<String, Object> params);

    @POST("events/user")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Map<String,Map<String,String>>> getEventsByUser(@Body Map<String, Object> params);

    @POST("events/list/user")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<UsersEvents>> getUserListEvent(@Body Map<String, Object> params);

    @POST("events/user")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<String> addUserToEvent(@Body Map<String, Object> params);
}
