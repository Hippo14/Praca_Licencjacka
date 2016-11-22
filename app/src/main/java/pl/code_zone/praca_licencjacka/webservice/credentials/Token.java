package pl.code_zone.praca_licencjacka.webservice.credentials;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import pl.code_zone.praca_licencjacka.model.Event;

/**
 * Created by MSI on 2016-10-19.
 */

public class Token implements Serializable {

    @SerializedName("token")
    String token;
    @SerializedName("body")
    Event body;

    public Token() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Event getBody() {
        return body;
    }

    public void setBody(Event body) {
        this.body = body;
    }
}
