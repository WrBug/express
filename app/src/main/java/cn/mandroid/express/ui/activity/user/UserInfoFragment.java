package cn.mandroid.express.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.List;

import cn.mandroid.express.model.bean.UserBean;
import cn.mandroid.express.model.Constant;
import cn.mandroid.express.model.FetchCallBack;
import cn.mandroid.express.model.RongImManager;
import cn.mandroid.express.model.UserManager;
import cn.mandroid.express.R;
import cn.mandroid.express.ui.activity.MultiImageSelectorActivity;
import cn.mandroid.express.ui.activity.MultiImageSelectorActivity_;
import cn.mandroid.express.ui.common.BasicHomeFragment;
import cn.mandroid.express.ui.dialog.ProgressDialog;
import cn.mandroid.express.utils.Const;
import cn.mandroid.express.utils.DateUtil;
import cn.mandroid.express.utils.UiUtil;
import cn.pedant.sweetalert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

@EFragment(R.layout.fragment_user_info)
public class UserInfoFragment extends BasicHomeFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bean
    UserManager mUserManager;
    @Bean
    RongImManager mRongImManager;
    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;
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
    TextView userSignInText;
    @ViewById
    TextView userSignInCountText;
    @ViewById
    TextView userIntegralText;
    @ViewById
    TextView userReleaseCountText;
    @ViewById
    TextView userReceiveCountText;
    UserBean userBean;
    public static final int CROP_IMAGE = 2;
    final int REQUEST_IMAGE = 0x99;
    private ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterVew() {
        swipeRefreshLayout.setOnRefreshListener(this);
        updateUser();
        updateUI();
    }

    @Click(R.id.userIcoImg)
    void selectIco() {
        MultiImageSelectorActivity_.intent(this).isShow(true).defaultCount(1).mode(MultiImageSelectorActivity.MODE_SINGLE).startForResult(REQUEST_IMAGE);
    }

    @Click(R.id.logoutBut)
    void logoutClick() {
        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("是否注销登陆？").setConfirmText("注销").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                mPreferenceHelper.cleanUser();
                mainActivity.setCenterFragment();
                sweetAlertDialog.dismiss();
            }
        }).show();
    }

    @Click(R.id.howToGetIntegralText)
    void howToGetIntegralText() {
        subActivityStart(UserSubActivity.Action.HOWTOGETINTEGRAL);
    }

    @Click(R.id.userIntegralDetailText)
    void userIntegralDetailText() {
        subActivityStart(UserSubActivity.Action.INTEGRALDETAIL);
    }

    @Click(R.id.userReleaseCountText)
    void userReleaseDetailClick() {
        subActivityStart(UserSubActivity.Action.RELEASETASK);
    }

    @Click(R.id.userReceiveCountText)
    void userReceiveDetailClick() {
        subActivityStart(UserSubActivity.Action.RECEIVETASK);
    }

    @Click(R.id.userSignInText)
    void userSignInClick() {
        progressDialog = new ProgressDialog(getActivity(), "请稍后");
        progressDialog.show();
        mUserManager.signIn(userBean.getUsername(), new FetchCallBack<UserBean>() {
            @Override
            public void onSuccess(int code, UserBean userBean) {
                progressDialog.dismiss();
                new SweetAlertDialog(getActivity()).setTitleText("签到成功").changeAlertType(SweetAlertDialog.SUCCESS_TYPE).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
                mPreferenceHelper.saveSignInfo(userBean.getSignInCount(), userBean.getSignInDate(), userBean.getIntegral());
                setSignInInfo(userBean);
                userIntegralText.setText(userBean.getIntegral() + "");
            }

            @Override
            public boolean onFail(int code, UserBean bean) {
                progressDialog.dismiss();
                if (code == Constant.Code.SING_IN_ERROR) {
                    new SweetAlertDialog(getActivity()).setTitleText("签到失败").changeAlertType(SweetAlertDialog.ERROR_TYPE).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
                }
                return false;
            }

            @Override
            public boolean onError() {
                progressDialog.dismiss();
                return true;
            }
        });
    }


    private void subActivityStart(UserSubActivity.Action action) {
        UserSubActivity_.intent(getActivity()).action(action).start();
    }

    @OnActivityResult(value = CROP_IMAGE)
    void onCropResult(int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                File file = (File) data.getSerializableExtra("file");
                if (file != null) {
                    uploadAvatar(file);
                }
            }
        }
    }

    @OnActivityResult(REQUEST_IMAGE)
    void onResult(Intent data) {
        if (data != null) {
            List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            if (path != null && path.size() != 0) {
                cropIco(path.get(0));
            }
        }
    }

    private void cropIco(String path) {
        CropImageActivity_.intent(this).imagePath(path).startForResult(CROP_IMAGE);
    }

    private void uploadAvatar(File file) {
        showToast("正在上传");
        final UserBean userBean = mPreferenceHelper.getUser();
        mUserManager.uploadAvatar(userBean.getUsername(), userBean.getName(), file, new FetchCallBack<String>() {
            @Override
            public void onSuccess(int code, String s) {
                showToast("上传成功！");
                UiUtil.loadImage(getActivity(), userIcoImg, s);
                mRongImManager.refreshUserInfoCache(userBean);
            }

            @Override
            public boolean onFail(int code, String s) {
                return false;
            }

            @Override
            public boolean onError() {
                showToast("上传失败！");
                return false;
            }
        });
    }

    private void updateUser() {
        userBean = mPreferenceHelper.getUser();
        mUserManager.updateUser(userBean.getUsername(), new FetchCallBack<UserBean>() {
            @Override
            public void onSuccess(int code, UserBean userBean) {
                swipeRefreshLayout.setRefreshing(false);
                mPreferenceHelper.saveUser(userBean);
                mRongImManager.refreshUserInfoCache(userBean);
                updateUI();
            }

            @Override
            public boolean onFail(int code, UserBean bean) {
                swipeRefreshLayout.setRefreshing(false);
                mainActivity.setCenterFragment();
                return false;
            }

            @Override
            public boolean onError() {
                swipeRefreshLayout.setRefreshing(false);
                return true;
            }
        });
    }

    private void updateUI() {
        UserBean userBean = mPreferenceHelper.getUser();
        userIcoImg.setImageResource(userBean.isMan() ? R.drawable.ic_user_default_man : R.drawable.ic_user_default_woman);
        if (!TextUtils.isEmpty(userBean.getAvatarUrl())) {
            UiUtil.loadImage(getActivity(), userIcoImg, userBean.getAvatarUrl());
        }
        userSexImg.setImageResource(userBean.isMan() ? R.drawable.ic_user_sex_male : R.drawable.ic_user_sex_female);
        userNameText.setText(userBean.getName());
        int level = getLevel(userBean);
        userLevelText.setText(Const.LEVEL[level]);
        if (level < 3) {
            userLevelUpText.setText("还差" + (Const.LEVEL_UP[level] - userBean.getIntegral()) + "分升级到" + Const.LEVEL[level + 1] + "!");
            userLevelUpProgress.setMax(Const.LEVEL_UP[level]);
            userLevelUpProgress.setProgress(Const.LEVEL_UP[level] - userBean.getIntegral());
        } else {
            userLevelUpText.setText("帮助达人,生活有你更精彩!");
            userLevelUpProgress.setMax(1);
            userLevelUpProgress.setProgress(1);
        }
        setSignInInfo(userBean);
        userIntegralText.setText("" + userBean.getIntegral());
        userReleaseCountText.setText("我发布了" + userBean.getReleaseCount() + "条消息");
        userReceiveCountText.setText("我帮忙拿了" + userBean.getReceiveCount() + "次快递");
    }

    private void setSignInInfo(UserBean userBean) {
        if (DateUtil.getTimesmorning() > userBean.getSignInDate()) {
            userSignInCountText.setText("今天还未签到");
            userSignInText.setText("签到");
            userSignInText.setClickable(true);
        } else {
            userSignInCountText.setText("已连续签到" + userBean.getSignInCount() + "天");
            userSignInText.setText("已签到");
            userSignInText.setClickable(false);
        }
    }

    private int getLevel(UserBean userBean) {
        for (int i = 1; i < 4; i++) {
            if (Const.LEVEL_UP[i] > userBean.getIntegral()) {
                return i;
            }
        }
        return 3;
    }

    @Override
    public void onRefresh() {
        updateUser();
    }
}
