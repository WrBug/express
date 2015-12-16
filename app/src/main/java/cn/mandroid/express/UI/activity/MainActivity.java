package cn.mandroid.express.UI.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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
import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
public class MainActivity extends BasicActivity implements ActionBar.OnHeadImgClickListenner, RadioGroup.OnCheckedChangeListener {
    @Bean
    JwcManager jwcManager;
    @ViewById
    ActionBar actionBar;
    @ViewById
    View fragmentContainer;
    @ViewById
    RadioGroup tabMenu;
    long exitTime;
    @Bean
    UserManager userManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterView() {
        setActionBar();
        tabMenu.setOnCheckedChangeListener(this);
        EventBus.getDefault().post(new ChatEvent(ChatEvent.Action.CONNECT));
    }


    private void setActionBar() {
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setLeftImgVisible(View.GONE);
        actionBar.setOnHeadImgClickListenner(this);

    }

    private void setFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    @Override
    public void leftImgClick(ImageView view) {

    }

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
                    LoginActivity_.intent(context).start();
                    return;
                }
                if (isConnectRongIm) {
                    ChatFragment fragment = ChatFragment_.builder().build();
                    setFragment(fragment);
                } else {
                    EventBus.getDefault().post(new ChatEvent(ChatEvent.Action.CONNECT));
                }
                break;
            case R.id.rbCenter:
                break;
            case R.id.rbMy:
                if (!CheckUtil.userIsInvid(mPreferenceHelper.getUser())) {
                    LoginActivity_.intent(context).start();
                    return;
                }
                UserInfoFragment fragment = UserInfoFragment_.builder().build();
                setFragment(fragment);
                break;
        }
    }

}
