package pl.code_zone.praca_licencjacka;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    ListView userList;
    ArrayAdapter<String> adapter;

    String context;
    Double latitude;
    Double longitude;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        markerTitle = (TextView) findViewById(R.id.title);
        markerUsername = (TextView) findViewById(R.id.username);
        markerDescription = (TextView) findViewById(R.id.description);

        userList = (ListView) findViewById(R.id.userList);

        context = (String) getIntent().getExtras().get("context");
        latitude = Double.parseDouble((String) getIntent().getExtras().get("latitude"));
        longitude = Double.parseDouble((String) getIntent().getExtras().get("longitude"));

        progressDialog = new ProgressDialog(this);
        populate();
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
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.e(TAG, t.toString());
                progressDialog.dismiss();
            }
        });
    }

    public void getUserListFromEvent() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.URL_WEBSERVICE)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.create()))
                .build();

        EventService service = retrofit.create(EventService.class);

        Map<String, String> body = new HashMap<>();
        body.put("latitude", Double.toString(latitude));
        body.put("longitude", Double.toString(longitude));

        Map<String, Object> params = new HashMap<>();
        params.put("token", SessionManager.getToken());
        params.put("body", body);

        Call<Map<String, Map<String, String>>> eventCall = service.getUserListEvent(params);
        eventCall.enqueue(new Callback<Map<String, Map<String, String>>>() {
            @Override
            public void onResponse(Call<Map<String, Map<String, String>>> call, Response<Map<String, Map<String, String>>> response) {
                if (response.isSuccessful()) {
                    List<String> list = new ArrayList<>();

                    // Add hashmap to list
                    for (Map.Entry<String, Map<String, String>> elem : response.body().entrySet()) {
                        Map<String, String> value = elem.getValue();
                        list.add(value.get("user"));
                    }

                    adapter = new ArrayAdapter<>(EventDetailsActivity.this, android.R.layout.simple_list_item_1, list);
                    userList.setAdapter(adapter);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Map<String, String>>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void populate() {
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Get event details
        getDetails(latitude, longitude);

        // Get user list from event
        getUserListFromEvent();
    }
}
