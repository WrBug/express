package cn.mandroid.express.UI.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.Constant;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.UserManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.common.App;
import cn.mandroid.express.Utils.Const;
import cn.mandroid.express.Utils.UiUtil;
import de.hdodenhof.circleimageview.CircleImageView;

@EFragment(R.layout.fragment_user_info)
public class UserInfoFragment extends Fragment {
    @Bean
    UserManager mUserManager;
    UserBean userBean;
    App mApp;
    @ViewById
    CircleImageView userIcoImg;
    @ViewById
    ImageView userSexImg;
    @ViewById
    TextView userNameText;
    @ViewById
    TextView userLevelText;
    @ViewById
    TextView userLevelUpText;
    @ViewById
    ProgressBar userLevelUpProgress;
    @ViewById
    TextView userSignInCountText;
    @ViewById
    TextView userIntegralText;
    @ViewById
    TextView userReleaseCountText;
    @ViewById
    TextView userReceiveCountText;

    public void onCreate(Bundle savedInstanceState) {
        mApp = App.INSTANCE;
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterVew() {
        userBean = mApp.getUser();
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUser();
    }

    private void updateUser() {
        mUserManager.updateUser(userBean.getUsername(), userBean.getSessionId(), new FetchCallBack<UserBean>() {
            @Override
            public void onSuccess(int status, int code, UserBean userBean) {
                if (status == 1) {
                    mApp.saveUser(userBean);
                    UserInfoFragment.this.userBean = userBean;
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
        userIcoImg.setImageResource(userBean.getSex() == 2 ? R.drawable.ic_user_default_woman : R.drawable.ic_user_default_man);
        if (!TextUtils.isEmpty(userBean.getAvatarUrl())) {
            UiUtil.loadImage(getActivity(), userIcoImg, userBean.getAvatarUrl());
        }
        userSexImg.setImageResource(userBean.getSex() == 2 ? R.drawable.ic_user_sex_female : R.drawable.ic_user_sex_male);
        userNameText.setText(userBean.getName());
        int level=getLevel();
        userLevelText.setText(Const.LEVEL[level]);
        if(level<3){
            userLevelUpText.setText("还差" + (Const.LEVEL_UP[level] - userBean.getIntegral()) + "分升级到" + Const.LEVEL[level+ 1] + "!");
            userLevelUpProgress.setMax(Const.LEVEL_UP[level]);
            userLevelUpProgress.setProgress(Const.LEVEL_UP[level] - userBean.getIntegral());
        }else{
            userLevelUpText.setText("帮助达人,生活有你更精彩!");
            userLevelUpProgress.setMax(1);
            userLevelUpProgress.setProgress(1);
        }

        userSignInCountText.setText("已连续签到" + userBean.getSignInCount() + "天");
        userIntegralText.setText("" + userBean.getIntegral());
        userReleaseCountText.setText("我发布了" + userBean.getReleaseCount() + "条消息");
        userReceiveCountText.setText("我帮忙拿了" + userBean.getReceiveCount() + "次快递");
    }

    private int getLevel() {
        for(int i=1;i<4;i++){
            if(Const.LEVEL_UP[i]>userBean.getIntegral()){
                return i;
            }
        }
        return  4;
    }
}
