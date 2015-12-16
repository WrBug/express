package cn.mandroid.express.UI.common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.Bean;

import cn.mandroid.express.Event.ChatEvent;
import cn.mandroid.express.Event.ExitApp;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.RongImManager;
import cn.mandroid.express.UI.dialog.ProgressDialog;
import cn.mandroid.express.Utils.CheckUtil;
import cn.mandroid.express.Utils.MToast;
import cn.mandroid.express.Utils.Preference;
import cn.mandroid.express.Utils.PreferenceHelper;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015-11-22.
 */
public class BasicActivity extends FragmentActivity {
    protected Context context;
    protected Preference mPreference;
    protected PreferenceHelper mPreferenceHelper;
    protected boolean isConnectRongIm;
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
        final UserBean user = mPreferenceHelper.getUser();
        if (CheckUtil.userIsInvid(user)) {
            RongImManager rongImManager=new RongImManager(context);
            rongImManager.connectIm(user, true, new FetchCallBack() {
                @Override
                public void onSuccess(int status, int code, Object o) {
                    isConnectRongIm = true;
                }

                @Override
                public void onError() {
                }
            });
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
}
