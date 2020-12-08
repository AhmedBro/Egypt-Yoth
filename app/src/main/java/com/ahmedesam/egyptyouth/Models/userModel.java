package com.ahmedesam.egyptyouth.Models;

public class userModel {
    String mName , mId , mMail , mImage , mAge , mDescription , mAddress , mLikeNumber;



    public userModel(String mName, String mId, String mMail, String mImage, String mDescription, String mLikeNumber) {
        this.mName = mName;
        this.mId = mId;
        this.mMail = mMail;
        this.mImage = mImage;
        this.mDescription = mDescription;
        this.mLikeNumber = mLikeNumber;
    }

    public String getmAge() {
        return mAge;
    }

    public userModel(String mName, String mId, String mMail , String mImage , String mLikeNumber ) {
        this.mName = mName;
        this.mId = mId;
        this.mMail = mMail;
        this.mImage = mImage;
        this.mLikeNumber = mLikeNumber;

    }

    public String getmLikeNumber() {
        return mLikeNumber;
    }

    public userModel() {
    }

    public userModel(String mName, String mId, String mMail, String mImage, String mAge, String mDescription, String mAddress, String mLikeNumber) {
        this.mName = mName;
        this.mId = mId;
        this.mMail = mMail;
        this.mImage = mImage;
        this.mAge = mAge;
        this.mDescription = mDescription;
        this.mAddress = mAddress;
        this.mLikeNumber = mLikeNumber;
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
