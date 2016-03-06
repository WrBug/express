package cn.mandroid.express.UI.common;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.androidannotations.annotations.EFragment;

import cn.mandroid.express.UI.activity.MainActivity;

/**
 * Created by WT on 2016-03-06.
 */
@EFragment
public class BasicHomeFragment extends BasicFragment {
    protected MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }
}
