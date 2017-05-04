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

    @POST("events/register")
    Call<String> addNewEvent(@Body Token event);

    @POST("events/")
    Call<List<Event>> getEvents(@Body Map<String, Object> params);

    @POST("events/marker")
    Call<Marker> getMarkerDetails(@Body TokenEventCred cred);

    @POST("events/details")
    Call<Event> getEventDetails(@Body TokenEventCred cred);

    @POST("events/board")
    Call<Map<String, Map<String, String>>> getBoard(@Body Map<String, Object> params);

    @POST("events/user")
    Call<Map<String,Map<String,String>>> getEventsByUser(@Body Map<String, Object> params);

    @POST("events/list/user")
    Call<List<UsersEvents>> getUserListEvent(@Body Map<String, Object> params);

    @POST("events/user/register")
    Call<String> addUserToEvent(@Body Map<String, Object> params);

    @POST("events/user/count")
    Call<Map<String,String>> getLikedEvents(@Body Map<String, Object> params);

    @POST("events/user/remove")
    Call<String> deleteUserFromEvent(Map<String, Object> params);

    @POST("events/user/status")
    Call<Boolean> getUserStatusEvent(Map<String, Object> params);
}
