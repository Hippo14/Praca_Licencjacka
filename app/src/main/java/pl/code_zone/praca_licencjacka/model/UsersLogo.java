package pl.code_zone.praca_licencjacka.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MSI on 2017-05-03.
 */

public class UsersLogo {

    @SerializedName("id")
    int id;
    @SerializedName("image")
    String image;

    public UsersLogo() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
