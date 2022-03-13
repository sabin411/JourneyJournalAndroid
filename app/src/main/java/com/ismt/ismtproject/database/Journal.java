package com.ismt.ismtproject.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "journal")
public class Journal {
    private String title;
    private String description;
    private String image;
    private double lat;
    private double lng;
    private String date;

    @PrimaryKey(autoGenerate = true)
    private int id;

    public Journal(String title, String description, String image, double lat, double lng, int id, String date) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }
}
