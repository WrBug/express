package cn.mandroid.express.UI1.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.mandroid.express.Events.ChatEvent;
import cn.mandroid.express.Events.ExitApp;
import cn.mandroid.express.Events.RefreshEvent;
import cn.mandroid.express.Events.UnreadEvent;
import cn.mandroid.express.Model1.Bean1.UserBean;
import cn.mandroid.express.Model1.RongIMListener1.ReceiveMeassageListener;
import cn.mandroid.express.Model1.RongImManager;
import cn.mandroid.express.Model1.RongImManager_;
import cn.mandroid.express.Model1.sPrefs.PreferenceHelper_;
import cn.mandroid.express.UI1.activity.MainActivity;
import cn.mandroid.express.UI1.dialog.ProgressDialog;
import cn.mandroid.express.Utils1.CheckUtil;
import cn.mandroid.express.Utils1.MLog;
import cn.mandroid.express.Utils1.MToast;
import cn.mandroid.express.Model1.sPrefs.Preference;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by Administrator on 2015-11-22.
 */
public class BasicActivity extends FragmentActivity implements RongIMClient.ConnectionStatusListener {
    protected Context context;
    protected Preference mPreference;
    protected PreferenceHelper_ mPreferenceHelper;
    protected static RongIMClient.ConnectionStatusListener.ConnectionStatus rongIMstatus;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //renceHelper.instance(context);
        mPreference = Preference.instance(this);
        mPreferenceHelper = PreferenceHelper_.getInstance_(context);
        EventBus.getDefault().register(this);
    }

    public void onEvent(UnreadEvent event) {
    }

    public void onEvent(ExitApp exit) {
        finish();
    }

    public void onEvent(Message message) {
        MLog.i(message.getExtra());
    }

    public void onEvent(ChatEvent event) {
        switch (event.action) {
            case CONNECT:
                doImLogin();
                break;
            case DISCONNECT:
                break;
        }
    }

    public void onEvent(RefreshEvent event) {
        MLog.i("a");
    }

    // 融云登录
    private void doImLogin() {
        //        登录状态监听
        RongIM.getInstance().getRongIMClient().setConnectionStatusListener(this);
        //        消息监听
        RongIM.setOnReceiveMessageListener(new ReceiveMeassageListener());
        final UserBean user = mPreferenceHelper.getUser();
        if (CheckUtil.userIsInvid(user)) {
            RongImManager rongImManager = RongImManager_.getInstance_(context);
            //            登录操作
            rongImManager.connectIm(user, true);
        }
    }

    protected void exitApp() {
        EventBus.getDefault().post(new ExitApp());
    }

    public void showToast(String content) {
        MToast.show(context, content);
    }

    public void showSmsErrorToast(Throwable throwable) throws JSONException {
        JSONObject jsonObject = new JSONObject(throwable.getMessage());
        MToast.show(context, jsonObject.getString("detail"));
    }

    protected void showProgressDialog() {
        showProgressDialog("加载中");
    }

    protected void showProgressDialog(String title) {
        progressDialog = new ProgressDialog(context, title);
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
