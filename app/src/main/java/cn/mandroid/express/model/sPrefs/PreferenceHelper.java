package cn.mandroid.express.model.sPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.mandroid.express.model.bean.FilterBean;
import cn.mandroid.express.model.bean.UserBean;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2015-11-15.
 */
@EBean
public class PreferenceHelper {
    @RootContext
    Context context;
    private static UserBean mUserBean;
    @Pref
    UserPrefs_ userPrefs;
    @Pref
    FilterPrefs_ filterPrefs;

    public boolean isFirstOpen() {
        return Preference.instance(context).getBoolean(Preference.IS_FIRST_RUN);
    }

    public void setRun(boolean run) {
        Preference.instance(context).putBoolean(Preference.IS_FIRST_RUN, run);
    }

    public void saveUsername(String username) {
        userPrefs.edit().username().put(username).apply();
    }

    public String getUsername() {
        return userPrefs.username().get();
    }

    public String getSessionId() {
        return userPrefs.sessionId().get();
    }

    public void savePassword(String password) {
        userPrefs.edit().password().put(password).apply();
    }

    public void saveSignInfo(int signInCount, long signInDate, int integral) {
        userPrefs.edit().signInCount().put(signInCount).signInDate().put(signInDate).integral().put(integral).apply();
        updataUser();

    }

    public String getPassword() {
        return userPrefs.password().get();
    }

    public void cleanCookie() {
        SharedPreferences preference = context.getSharedPreferences("ion-cookies", Context.MODE_PRIVATE);
        preference.edit().remove("http://jxgl.hdu.edu.cn").apply();
    }

    public void saveUser(UserBean userBean) {
        mUserBean = userBean;
        userPrefs.edit()
                .username().put(userBean.getUsername())
                .name().put(userBean.getName())
                .integral().put(userBean.getIntegral())
                .releaseCount().put(userBean.getReleaseCount())
                .receiveCount().put(userBean.getReceiveCount())
                .sessionId().put(userBean.getSessionId())
                .signInCount().put(userBean.getSignInCount())
                .signInDate().put(userBean.getSignInDate())
                .sex().put(userBean.getSex())
                .avatarUrl().put(userBean.getAvatarUrl())
                .token().put(userBean.getToken())
                .apply();
    }

    public void cleanUser() {
        userPrefs.edit().clear().apply();
        mUserBean = null;
        RongIM.getInstance().disconnect();
    }

    private UserBean updataUser() {
        mUserBean = new UserBean();
        mUserBean.setIntegral(userPrefs.integral().get());
        mUserBean.setUsername(userPrefs.username().get());
        mUserBean.setName(userPrefs.name().get());
        mUserBean.setReceiveCount(userPrefs.receiveCount().get());
        mUserBean.setReleaseCount(userPrefs.releaseCount().get());
        mUserBean.setSessionId(userPrefs.sessionId().get());
        mUserBean.setSignInCount(userPrefs.signInCount().get());
        mUserBean.setSex(userPrefs.sex().get());
        mUserBean.setAvatarUrl(userPrefs.avatarUrl().get());
        mUserBean.setToken(userPrefs.token().get());
        return mUserBean;
    }

    public void saveFilter(FilterBean bean) {
        filterPrefs.edit()
                .depo().put(bean.getDepo())
                .dest().put(bean.getDest())
                .finish().put(bean.isFinish())
                .complete().put(bean.isComplete())
                .pennding().put(bean.isPennding())
                .running().put(bean.isRunning())
                .apply();
    }

    public FilterBean getFilter() {
        FilterBean bean = new FilterBean();
        bean.setPennding(filterPrefs.pennding().get());
        bean.setRunning(filterPrefs.running().get());
        bean.setFinish(filterPrefs.finish().get());
        bean.setComplete(filterPrefs.complete().get());
        bean.setDepo(filterPrefs.depo().get());
        bean.setDest(filterPrefs.dest().get());
        return bean;
    }

    public UserBean getUser() {
        if (mUserBean == null) {
            return updataUser();
        }
        return mUserBean;
    }
}
