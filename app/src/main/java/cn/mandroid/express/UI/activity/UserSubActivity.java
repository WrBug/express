package cn.mandroid.express.UI.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.R;
import cn.mandroid.express.UI.common.BasicActivity;
import cn.mandroid.express.UI.widget.ActionBar;

@EActivity(R.layout.activity_user_sub)
public class UserSubActivity extends BasicActivity {
    @Extra
    Action action;
    @ViewById
    ActionBar actionBar;
    @ViewById
    View container;

    @AfterViews
    void afterView() {
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
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public enum Action {
        INTEGRALDETAIL, HOWTOGETINTEGRAL
    }
}
