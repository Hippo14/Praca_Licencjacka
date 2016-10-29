package pl.code_zone.praca_licencjacka.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

/**
 * Created by MSI on 2016-10-14.
 */

public class User {

    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("password")
    String password;
    @SerializedName("email")
    String email;
    @SerializedName("profiles")
    Profile profile;
    @SerializedName("deleted")
    Byte deleted;
    @SerializedName("dateCreation")
    Timestamp dateCreation;
    @SerializedName("dateUpdated")
    Timestamp dateUpdated;
    @SerializedName("dateDeleted")
    Timestamp dateDeleted;
    @SerializedName("userImage")
    String userImage;

    public Timestamp getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(Timestamp dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

}
