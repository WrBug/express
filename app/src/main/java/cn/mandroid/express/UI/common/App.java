package cn.mandroid.express.UI.common;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.EApplication;

import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.RongImManager;
import cn.mandroid.express.Utils.PreferenceHelper;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2015-12-12.
 */
public class App extends Application {
    public static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG).proxy("192.168.0.102",8888);
        RongIM.init(this);

    }
    public UserBean getUser() {
        return PreferenceHelper.instance(this).getUser();
    }

    public void saveUser(UserBean u) {
        PreferenceHelper.instance(this).saveUser(u);
    }
}
