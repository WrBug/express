package cn.mandroid.express.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2015/12/16.
 */
public class BaseDialogFragment extends DialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragment);
    }

    public void show(FragmentManager manager) {
        show(manager, this.getClass().getName());
    }
}
