package cn.mandroid.express.UI.common;

import android.app.Application;

import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.EApplication;

import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Utils.PreferenceHelper;

/**
 * Created by Administrator on 2015-12-12.
 */
public class App extends Application{
    public static App INSTANCE;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE=this;
    }
    public UserBean getUser(){
        return PreferenceHelper.instance(this).getUser();
    }
    public void saveUser(UserBean u){
        PreferenceHelper.instance(this).saveUser(u);
    }
}
