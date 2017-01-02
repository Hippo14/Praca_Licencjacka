package pl.code_zone.praca_licencjacka.row;

/**
 * Created by MSI on 2017-01-02.
 */

public class EventRow {

    private String eventName;
    private String description;

    public EventRow (String eventName, String description) {
        this.eventName = eventName;
        this.description = description;
    }

    public String getEventName() {
        return eventName;
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
