package cn.mandroid.express.UI.common;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import cn.mandroid.express.Event.ChatEvent;
import cn.mandroid.express.Event.ExitApp;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.RongImManager;
import cn.mandroid.express.UI.activity.MainActivity;
import cn.mandroid.express.UI.dialog.ProgressDialog;
import cn.mandroid.express.Utils.CheckUtil;
import cn.mandroid.express.Utils.MLog;
import cn.mandroid.express.Utils.MToast;
import cn.mandroid.express.Utils.Preference;
import cn.mandroid.express.Utils.PreferenceHelper;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2015-11-22.
 */
public class BasicActivity extends FragmentActivity implements RongIMClient.ConnectionStatusListener {
    protected Context context;
    protected Preference mPreference;
    protected PreferenceHelper mPreferenceHelper;
    protected static RongIMClient.ConnectionStatusListener.ConnectionStatus rongIMstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //renceHelper.instance(context);
        mPreference = Preference.instance(this);
        mPreferenceHelper = PreferenceHelper.instance(this);
        EventBus.getDefault().register(this);
    }

    public void onEvent(ExitApp exit) {
        finish();
    }

    public void onEvent(ChatEvent event) {
        switch (event.action) {
            case CONNECT:
                initIm();
                break;
            case DISCONNECT:
                break;
        }
    }

    private void initIm() {
        RongIM.getInstance().getRongIMClient().setConnectionStatusListener(this);
        final UserBean user = mPreferenceHelper.getUser();
        if (CheckUtil.userIsInvid(user)) {
            RongImManager rongImManager = new RongImManager(context);
            rongImManager.connectIm(user, true);
        }
    }

    protected void exitApp() {
        EventBus.getDefault().post(new ExitApp());
    }

    protected void showToast(String content) {
        MToast.show(context, content);
    }

    protected void showProgressDialog() {
        ProgressDialog.instance(context).show();
    }

    protected void hideProgressDialog() {
        ProgressDialog.instance(context).dismiss();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        rongIMstatus = connectionStatus;
        if (rongIMstatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT) {
            if (!(context instanceof MainActivity)) {
                finish();
            }
            showToast("账号在别处登录,您已强制下线");
            RongIM.getInstance().logout();
        }
    }
}
