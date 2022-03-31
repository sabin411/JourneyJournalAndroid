package com.ismt.ismtproject;

public class Journal {
    String placeName, description, locationImage, shortCaption, userId, date, journalKey;

    Journal(){

    }

    public Journal(String placeName, String description, String locationImage, String shortCaption, String userId, String date, String journalKey) {
        this.placeName = placeName;
        this.description = description;
        this.locationImage = locationImage;
        this.shortCaption = shortCaption;
        this.userId = userId;
        this.date = date;
        this.journalKey = journalKey;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationImage() {
        return locationImage;
    }

    public void setLocationImage(String locationImage) {
        this.locationImage = locationImage;
    }

    public String getShortCaption() {
        return shortCaption;
    }

    public void setShortCaption(String shortCaption) {
        this.shortCaption = shortCaption;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJournalKey() {
        return journalKey;
    }

    public void setJournalKey(String journalKey) {
        this.journalKey = journalKey;
    }
}
