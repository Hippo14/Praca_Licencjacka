package pl.code_zone.praca_licencjacka.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import pl.code_zone.praca_licencjacka.R;
import pl.code_zone.praca_licencjacka.model.Category;

/**
 * Created by MSI on 2017-05-01.
 */

public class SpinnerAdapter extends ArrayAdapter<Category> {

    private Context context;
    private List<Category> values;

    private TextView mTitle;

    public SpinnerAdapter(Context context, int resource, List<Category> values) {
        super(context, resource);

        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    public Category getItem(int position) {
        return  values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner, parent, false);

        mTitle = (TextView) convertView.findViewById(R.id.title);
        mTitle.setText(getItem(position).getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner, parent, false);

        mTitle = (TextView) convertView.findViewById(R.id.title);
        mTitle.setText(getItem(position).getName());

        return convertView;
    }

}
