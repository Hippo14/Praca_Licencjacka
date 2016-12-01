package pl.code_zone.praca_licencjacka.webservice.credentials;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MSI on 2016-12-01.
 */

public class TokenEventCred {

    @SerializedName("token")
    String token;
    @SerializedName("body")
    EventCredentials body;

    public TokenEventCred() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public EventCredentials getBody() {
        return body;
    }

    public void setBody(EventCredentials body) {
        this.body = body;
    }
}
