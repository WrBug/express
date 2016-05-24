package cn.mandroid.express.UI1.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.mandroid.express.Events.ChatEvent;
import cn.mandroid.express.Events.ExitApp;
import cn.mandroid.express.Events.UnreadEvent;
import cn.mandroid.express.Model1.JwcManager;
import cn.mandroid.express.Model1.UserManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI1.activity.rongIM.BasicChatFragment;
import cn.mandroid.express.UI1.activity.rongIM.ChatFragment;
import cn.mandroid.express.UI1.activity.rongIM.FriendsFragment;
import cn.mandroid.express.UI1.activity.user.UserInfoFragment;
import cn.mandroid.express.UI1.activity.user.UserInfoFragment_;
import cn.mandroid.express.UI1.common.BasicActivity;
import cn.mandroid.express.UI1.widget.ActionBar;
import cn.mandroid.express.Utils1.CheckUtil;
import de.greenrobot.event.EventBus;
import io.rong.imlib.RongIMClient;

@EActivity(R.layout.activity_main)
public class MainActivity extends BasicActivity implements ActionBar.OnHeadImgClickListener, RadioGroup.OnCheckedChangeListener {
    @Bean
    JwcManager jwcManager;
    @ViewById
    ActionBar actionBar;
    @ViewById
    FrameLayout fragmentContainer;
    @ViewById
    RadioGroup tabMenu;
    @ViewById
    RadioButton rbCenter;
    @ViewById
    RadioButton rbChat;
    @ViewById
    RadioButton rbMy;
    RadioButton lastCheckedRb;
    long exitTime;
    @Bean
    UserManager userManager;
    List<Fragment> fragments;
    CenterFragment centerFragment;
    UserInfoFragment userInfoFragment;
    Fragment cacheFragment;
    public static boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRunning = true;
    }

    @Override
    protected void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }

    @AfterViews
    void afterView() {
        setActionBar();
        tabMenu.setOnCheckedChangeListener(this);
        EventBus.getDefault().post(new ChatEvent(ChatEvent.Action.CONNECT));
        lastCheckedRb = rbCenter;
        rbCenter.setChecked(true);
        initFragment();
        lastCheckedRb = rbCenter;
        setFragment(centerFragment);
    }

    private void initFragment() {
        centerFragment = CenterFragment_.builder().build();
        userInfoFragment = UserInfoFragment_.builder().build();
    }

    public void setCenterFragment() {
        rbCenter.setChecked(true);
    }

    private void setActionBar() {
        actionBar.setTitle("发现");
        actionBar.setLeftImgVisible(View.GONE);
        actionBar.setRigthImgVisible(View.GONE);
        actionBar.setOnHeadImgClickListener(this);

    }

    public void setActionBarTitle(String title) {
        actionBar.setTitle(title);
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (cacheFragment != null) {
            transaction.hide(cacheFragment);
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.fragmentContainer, fragment, fragment.getClass().getName());
        }
//        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
        cacheFragment = fragment;
    }

    @Override
    public void leftImgClick(ImageView view) {

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

    public void onEvent(UnreadEvent event) {
        super.onEvent(event);
        if (event.getCount() > 0) {
            Drawable drawable = getResources().getDrawable(R.drawable.tab_selector_contact_new_msg);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            rbChat.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.tab_selector_contact);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            rbChat.setCompoundDrawables(null, drawable, null, null);
        }
    }

    //    public void onEvent(AcountStatusEvent event) {
//        super.onEvent(event);
//        if (event.getStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT) {
//            rbCenter.setChecked(true);
//            CenterFragment fragment = CenterFragment_.builder().build();
//            setFragment(fragment);
//            LoginActivity_.intent(context).start();
//        }
//    }
    @Override
    public void rightImgClick(ImageView view) {
        LoginActivity_.intent(context).start();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            showToast("再按一次退出");
            exitTime = System.currentTimeMillis();
        } else {
            EventBus.getDefault().post(new ExitApp());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbChat:
                if (!CheckUtil.userIsInvid(mPreferenceHelper.getUser())) {
                    lastCheckedRb.setChecked(true);
                    LoginActivity_.intent(context).start();
                    return;
                }
                if (rongIMstatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {
                    lastCheckedRb = rbChat;
                    Fragment fragment = BasicChatFragment.lastChatFragment;
                    if (fragment instanceof ChatFragment) {
                        actionBar.setTitle("最近联系人");

                    } else if (fragment instanceof FriendsFragment) {
                        actionBar.setTitle("好友");
                    }
                    setFragment(BasicChatFragment.lastChatFragment);
                } else {
                    if (rongIMstatus != RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING) {
                        EventBus.getDefault().post(new ChatEvent(ChatEvent.Action.CONNECT));
                        showToast("正在登录,请稍后");
                    } else {
                        showToast("正在努力加载中,请稍后");
                    }
                    lastCheckedRb.setChecked(true);
                }
                break;
            case R.id.rbCenter: {
                lastCheckedRb = rbCenter;
                setFragment(centerFragment);
                actionBar.setTitle("发现");
                break;
            }
            case R.id.rbMy:
                if (!CheckUtil.userIsInvid(mPreferenceHelper.getUser())) {
                    LoginActivity_.intent(context).start();
                    lastCheckedRb.setChecked(true);
                    return;
                }
                lastCheckedRb = rbMy;
                setFragment(UserInfoFragment_.builder().build());
                actionBar.setTitle("个人中心");
                break;
        }
    }
}
