package cn.mandroid.express.UI.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.koushikdutta.ion.Ion;

import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.RongIMMessage.TaskInfoMessage;
import cn.mandroid.express.Model.RongIMMessage.TaskInfoNoNoticeMessage;
import cn.mandroid.express.Utils.PreferenceHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
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
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
//        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG).proxy("192.168.1.195", 8888);
        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG).proxy("10.1.51.53", 8888);
        rongImInit();
    }

    private void rongImInit() {
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIM.init(this);
            RongIM.registerMessageType(TaskInfoMessage.class);
            RongIM.registerMessageType(TaskInfoNoNoticeMessage.class);
        }
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public UserBean getUser() {
        return PreferenceHelper.instance(this).getUser();
    }

    public void saveUser(UserBean u) {
        PreferenceHelper.instance(this).saveUser(u);
    }
}
