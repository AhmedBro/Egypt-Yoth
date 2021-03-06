package com.ahmedesam.egyptyouth.Models;

import java.util.ArrayList;

public class PostModel {
    String mPost, mImage, mLikeNumber, mPostID,mUserId,mVideo , mUserName, mUserImage , mCommentsNumber;

    public PostModel(String mPost, String mImage, String mLikeNumber, String mPostID, String mUserId, String mVideo, String mUserName, String mUserImage , String mCommentsNumber) {
        this.mPost = mPost;
        this.mImage = mImage;
        this.mLikeNumber = mLikeNumber;
        this.mPostID = mPostID;
        this.mUserId = mUserId;
        this.mVideo = mVideo;
        this.mUserName = mUserName;
        this.mUserImage = mUserImage;
        this.mCommentsNumber = mCommentsNumber;
    }

    public PostModel() {
    }

    public String getmCommentsNumber() {
        return mCommentsNumber;
    }

    public String getmPost() {
        return mPost;
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

    public String getmUserId() {
        return mUserId;
    }

    public String getmVideo() {
        return mVideo;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmUserImage() {
        return mUserImage;
    }
}
