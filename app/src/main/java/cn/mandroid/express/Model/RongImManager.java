package cn.mandroid.express.Model;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.RongIMListener.UnreadCountChangedListener;
import cn.mandroid.express.Model.RongIMMessage.TaskInfoMessageItemProvider;
import cn.mandroid.express.Model.RongIMMessage.TaskInfoNoNoticeMessageItemProvider;
import cn.mandroid.express.R;
import cn.mandroid.express.Utils.FileUtils;
import cn.mandroid.express.Utils.MLog;
import cn.mandroid.express.Utils.PreferenceHelper;
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

    public RongImManager(Context context) {
        this.context = context;
    }

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
                UserBean userBean = DaoManager.getUserInfoByUsername(s);
                if (userBean != null) {
                    Uri uri;
                    if (TextUtils.isEmpty(userBean.getAvatarUrl())) {
                        uri = userBean.getSex() == 1 ? FileUtils.getDefalutWomanIco(context) : FileUtils.getDefalutManIco(context);
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
            uri = FileUtils.res2Uri(context, userBean.getSex() == 1 ? R.drawable.ic_user_default_woman : R.drawable.ic_user_default_man);
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
                PreferenceHelper.instance(context).saveUser(userBean);
                connectIm(PreferenceHelper.instance(context).getUser(), false);
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

    public void refreshUserInfoCache(String username, String nickname, @Nullable String avatarUrl) {
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(username, nickname, Uri.parse(avatarUrl)));
    }
}
