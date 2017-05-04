package pl.code_zone.praca_licencjacka.row;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by MSI on 2016-12-27.
 */

public class BoardRow {

    private String eventName;
    private String username;
    private String description;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    private Bitmap image;

    public BoardRow (String eventName, String username, String description, String imageBase64) {
        this.eventName = eventName;
        this.username = username;
        this.description = description;
        byte[] decodedString = Base64.decode(imageBase64, Base64.NO_WRAP);
        this.image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
