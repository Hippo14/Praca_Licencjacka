package pl.code_zone.praca_licencjacka.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MSI on 2016-12-02.
 */

public class Marker implements Serializable {

    @SerializedName("title")
    String title;
    @SerializedName("username")
    String username;
    @SerializedName("description")
    String description;

    public Marker() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
