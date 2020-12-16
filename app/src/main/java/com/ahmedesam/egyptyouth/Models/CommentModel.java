package com.ahmedesam.egyptyouth.Models;

public class CommentModel {
    String mText , mUserName , mUserImage , mId , mUserId;

    public CommentModel(String mText, String mUserName, String mUserImage, String mId, String mUserId) {
        this.mText = mText;
        this.mUserName = mUserName;
        this.mUserImage = mUserImage;
        this.mId = mId;
        this.mUserId = mUserId;
    }

    public CommentModel() {
    }

    public String getmText() {
        return mText;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmUserImage() {
        return mUserImage;
    }

    public String getmId() {
        return mId;
    }

    public String getmUserId() {
        return mUserId;
    }
}
