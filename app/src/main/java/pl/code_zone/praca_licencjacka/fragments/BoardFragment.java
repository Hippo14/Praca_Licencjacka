package pl.code_zone.praca_licencjacka.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.code_zone.praca_licencjacka.EventDetailsActivity;
import pl.code_zone.praca_licencjacka.LoginActivity;
import pl.code_zone.praca_licencjacka.MainActivity;
import pl.code_zone.praca_licencjacka.R;
import pl.code_zone.praca_licencjacka.adapter.BoardAdapter;
import pl.code_zone.praca_licencjacka.config.ApiClient;
import pl.code_zone.praca_licencjacka.row.BoardRow;
import pl.code_zone.praca_licencjacka.utils.ActivityUtils;
import pl.code_zone.praca_licencjacka.utils.Config;
import pl.code_zone.praca_licencjacka.utils.GsonUtils;
import pl.code_zone.praca_licencjacka.utils.RsaUtils;
import pl.code_zone.praca_licencjacka.utils.SessionManager;
import pl.code_zone.praca_licencjacka.webservice.EventService;
import pl.code_zone.praca_licencjacka.webservice.UserService;
import pl.code_zone.praca_licencjacka.webservice.credentials.EmailPassCred;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MSI on 2016-10-30.
 */

public class BoardFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = BoardFragment.class.getSimpleName();

    private ListView listView;
    private BoardAdapter boardAdapter;
    private List<BoardRow> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add to board list
        addToBoard();
    }

    private void addToBoard() {
        list = new ArrayList<>();
        boardAdapter = new BoardAdapter(getActivity().getApplicationContext(), list);
        // Download list from webservice
        getBoardList();

//        list.add(new BoardRow("eventName", "userName"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(boardAdapter);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BoardRow boardRow = list.get(position);

        HashMap<String, String> params = new HashMap<>();
        params.put("latitude", Double.toString(boardRow.getLatitude()));
        params.put("longitude", Double.toString(boardRow.getLongitude()));
        params.put("context", "SearchEventActivity");
        ActivityUtils.change(this.getActivity(), EventDetailsActivity.class, params);
    }

    public void getBoardList() {
        LatLng location = SessionManager.getLocation();
        if (location != null)
            boardTask();
    }

    public void boardTask() {
        Retrofit retrofit = ApiClient.getInstance().getClient();

        EventService service = retrofit.create(EventService.class);

        Map<String, String> body = new HashMap<>();
        body.put("latitude", Double.toString(SessionManager.getLocation().latitude));
        body.put("longitude", Double.toString(SessionManager.getLocation().longitude));

        Map<String, Object> params = new HashMap<>();
        params.put("token", SessionManager.getToken());
        params.put("body", body);

        Call<Map<String, Map<String, String>>> boardCall = service.getBoard(params);
        boardCall.enqueue(new Callback<Map<String, Map<String, String>>>() {
            @Override
            public void onResponse(Call<Map<String, Map<String, String>>> call, Response<Map<String, Map<String, String>>> response) {
                if (response.body() != null) {
                    // Add hashmap to list
                    for (Map.Entry<String, Map<String, String>> elem : response.body().entrySet()) {
                        String key = elem.getKey();
                        Map<String, String> value = elem.getValue();

                        list.add(new BoardRow(value.get("name"), value.get("username"), value.get("description"), value.get("image"), value.get("latitude"), value.get("longitude")));
                    }

                    // Refresh list
                    boardAdapter.notifyDataSetChanged();

                    Log.d(TAG, String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Map<String, String>>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

}
