package com.example.xinyue.helloworld.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "yyappartment_saveInfo";
    private static SharedPreferences mSharedPreferences;
    private static PreferenceUtils mPreferenceUtils;
    private static SharedPreferences.Editor editor;
    private String SHARED_KEY_PASSWORD = "password";
    private String SHARED_KEY_USERNAME = "username";
    private String SHARED_KEY_ISSAVED = "login_info_saved_state";
    private String SHARD_KEY_TOKEN = "token";
    private String SHARD_KEY_UID = "uid";
    private String SHARD_KEY_AVATARURL = "avatar_url";
    private String SHARD_KEY_FIRSTNAME = "fistname";
    private String SHARD_KEY_ROLE = "role";

    private PreferenceUtils(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt) {
        if (mPreferenceUtils == null) {
            mPreferenceUtils = new PreferenceUtils(cxt);
        }
    }

    /**
     * 单例模式，获取instance实例
     *
     * @param cxt
     * @return
     */
    public static PreferenceUtils getInstance() {
        if (mPreferenceUtils == null) {
            throw new RuntimeException("please init first!");
        }

        return mPreferenceUtils;
    }

    public void setPassword(String password) {
        editor.putString(SHARED_KEY_PASSWORD, password);
        editor.commit();
    }

    public String getPassword() {
        return mSharedPreferences.getString(SHARED_KEY_PASSWORD, "");
    }

    public void setUsername(String username) {
        editor.putString(SHARED_KEY_USERNAME, username);
        editor.commit();
    }

    public String getUsername() {
        return mSharedPreferences.getString(SHARED_KEY_USERNAME, "");
    }

    public void setUID(String uid) {
        editor.putString(SHARD_KEY_UID, uid);
        editor.commit();
    }

    public String getUID() {
        return mSharedPreferences.getString(SHARD_KEY_UID, "");
    }

    public void setInfoSavedFlag(boolean flag) {
        editor.putBoolean(SHARED_KEY_ISSAVED, flag);
        editor.commit();
    }

    public boolean getInfoSavedFlag() {
        return mSharedPreferences.getBoolean(SHARED_KEY_ISSAVED, false);
    }

    public void setToken(String token) {
        editor.putString(SHARD_KEY_TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return mSharedPreferences.getString(SHARD_KEY_TOKEN, "");
    }

    public void setAvatarUrl(String url) {
        editor.putString(SHARD_KEY_AVATARURL, url);
        editor.commit();
    }

    public String getAvatarUrl() {
        return mSharedPreferences.getString(SHARD_KEY_AVATARURL, "");
    }

    public void setFirstName(String name) {
        editor.putString(SHARD_KEY_FIRSTNAME, name);
        editor.commit();
    }

    public String getFirstName() {
        return mSharedPreferences.getString(SHARD_KEY_FIRSTNAME, "");
    }

    public void setRole(String role) {
        editor.putString(SHARD_KEY_ROLE, role);
        editor.commit();
    }

    public String getRole() {
        return mSharedPreferences.getString(SHARD_KEY_FIRSTNAME, "");
    }
}
