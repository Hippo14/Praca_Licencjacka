package pl.code_zone.praca_licencjacka.row;

/**
 * Created by MSI on 2017-01-02.
 */

public class EventRow {

    private String eventName;
    private String description;
    private String latitude;
    private String longitude;

    public EventRow (String eventName, String description, String latitude, String longitude) {
        this.eventName = eventName;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEventName() {
        return eventName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
