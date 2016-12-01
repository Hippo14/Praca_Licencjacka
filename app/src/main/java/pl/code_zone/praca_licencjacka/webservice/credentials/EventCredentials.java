package pl.code_zone.praca_licencjacka.webservice.credentials;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MSI on 2016-12-01.
 */
public class EventCredentials implements Serializable {

    @SerializedName("cityName")
    String cityName;
    @SerializedName("latitude")
    double latitude;
    @SerializedName("longitude")
    double longitude;

    public EventCredentials() {}

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
