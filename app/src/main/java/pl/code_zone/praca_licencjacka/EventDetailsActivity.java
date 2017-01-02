package pl.code_zone.praca_licencjacka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

import pl.code_zone.praca_licencjacka.model.Event;
import pl.code_zone.praca_licencjacka.utils.Config;
import pl.code_zone.praca_licencjacka.utils.GsonUtils;
import pl.code_zone.praca_licencjacka.utils.SessionManager;
import pl.code_zone.praca_licencjacka.webservice.EventService;
import pl.code_zone.praca_licencjacka.webservice.credentials.EventCredentials;
import pl.code_zone.praca_licencjacka.webservice.credentials.TokenEventCred;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventDetailsActivity extends AppCompatActivity {

    private static final String TAG = EventDetailsActivity.class.getSimpleName();

    TextView markerTitle;
    TextView markerUsername;
    TextView markerDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        markerTitle = (TextView) findViewById(R.id.title);
        markerUsername = (TextView) findViewById(R.id.username);
        markerDescription = (TextView) findViewById(R.id.description);

        getDetails(Double.parseDouble((String) getIntent().getExtras().get("latitude")), Double.parseDouble((String) getIntent().getExtras().get("longitude")));
    }

    public void getDetails(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.URL_WEBSERVICE)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.create()))
                .build();

        EventService service = retrofit.create(EventService.class);
        TokenEventCred cred = new TokenEventCred();
        EventCredentials eventCred = new EventCredentials();
        eventCred.setLatitude(latitude);
        eventCred.setLongitude(longitude);
        cred.setToken(SessionManager.getToken());
        cred.setBody(eventCred);

        Call<Event> userCall = service.getEventDetails(cred);
        userCall.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful()) {
                    markerTitle.setText(response.body().getName());
                    markerUsername.setText(response.body().getUser().getName());
                    markerDescription.setText(response.body().getDescription());
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
