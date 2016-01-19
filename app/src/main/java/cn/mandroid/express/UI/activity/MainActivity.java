package cn.mandroid.express.UI.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.mandroid.express.Event.ChatEvent;
import cn.mandroid.express.Event.ExitApp;
import cn.mandroid.express.Model.JwcManager;
import cn.mandroid.express.Model.UserManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.activity.rongIM.ChatFragment;
import cn.mandroid.express.UI.activity.rongIM.ChatFragment_;
import cn.mandroid.express.UI.common.BasicActivity;
import cn.mandroid.express.UI.widget.ActionBar;
import cn.mandroid.express.Utils.CheckUtil;
import cn.mandroid.express.Utils.MLog;
import de.greenrobot.event.EventBus;
import io.rong.imlib.RongIMClient;

@EActivity(R.layout.activity_main)
public class MainActivity extends BasicActivity implements ActionBar.OnHeadImgClickListenner, RadioGroup.OnCheckedChangeListener {
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
    ChatFragment chatFragment;
    UserInfoFragment userInfoFragment;
    Fragment cacheFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterView() {
        setActionBar();
        tabMenu.setOnCheckedChangeListener(this);
        EventBus.getDefault().post(new ChatEvent(ChatEvent.Action.CONNECT));
        lastCheckedRb = rbCenter;
        rbCenter.setChecked(true);
        initFragment();
        setFragment(centerFragment);
    }

    private void initFragment() {
        centerFragment = CenterFragment_.builder().build();
        chatFragment = ChatFragment_.builder().build();
        userInfoFragment = UserInfoFragment_.builder().build();
    }


    private void setActionBar() {
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setLeftImgVisible(View.GONE);
        actionBar.setOnHeadImgClickListenner(this);

    }

    private void setFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (cacheFragment != null) {
            transaction.hide(cacheFragment);
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.fragmentContainer, fragment);
        }
//        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
        cacheFragment = fragment;
    }

//    private void setFragment(int index) {
//        Fragment fragment = (Fragment) mFragmentPagerAdapter.instantiateItem(
//                fragmentContainer, index);
//        mFragmentPagerAdapter.setPrimaryItem(fragmentContainer, index, fragment);
//        mFragmentPagerAdapter.finishUpdate(fragmentContainer);
//    }

    @Override
    public void leftImgClick(ImageView view) {

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
                    setFragment(chatFragment);
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
//                setFragment(checkedId);
                break;
            }
            case R.id.rbMy:
                if (!CheckUtil.userIsInvid(mPreferenceHelper.getUser())) {
                    LoginActivity_.intent(context).start();
                    return;
                }
                lastCheckedRb = rbMy;
                setFragment(userInfoFragment);
                break;
        }
    }
}
