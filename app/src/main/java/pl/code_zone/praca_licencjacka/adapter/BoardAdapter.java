package pl.code_zone.praca_licencjacka.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.code_zone.praca_licencjacka.R;
import pl.code_zone.praca_licencjacka.row.BoardRow;
import pl.code_zone.praca_licencjacka.utils.ImageConverter;

/**
 * Created by MSI on 2016-12-27.
 */

public class BoardAdapter extends ArrayAdapter<BoardRow> {

    TextView eventName;
    TextView username;
    TextView description;
    ImageView imageView;

    public BoardAdapter(Context context, List<BoardRow> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BoardRow boardRow = getItem(position);

        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.board_adapter, parent, false);

        eventName = (TextView) convertView.findViewById(R.id.eventName);
        username = (TextView) convertView.findViewById(R.id.username);
        description = (TextView) convertView.findViewById(R.id.description);
        imageView = (ImageView) convertView.findViewById(R.id.item_imageView);


        eventName.setText(boardRow.getEventName());
        username.setText(boardRow.getUsername());
        description.setText(boardRow.getDescription());
        imageView.setImageBitmap(boardRow.getImage());

        return convertView;
    }
}
