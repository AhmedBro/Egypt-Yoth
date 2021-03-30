package com.ahmedesam.egyptyouth.Models;

public class mChats {
    String  mName, mImage, mId , mLastMessage , mTime;

    public mChats(String mPhone, String id, String mName, String mImage) {

        this.mName = mName;
        this.mImage = mImage;
        mId = id;
    }

    public mChats() {
    }

    public mChats(String mName, String mImage, String mId, String mLastMessage, String mTime) {

        this.mName = mName;
        this.mImage = mImage;
        this.mId = mId;
        this.mLastMessage = mLastMessage;
        this.mTime = mTime;
    }

    public String getmId() {
        return mId;
    }

    public String getmLastMessage() {
        return mLastMessage;
    }

    public String getmTime() {
        return mTime;
    }


    public String getmName() {
        return mName;
    }

    public String getmImage() {
        return mImage;
    }
}
