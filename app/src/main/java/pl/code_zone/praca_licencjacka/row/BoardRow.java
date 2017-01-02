package pl.code_zone.praca_licencjacka.row;

/**
 * Created by MSI on 2016-12-27.
 */

public class BoardRow {

    private String eventName;
    private String username;
    private String description;

    public BoardRow (String eventName, String username, String description) {
        this.eventName = eventName;
        this.username = username;
        this.description = description;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
