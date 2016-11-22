package pl.code_zone.praca_licencjacka.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MSI on 2016-11-20.
 */

public class Category {

    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;

    public Category() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
