package cn.mandroid.express.ui.activity.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.R;
import cn.mandroid.express.ui.common.BasicActivity;
import cn.mandroid.express.ui.widget.ActionBar;

@EActivity(R.layout.activity_user_sub)
public class UserSubActivity extends BasicActivity implements ActionBar.OnHeadImgClickListener {
    @Extra
    Action action;
    @ViewById
    ActionBar actionBar;
    @ViewById
    View container;

    @AfterViews
    void afterView() {
        actionBar.setOnHeadImgClickListener(this);
        actionBar.setRigthImgVisible(View.GONE);
        initView();
    }

    private void initView() {
        switch (action) {
            case INTEGRALDETAIL:
                actionBar.setVisibility(View.GONE);
                setFragment(UserIntegralDetailFragment_.builder().build());
                break;
            case HOWTOGETINTEGRAL:
                actionBar.setVisibility(View.VISIBLE);
                actionBar.setTitle("获取积分方式");
//                setFragment(UserIntegralDetailFragment_.builder().build());
                break;
            case RELEASETASK:
                actionBar.setVisibility(View.VISIBLE);
                actionBar.setTitle("我发布的信息");
                setFragment(UserTaskDetailFragment_.builder().username(mPreferenceHelper.getUsername()).action(Action.RELEASETASK).build());
                break;
            case RECEIVETASK:
                actionBar.setVisibility(View.VISIBLE);
                actionBar.setTitle("我领取的任务");
                setFragment(UserTaskDetailFragment_.builder().username(mPreferenceHelper.getUsername()).action(Action.RECEIVETASK).build());
                break;
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public void leftImgClick(ImageView view) {
        finish();
    }

    @Override
    public void rightImgClick(ImageView view) {

    }

    public enum Action {
        INTEGRALDETAIL, HOWTOGETINTEGRAL, RELEASETASK, RECEIVETASK
    }
}
