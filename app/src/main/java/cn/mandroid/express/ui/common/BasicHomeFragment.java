package cn.mandroid.express.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.androidannotations.annotations.EFragment;

import cn.mandroid.express.ui.activity.MainActivity;

/**
 * Created by WT on 2016-03-06.
 */
@EFragment
public class BasicHomeFragment extends BasicFragment {
    private MainActivity parentActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
    }

    protected void setActionBar(CharSequence charSequence) {
        parentActivity.setActionBarTitle(charSequence.toString());
    }

    protected void setFragment(Fragment fragment) {
        parentActivity.setFragment(fragment);
    }

    protected void setCenterFragment() {
        parentActivity.setCenterFragment();
    }
}
