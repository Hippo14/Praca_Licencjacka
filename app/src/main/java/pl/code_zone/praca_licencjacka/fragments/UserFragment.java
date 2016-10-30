package pl.code_zone.praca_licencjacka.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.code_zone.praca_licencjacka.R;

/**
 * Created by MSI on 2016-10-30.
 */

public class UserFragment extends Fragment {

    // UI
    TextView mToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        mToken = (TextView) view.findViewById(R.id.token);

        Intent intent = getActivity().getIntent();
        String token= intent.getExtras().getString("token");
        mToken.setText(token);

        return view;
    }

}