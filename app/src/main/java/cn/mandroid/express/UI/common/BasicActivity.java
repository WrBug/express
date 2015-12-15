package cn.mandroid.express.UI.common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.koushikdutta.ion.Ion;

import cn.mandroid.express.Event.ExitApp;
import cn.mandroid.express.UI.dialog.ProgressDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //renceHelper.instance(context);
        mPreference = Preference.instance(this);
        mPreferenceHelper = PreferenceHelper.instance(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    public void onEvent(ExitApp exit) {
        finish();
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
