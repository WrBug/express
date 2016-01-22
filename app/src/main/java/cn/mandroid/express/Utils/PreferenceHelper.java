package cn.mandroid.express.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.mandroid.express.Model.Bean.UserBean;

/**
 * Created by Administrator on 2015-11-15.
 */
public class PreferenceHelper {
    Context context;
    Preference preference;
    private static PreferenceHelper helper;
    private static UserBean mUserBean;

    private PreferenceHelper(Context context) {
        this.context = context;
        preference = Preference.instance(context);
    }

    public static PreferenceHelper instance(Context context) {
        if (helper == null) {
            helper = new PreferenceHelper(context);
        }
        return helper;
    }

    public void saveUsername(String username) {
        preference.putString(Preference.USERNAME, username);
    }

    public String getUsername() {
        return preference.getString(Preference.USERNAME);
    }

    public void savePassword(String password) {
        preference.putString(Preference.PASSWORD, password);
    }

    public void saveSignInfo(int signInCount, long signInDate) {
        preference.putInt(Preference.SIGN_IN_COUNT, signInCount);
        preference.putLong(Preference.SIGN_IN_DATE, signInDate);
    }
    public void saveIntegral(int integral) {
        preference.putInt(Preference.INTEGRAL, integral);
    }
    public String getPassword() {
        return preference.getString(Preference.PASSWORD);
    }

    public void cleanCookie() {
        SharedPreferences preference = context.getSharedPreferences("ion-cookies", Context.MODE_PRIVATE);
        preference.edit().remove("http://jxgl.hdu.edu.cn").apply();
    }

    public void saveUser(UserBean userBean) {
        mUserBean = userBean;
        preference.putString(Preference.USERNAME, userBean.getUsername());
        preference.putString(Preference.NAME, userBean.getName());
        preference.putInt(Preference.INTEGRAL, userBean.getIntegral());
        preference.putInt(Preference.RELEASE_COUNT, userBean.getReleaseCount());
        preference.putInt(Preference.RECEIVE_COUNT, userBean.getReceiveCount());
        preference.putString(Preference.SESSION_ID, userBean.getSessionId());
        preference.putInt(Preference.SIGN_IN_COUNT, userBean.getSignInCount());
        preference.putLong(Preference.SIGN_IN_DATE, userBean.getSignInDate());
        preference.putInt(Preference.USER_SEX, userBean.getSex());
        preference.putString(Preference.AVATAR_URL, userBean.getAvatarUrl());
        preference.putString(Preference.TOKEN, userBean.getToken());
    }

    private UserBean updataUser() {
        mUserBean = new UserBean();
        mUserBean.setIntegral(preference.getInt(Preference.INTEGRAL));
        mUserBean.setUsername(preference.getString(Preference.USERNAME));
        mUserBean.setName(preference.getString(Preference.NAME));
        mUserBean.setReceiveCount(preference.getInt(Preference.RECEIVE_COUNT));
        mUserBean.setReleaseCount(preference.getInt(Preference.RELEASE_COUNT));
        mUserBean.setSessionId(preference.getString(Preference.SESSION_ID));
        mUserBean.setSignInCount(preference.getInt(Preference.SIGN_IN_COUNT));
        mUserBean.setSex(preference.getInt(Preference.USER_SEX));
        mUserBean.setAvatarUrl(preference.getString(Preference.AVATAR_URL));
        mUserBean.setToken(preference.getString(Preference.TOKEN));
        return mUserBean;
    }

    public UserBean getUser() {
        if (mUserBean == null) {
            return updataUser();
        }
        return mUserBean;
    }
}
