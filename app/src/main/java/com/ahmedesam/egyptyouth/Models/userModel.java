package com.ahmedesam.egyptyouth.Models;

public class userModel {
    String mName , mId , mMail , mImage , mDate , mDescription , mAddress , mLikeNumber;

    public String getmDate() {
        return mDate;
    }

    public userModel(String mName, String mId, String mMail , String mImage , String mDate ) {
        this.mName = mName;
        this.mId = mId;
        this.mMail = mMail;
        this.mImage = mImage;
        this.mDate = mDate;

    }

    public String getmLikeNumber() {
        return mLikeNumber;
    }

    public userModel() {
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmImage() {
        return mImage;
    }

    public String getmName() {
        return mName;
    }

    public String getmId() {
        return mId;
    }

    public String getmMail() {
        return mMail;
    }
}
