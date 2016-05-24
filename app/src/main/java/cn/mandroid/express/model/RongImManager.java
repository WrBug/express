package cn.mandroid.express.model;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import cn.mandroid.express.model.bean.UserBean;
import cn.mandroid.express.model.rongIMListener.UnreadCountChangedListener;
import cn.mandroid.express.model.rongIMMessage.TaskInfoMessageItemProvider;
import cn.mandroid.express.model.rongIMMessage.TaskInfoNoNoticeMessageItemProvider;
import cn.mandroid.express.utils.FileUtils;
import cn.mandroid.express.utils.MLog;
import cn.mandroid.express.model.sPrefs.PreferenceHelper;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2015/12/16.
 */
@EBean
public class RongImManager {
    private Context context;
    @Bean
    PreferenceHelper preferenceHelper;
    public void connectIm(final UserBean userBean, final boolean tryAgan) {
        RongIM.connect(userBean.getToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                MLog.i("incor");
                if (tryAgan) {
                    getToken(userBean);
                }
            }

            @Override
            public void onSuccess(String s) {
                Conversation.ConversationType[] types = new Conversation.ConversationType[7];
                types[0] = Conversation.ConversationType.PRIVATE;
                types[1] = Conversation.ConversationType.APP_PUBLIC_SERVICE;
                types[2] = Conversation.ConversationType.CHATROOM;
                types[3] = Conversation.ConversationType.CUSTOMER_SERVICE;
                types[4] = Conversation.ConversationType.DISCUSSION;
                types[5] = Conversation.ConversationType.GROUP;
                types[6] = Conversation.ConversationType.PUSH_SERVICE;
                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new UnreadCountChangedListener(), types);
                RongIM.getInstance().registerMessageTemplate(new TaskInfoMessageItemProvider());
                RongIM.getInstance().registerMessageTemplate(new TaskInfoNoNoticeMessageItemProvider());
                setUserinfo(userBean);
                getUserInfo(userBean);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MLog.i("error" + errorCode.getMessage());
            }
        });
    }

    private void getUserInfo(final UserBean myInfo) {
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                UserBean userBean = DaoManager.getLocalUserInfo(s);
                if (userBean != null) {
                    Uri uri;
                    if (TextUtils.isEmpty(userBean.getAvatarUrl())) {
                        uri = userBean.isMan() ? FileUtils.getDefalutManIco(context) : FileUtils.getDefalutWomanIco(context);
                    } else {
                        uri = Uri.parse(userBean.getAvatarUrl());
                    }
                    UserInfo userInfo = new UserInfo(userBean.getUsername(), userBean.getName(), uri);
                    return userInfo;
                }

                new UserManager(context).getUserInfoByUsername(s, myInfo.getUsername(), new FetchCallBack<UserBean>() {
                    @Override
                    public void onSuccess(int code, UserBean userBean) {
                        if (userBean != null && !TextUtils.isEmpty(userBean.getUsername())) {
                            DaoManager.saveUserInfo(userBean.getUsername(), userBean.getSex(), userBean.getName(), userBean.getAvatarUrl());
                        }
                    }

                    @Override
                    public boolean onFail(int code, UserBean bean) {
                        return true;
                    }

                    @Override
                    public boolean onError() {
                        return false;
                    }
                });
                return null;
            }
        }, false);
    }

    public void setUserinfo(UserBean userBean) {
        Uri uri;
        if (TextUtils.isEmpty(userBean.getAvatarUrl())) {
            uri = userBean.isMan() ? FileUtils.getDefalutManIco(context) : FileUtils.getDefalutWomanIco(context);
        } else {
            uri = Uri.parse(userBean.getAvatarUrl());
        }
        UserInfo userInfo = new UserInfo(userBean.getUsername(), userBean.getName(), uri);
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        RongIM.getInstance().setCurrentUserInfo(userInfo);
    }

    private void getToken(final UserBean userBean) {
        UserManager userManager = new UserManager(context);
        userManager.getToken(userBean.getUsername(), new FetchCallBack<String>() {
            @Override
            public void onSuccess(int code, String s) {
                userBean.setToken(s);
                preferenceHelper.saveUser(userBean);
                connectIm(preferenceHelper.getUser(), false);
            }

            @Override
            public boolean onFail(int code, String s) {
                return true;
            }

            @Override
            public boolean onError() {
                return false;
            }
        });
    }

    public void refreshUserInfoCache(UserBean userBean) {
        Uri uri;
        if (TextUtils.isEmpty(userBean.getAvatarUrl())) {
            uri = userBean.isMan() ? FileUtils.getDefalutManIco(context) : FileUtils.getDefalutWomanIco(context);
        } else {
            uri = Uri.parse(userBean.getAvatarUrl());
        }
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(userBean.getUsername(), userBean.getName(), uri));
    }
}
