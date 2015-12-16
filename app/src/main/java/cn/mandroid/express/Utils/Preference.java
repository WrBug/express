package cn.mandroid.express.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单数据缓存工具类
 *
 * @author Administrator
 */
public class Preference {

    public static String USER_VERIFIED = "userVerified";
    private final String SHARED_PREFERENCE_NAME = "express";
    private static Preference catche;
    // private static Preference catcheWithName;
    private static Map<String, Preference> Premap = new HashMap<String, Preference>();
    private SharedPreferences spf;
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String NAME = "name";
    public static String INTEGRAL = "integral";
    public static String RELEASE_COUNT = "releaseCount";
    public static String RECEIVE_COUNT = "receiveCount";
    public static String SESSION_ID = "sessionId";
    public static String SIGN_IN_COUNT = "signInCount";
    public static String USER_SEX = "sex";
    public static String AVATAR_URL = "avatarUrl";
    public static String TOKEN = "token";

    public static Preference instance(Context context) {
        if (catche == null) {
            catche = new Preference(context);
        }
        return catche;
    }

    public static Preference instance(Context context, String name) {
        if (!Premap.containsKey(name)) {
            Premap.put(name, new Preference(context, name));
        }
        return Premap.get(name);
    }

    public Preference(Context context) {
        spf = context.getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

    public Preference(Context context, String name) {
        spf = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, boolean value) {
        if(!value){
            return;
        }
        spf.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return spf.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        if(value==null){
            return;
        }
        spf.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return spf.getString(key, "");
    }

    public void putInt(String key, int value) {
        if(value==0){
            return;
        }
        spf.edit().putInt(key, value).commit();
    }

    public void putLong(String key, long value) {
        if(value==0){
            return;
        }
        spf.edit().putLong(key, value).commit();
    }

    public int getInt(String key) {
        return spf.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return spf.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return spf.getLong(key, 0);
    }

    public long getLong(String key, long def) {
        return spf.getLong(key, def);
    }

    public void clearData() {
        spf.edit().clear().commit();
    }

    public void remove(String key) {
        spf.edit().remove(key).commit();
    }

    public void commit() {
        spf.edit().commit();
    }

}
