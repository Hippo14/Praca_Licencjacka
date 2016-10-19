package pl.code_zone.praca_licencjacka.webservice.credentials;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MSI on 2016-10-19.
 */

public class Token implements Serializable {

    @SerializedName("token")
    String token;

    public Token() { }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
