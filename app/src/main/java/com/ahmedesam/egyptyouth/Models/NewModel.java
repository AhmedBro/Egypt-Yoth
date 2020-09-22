package com.ahmedesam.egyptyouth.Models;

import java.util.ArrayList;

public class NewModel {
    String mHeader, mNew, mImage, mLikeNumber, mPostID;
    ArrayList<String> mUserLiked;

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

    public ArrayList<String> getmUserLiked() {
        return mUserLiked;
    }

    public String getmNew() {
        return mNew;
    }

    public String getmImage() {
        return mImage;
    }

    public String getmLikeNumber() {
        return mLikeNumber;
    }

    public String getmPostID() {
        return mPostID;
    }
}
