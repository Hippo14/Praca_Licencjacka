package pl.code_zone.praca_licencjacka.webservice.credentials;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MSI on 2016-10-19.
 */
public class EmailPassCred implements Serializable {

    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;

    public EmailPassCred() { }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
