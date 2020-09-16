package com.ahmedesam.egyptyouth.Shard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ahmedesam.egyptyouth.Ui.Activities.LogIn;

import java.util.HashMap;

public class ShardPrefrances {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    private int PRIVATE_MODE = 0;
    private Intent mIntent;


    private static final String PREF_NAME = "MyApp";

    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ID = "key_id";
    public static final String KEY_FNAME = "key_fname";
    public static final String KEY_EMAIL = "key_email";
    public static final String KEY_IMAGE = "key_image";
    public static final String KEY_IMAGEPATH = "key_image_path";
    public static final String KEY_BIRTH_DATE = "BirthDate";
    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_Address= "Address";


    /**
     * Constructor
     **/
    public ShardPrefrances(Context context) {
        this.mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }

    /**
     * Save User Details
     **/
    public void createLoginSession(Boolean status,
                                   String id,
                                   String fname,
                                   String email,
                                   String Image,
                                   String mBirthDate
    ) {

        mEditor.putBoolean(IS_LOGIN, status);
        mEditor.putString(KEY_ID, id);
        mEditor.putString(KEY_FNAME, fname);
        mEditor.putString(KEY_EMAIL, email);
        mEditor.putString(KEY_IMAGE, Image);
        mEditor.putString(KEY_BIRTH_DATE, mBirthDate);

        mEditor.commit();
    }

    public void EditImage(String Image) {
        mEditor.putString(KEY_IMAGE, Image);
        mEditor.commit();
    }
    public void EditAge(String mBirthDate) {
        mEditor.putString(KEY_BIRTH_DATE, mBirthDate);
        mEditor.commit();
    }
    public void EditDescription(String mBirthDate) {
        mEditor.putString(KEY_DESCRIPTION, mBirthDate);
        mEditor.commit();
    }
    public void EditAddress(String mBirthDate) {
        mEditor.putString(KEY_Address, mBirthDate);
        mEditor.commit();
    }
    public void EditName(String mBirthDate) {
        mEditor.putString(KEY_FNAME, mBirthDate);
        mEditor.commit();
    }
    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> users = new HashMap<String, String>();

        users.put(KEY_ID, mSharedPreferences.getString(KEY_ID, null));
        users.put(KEY_FNAME, mSharedPreferences.getString(KEY_FNAME, null));

        users.put(KEY_EMAIL, mSharedPreferences.getString(KEY_EMAIL, null));
        users.put(KEY_IMAGE, mSharedPreferences.getString(KEY_IMAGE, null));
        users.put(KEY_IMAGEPATH, mSharedPreferences.getString(KEY_IMAGEPATH, null));
        users.put(KEY_DESCRIPTION, mSharedPreferences.getString(KEY_DESCRIPTION, null));
        users.put(KEY_BIRTH_DATE, mSharedPreferences.getString(KEY_BIRTH_DATE, null));
        users.put(KEY_Address, mSharedPreferences.getString(KEY_Address, null));


        return users;
    }


    public void clearData() {
        mEditor.clear();
        mEditor.commit();
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
        mIntent = new Intent(mContext, LogIn.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(mIntent);
    }

    public void SavePhoto(String bitmap) {
        mEditor.putString(KEY_IMAGE, bitmap);
        mEditor.commit();
    }

    /**
     * Quick check for login
     **/
    public boolean isLoggedIn() {
        return mSharedPreferences.getBoolean(IS_LOGIN, false);
    }
}
