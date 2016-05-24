package cn.mandroid.express.UI1.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import cn.mandroid.express.UI1.dialog.ProgressDialog;
import cn.mandroid.express.Utils1.MToast;
import cn.mandroid.express.Model1.sPrefs.PreferenceHelper;

/**
 * Created by Administrator on 2015/12/15.
 */
@EFragment
public class BasicFragment extends Fragment {
    @Bean
    protected PreferenceHelper mPreferenceHelper;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UiUtil.hideKeyboard(getActivity());
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
