package pl.code_zone.praca_licencjacka.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.code_zone.praca_licencjacka.EventDetailsActivity;
import pl.code_zone.praca_licencjacka.R;
import pl.code_zone.praca_licencjacka.adapter.EventAdapter;
import pl.code_zone.praca_licencjacka.row.BoardRow;
import pl.code_zone.praca_licencjacka.row.EventRow;
import pl.code_zone.praca_licencjacka.utils.ActivityUtils;
import pl.code_zone.praca_licencjacka.utils.Config;
import pl.code_zone.praca_licencjacka.utils.GsonUtils;
import pl.code_zone.praca_licencjacka.utils.SessionManager;
import pl.code_zone.praca_licencjacka.webservice.EventService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MSI on 2016-10-30.
 */

public class EventsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = EventsFragment.class.getSimpleName();

    private ListView listView;
    private EventAdapter eventAdapter;
    private List<EventRow> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(eventAdapter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add to event list
        addToEvent();
    }

    private void addToEvent() {
        list = new ArrayList<>();
        eventAdapter = new EventAdapter(getActivity().getApplicationContext(), list);
        // Download list from webservice
        getEventList();
    }

    public void getEventList() {
        eventTask();
    }

    public void eventTask() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.URL_WEBSERVICE)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.create()))
                .build();

        EventService service = retrofit.create(EventService.class);

        Map<String, String> body = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("token", SessionManager.getToken());
        params.put("body", body);

        Call<Map<String, Map<String, String>>> boardCall = service.getEventsByUser(params);
        boardCall.enqueue(new Callback<Map<String, Map<String, String>>>() {
            @Override
            public void onResponse(Call<Map<String, Map<String, String>>> call, Response<Map<String, Map<String, String>>> response) {
                // Add hashmap to list
                for (Map.Entry<String, Map<String, String>> elem : response.body().entrySet()) {
                    String key = elem.getKey();
                    Map<String, String> value = elem.getValue();

                    list.add(new EventRow(value.get("name"), value.get("description"), value.get("latitude"), value.get("longitude")));
                }

                // Refresh list
                eventAdapter.notifyDataSetChanged();

                Log.d(TAG, String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Map<String, Map<String, String>>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventRow eventRow = list.get(position);
        HashMap<String, String> params = new HashMap<>();
        params.put("latitude", eventRow.getLatitude());
        params.put("longitude", eventRow.getLongitude());
        ActivityUtils.change(getContext(), EventDetailsActivity.class, params);
    }
}