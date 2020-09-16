package com.ahmedesam.egyptyouth.Models;

public class NewModel {
    String mHeader , mNew , mImage;

    public NewModel(String mHeader, String mNew, String mImage) {
        this.mHeader = mHeader;
        this.mNew = mNew;
        this.mImage = mImage;
    }

    public NewModel() {
    }

    public String getmHeader() {
        return mHeader;
    }

    public String getmNew() {
        return mNew;
    }

    public String getmImage() {
        return mImage;
    }
}
