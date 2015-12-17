package cn.mandroid.express.Model;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import org.androidannotations.annotations.EBean;

import cn.mandroid.express.Event.AcountStatusEvent;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.R;
import cn.mandroid.express.Utils.FileUtils;
import cn.mandroid.express.Utils.MLog;
import cn.mandroid.express.Utils.PreferenceHelper;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

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
                if (tryAgan) {
                    getToken(userBean, mCallBack);
                }
            }

            @Override
            public void onSuccess(String s) {
                mCallBack.onSuccess(1, 1, null);
                setUserinfo(userBean);
                getUserInfo();
                RongIM.getInstance().getRongIMClient().setConnectionStatusListener(rongImConnectListener);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MLog.i("error" + errorCode.getMessage());
            }
        });
    }

    private void getUserInfo() {
//        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//            @Override
//            public UserInfo getUserInfo(String s) {
//                File file = new File(FileUtils.getSdcardPath() + "/1.png");
//                Uri uri = Uri.fromFile(file);
//                UserInfo userInfo = new UserInfo("12946137", "test", uri);
//                return userInfo;
//            }
//        }, true);
    }

    public void setUserinfo(UserBean userBean) {
        Uri uri;
        if (TextUtils.isEmpty(userBean.getAvatarUrl())) {
            uri = FileUtils.res2Uri(context, userBean.getSex() == 2 ? R.drawable.ic_user_default_woman : R.drawable.ic_user_default_man);
        } else {
            uri = Uri.parse(userBean.getAvatarUrl());
        }
        UserInfo userInfo = new UserInfo(userBean.getUsername(), userBean.getName(), uri);
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        RongIM.getInstance().setCurrentUserInfo(userInfo);
    }

    private void getToken(final UserBean userBean, final FetchCallBack mCallBack) {
        UserManager userManager = new UserManager(context);
        userManager.getToken(userBean.getUsername(), userBean.getSessionId(), new FetchCallBack<String>() {
            @Override
            public void onSuccess(int status, int code, String s) {
                if (status == 1) {
                    userBean.setToken(s);
                    PreferenceHelper.instance(context).saveUser(userBean);
                    connectIm(PreferenceHelper.instance(context).getUser(), false, mCallBack);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private RongIMClient.ConnectionStatusListener rongImConnectListener = new RongIMClient.ConnectionStatusListener() {

        @Override
        public void onChanged(ConnectionStatus status) {
            AcountStatusEvent event=new AcountStatusEvent();
            event.setStatus(status);
            EventBus.getDefault().post(event);
        }
    };
}
