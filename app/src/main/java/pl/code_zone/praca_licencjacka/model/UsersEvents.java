package pl.code_zone.praca_licencjacka.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MSI on 2017-05-03.
 */

public class UsersEvents {

    @SerializedName("id")
    int id;
    @SerializedName("events")
    Event event;
    @SerializedName("users")
    User user;

    public UsersEvents() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
