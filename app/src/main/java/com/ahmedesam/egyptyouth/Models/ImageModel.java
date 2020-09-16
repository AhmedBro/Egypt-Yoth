package com.ahmedesam.egyptyouth.Models;

public class ImageModel {
    String url , id ;

    public ImageModel(String url, String id) {
        this.url = url;
        this.id = id;
    }

    public ImageModel() {
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }
}
