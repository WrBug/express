package cn.mandroid.express.UI.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import cn.mandroid.express.UI.dialog.ProgressDialog;
import cn.mandroid.express.Utils.MToast;
import cn.mandroid.express.Utils.PreferenceHelper;
import cn.mandroid.express.Utils.UiUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/12/15.
 */
public class BasicFragment extends Fragment {
    protected PreferenceHelper preferenceHelper;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UiUtil.hideKeyboard(getActivity());
        preferenceHelper = PreferenceHelper.instance(getActivity());
    }

    protected void showToast(String msg) {
        MToast.show(getActivity(), msg);
    }

    protected void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity(), "加载中");
        }
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
