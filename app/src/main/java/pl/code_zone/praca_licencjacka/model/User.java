package pl.code_zone.praca_licencjacka.model;

import com.google.gson.annotations.SerializedName;

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
    long dateCreation;
    @SerializedName("dateUpdated")
    long dateUpdated;
    @SerializedName("dateDeleted")
    long dateDeleted;
    @SerializedName("userImage")
    String userImage;
    @SerializedName("usersLogo")
    UsersLogo usersLogo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public long getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(long dateCreation) {
        this.dateCreation = dateCreation;
    }

    public long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public long getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(long dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsersLogo getUsersLogo() {
        return usersLogo;
    }

    public void setUsersLogo(UsersLogo usersLogo) {
        this.usersLogo = usersLogo;
    }
}
