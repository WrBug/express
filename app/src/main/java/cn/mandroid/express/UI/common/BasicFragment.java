package cn.mandroid.express.UI.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import cn.mandroid.express.Utils.MToast;
import cn.mandroid.express.Utils.PreferenceHelper;

/**
 * Created by Administrator on 2015/12/15.
 */
public class BasicFragment extends Fragment {
    protected PreferenceHelper preferenceHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceHelper = PreferenceHelper.instance(getActivity());
    }

    protected void showToast(String msg) {
        MToast.show(getActivity(), msg);
    }
}
