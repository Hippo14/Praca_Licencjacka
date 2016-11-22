package pl.code_zone.praca_licencjacka.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MSI on 2016-11-13.
 */
public class Event implements Serializable {

    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("longitude")
    double longitude;
    @SerializedName("latitude")
    double latitude;
    @SerializedName("users")
    User user;
    @SerializedName("category")
    Category category;
    @SerializedName("deleted")
    Byte deleted;
    @SerializedName("active")
    Byte active;
    @SerializedName("date_creation")
    long dateCreation;
    @SerializedName("date_updated")
    long dateUpdated;
    @SerializedName("date_ending")
    long dateEnding;

    public Event() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public Byte getActive() {
        return active;
    }

    public void setActive(Byte active) {
        this.active = active;
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

    public long getDateEnding() {
        return dateEnding;
    }

    public void setDateEnding(long dateEnding) {
        this.dateEnding = dateEnding;
    }
}