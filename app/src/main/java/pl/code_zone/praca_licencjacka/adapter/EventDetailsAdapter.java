package pl.code_zone.praca_licencjacka.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.code_zone.praca_licencjacka.R;
import pl.code_zone.praca_licencjacka.model.UsersEvents;
import pl.code_zone.praca_licencjacka.row.EventDetailsRow;
import pl.code_zone.praca_licencjacka.utils.ImageConverter;

/**
 * Created by MSI on 2017-05-03.
 */

public class EventDetailsAdapter extends ArrayAdapter<EventDetailsRow> {

    TextView username;
    ImageView imageView;

    public EventDetailsAdapter(Context context, List<EventDetailsRow> resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventDetailsRow eventRow = getItem(position);

        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_details_adapter, parent, false);

        username = (TextView) convertView.findViewById(R.id.username);
        imageView = (ImageView) convertView.findViewById(R.id.item_imageView);

        username.setText(eventRow.getName());
        imageView.setImageBitmap(ImageConverter.getRoundedCornerBitmap(eventRow.getImage(), 100));

        return convertView;
    }
}
