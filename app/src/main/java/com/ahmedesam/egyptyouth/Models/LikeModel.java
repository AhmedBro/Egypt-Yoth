package com.ahmedesam.egyptyouth.Models;

public class LikeModel {
    String mId, mUserName, mUserImage;

    public LikeModel(String mId, String mUserName, String mUserImage) {
        this.mId = mId;
        this.mUserName = mUserName;
        this.mUserImage = mUserImage;
    }

    public LikeModel() {
    }

    public String getmId() {
        return mId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmUserImage() {
        return mUserImage;
    }
}
