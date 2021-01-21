package com.ahmedesam.egyptyouth.Models;

public class ImageModel {
    String url , id , mUserId;

    public ImageModel(String url, String id, String mUserId) {
        this.url = url;
        this.id = id;
        this.mUserId = mUserId;
    }

    public ImageModel() {
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getmUserId() {
        return mUserId;
    }
}
