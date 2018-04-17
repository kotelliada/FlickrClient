package io.github.kotelliada.flickrlient.model;

public class Photo {
    private String id;
    private String title;
    private String url;
    private String latitude;
    private String longitude;

    public Photo() {
    }

    public Photo(String id, String title, String url, String latitude, String longitude) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}