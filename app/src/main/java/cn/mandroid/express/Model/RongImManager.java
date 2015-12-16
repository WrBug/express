package cn.mandroid.express.Model;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Utils.MLog;
import cn.mandroid.express.Utils.PreferenceHelper;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2015/12/16.
 */
@EBean
public class RongImManager {
    private Context context;
    public RongImManager(Context context) {
        this.context = context;
    }

    public void connectIm(final UserBean userBean, final boolean tryAgan, final FetchCallBack mCallBack) {
        RongIM.connect(userBean.getToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                if(tryAgan){
                    getToken(userBean, mCallBack);
                }
            }

            @Override
            public void onSuccess(String s) {
                mCallBack.onSuccess(1, 1, null);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MLog.i("error" + errorCode.getMessage());
            }
        });
    }

    private void getToken(final UserBean userBean, final FetchCallBack mCallBack) {
        UserManager userManager=new UserManager(context);
        userManager.getToken(userBean.getUsername(), userBean.getSessionId(), new FetchCallBack<String>() {
            @Override
            public void onSuccess(int status, int code, String s) {
                if (status == 1) {
                    userBean.setToken(s);
                    PreferenceHelper.instance(context).saveUser(userBean);
                    connectIm(PreferenceHelper.instance(context).getUser(),false, mCallBack);
                }
            }
            @Override
            public void onError() {

            }
        });
    }
}
