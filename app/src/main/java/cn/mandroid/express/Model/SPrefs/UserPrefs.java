package cn.mandroid.express.Model.SPrefs;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Administrator on 2016/2/26 0026.
 */
@SharedPref(SharedPref.Scope.UNIQUE)
public interface UserPrefs {
     String username();
     String password();
     String name();
     String phone();
     int integral();
     int releaseCount();
     int receiveCount();
     String sessionId();
     int signInCount();
     long signInDate();
     int sex();
     String avatarUrl();
     String token();
}
