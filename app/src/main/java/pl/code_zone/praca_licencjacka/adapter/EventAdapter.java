package pl.code_zone.praca_licencjacka.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.code_zone.praca_licencjacka.R;
import pl.code_zone.praca_licencjacka.model.Event;
import pl.code_zone.praca_licencjacka.row.BoardRow;
import pl.code_zone.praca_licencjacka.row.EventRow;

/**
 * Created by MSI on 2017-01-02.
 */

public class EventAdapter extends ArrayAdapter<EventRow> {

    TextView eventName;
    TextView description;

    public EventAdapter(Context context, List<EventRow> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventRow eventRow = getItem(position);

        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.board_adapter, parent, false);

        eventName = (TextView) convertView.findViewById(R.id.eventName);
        description = (TextView) convertView.findViewById(R.id.description);

        eventName.setText(eventRow.getEventName());
        description.setText(eventRow.getDescription());

        return convertView;
    }
}
