package pl.code_zone.praca_licencjacka.row;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import pl.code_zone.praca_licencjacka.model.UsersEvents;

/**
 * Created by MSI on 2017-05-03.
 */

public class EventDetailsRow {

    private String name;
    private Bitmap image;

    public EventDetailsRow(UsersEvents usersEvents) {
        this.name = usersEvents.getUser().getName();
        byte[] decodedString = Base64.decode(usersEvents.getUser().getUsersLogo().getImage(), Base64.NO_WRAP);
        this.image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
