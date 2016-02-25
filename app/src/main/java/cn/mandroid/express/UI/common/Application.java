package cn.mandroid.express.UI.common;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.EApplication;

import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.RongIMMessage.TaskInfoMessage;
import cn.mandroid.express.Model.RongIMMessage.TaskInfoNoNoticeMessage;
import cn.mandroid.express.Utils.PreferenceHelper;
import cn.smssdk.SMSSDK;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2015-12-12.
 */
@EApplication
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG).proxy("192.168.1.195", 8888);
        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG).proxy("10.1.51.53", 8888);
        SMSSDK.initSDK(this, "f806a43aa048", "79a51c451d4a6af077d13ed5eb104891");
        ActiveAndroid.initialize(this);
        rongImInit();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
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
