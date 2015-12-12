package cn.mandroid.express.UI.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.Constant;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.UserManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.common.App;
import cn.mandroid.express.Utils.MLog;
import cn.mandroid.express.Utils.PreferenceHelper;

@EFragment
public class UserInfoFragment extends Fragment {
    @Bean
    UserManager mUserManager;
    UserBean userBean;
    App mApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mApp = App.INSTANCE;
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterVew() {
        updateUser();
    }

    private void updateUser() {
        mUserManager.updateUser(userBean.getUsername(), userBean.getSessionId(), new FetchCallBack<UserBean>() {
            @Override
            public void onSuccess(int status, int code, UserBean userBean) {
                if (status == 1) {
                    mApp.saveUser(userBean);
                    updateUI();
                } else {
                    if (code == Constant.Code.SESSION_ERROR) {
                        LoginActivity_.intent(getActivity()).start();
                    }
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void updateUI() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userBean = mApp.getUser();
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

}
