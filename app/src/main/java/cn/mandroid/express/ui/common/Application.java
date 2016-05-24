package cn.mandroid.express.ui.common;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

import cn.mandroid.express.model.bean.UserBean;
import cn.mandroid.express.model.Constant;
import cn.mandroid.express.model.rongIMMessage.TaskInfoMessage;
import cn.mandroid.express.model.rongIMMessage.TaskInfoNoNoticeMessage;
import cn.mandroid.express.model.sPrefs.PreferenceHelper;
import cn.smssdk.SMSSDK;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2015-12-12.
 */
@EApplication
public class Application extends android.app.Application {
    @Bean
    PreferenceHelper mPreferenceHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG).proxy(Constant.HOST_IP, 8888);
        SMSSDK.initSDK(this, "f806a43aa048", "79a51c451d4a6af077d13ed5eb104891");
        ActiveAndroid.initialize(this);
        rongImInit();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
    //融云初始化
    private void rongImInit() {
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIM.init(this);
            //自定义消息
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
        return mPreferenceHelper.getUser();
    }

    public void saveUser(UserBean u) {
        mPreferenceHelper.saveUser(u);
    }
}
