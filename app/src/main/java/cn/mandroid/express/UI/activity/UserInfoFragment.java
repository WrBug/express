package cn.mandroid.express.UI.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.Constant;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.RongImManager;
import cn.mandroid.express.Model.UserManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.common.BasicFragment;
import cn.mandroid.express.UI.dialog.CropDialog;
import cn.mandroid.express.UI.dialog.CropDialog_;
import cn.mandroid.express.Utils.Const;
import cn.mandroid.express.Utils.DateUtil;
import cn.mandroid.express.Utils.FileUtils;
import cn.mandroid.express.Utils.PreferenceHelper;
import cn.mandroid.express.Utils.UiUtil;
import de.hdodenhof.circleimageview.CircleImageView;

@EFragment(R.layout.fragment_user_info)
public class UserInfoFragment extends BasicFragment {
    @Bean
    UserManager mUserManager;
    @Bean
    RongImManager mRongImManager;
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
    Bitmap photo;
    UserBean userBean;
    final int REQUEST_IMAGE = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterVew() {
        updateUI();
    }

    @Click(R.id.userIcoImg)
    void selectIco() {
        Intent intent = new Intent(getActivity(), MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Click(R.id.howToGetIntegralText)
    void howToGetIntegralText() {
        UserSubActivity_.intent(getActivity()).action(UserSubActivity.Action.HOWTOGETINTEGRAL).start();
    }

    @Click(R.id.userIntegralDetailText)
    void userIntegralDetailText() {
        UserSubActivity_.intent(getActivity()).action(UserSubActivity.Action.INTEGRALDETAIL).start();
    }

    @Click(R.id.userSignInText)
    void userSignInClick() {
        mUserManager.signIn(userBean.getUsername(), userBean.getSessionId(), new FetchCallBack<UserBean>() {
            @Override
            public void onSuccess(int status, int code, UserBean userBean) {
                if (status == 1) {
                    preferenceHelper.saveSignInfo(userBean.getSignInCount(), userBean.getSignInDate());
                    setSignInInfo(userBean);
                } else {
                    if (code == Constant.Code.SESSION_ERROR) {
                        showToast("身份已过期，请重新登录！");
                    } else if (code == Constant.Code.SING_IN_ERROR) {
                        showToast("签到失败");
                    }
                }
            }

            @Override
            public void onError() {
                showToast("网络连接失败,请稍后再试!");
            }
        });
    }

    @OnActivityResult(value = REQUEST_IMAGE)
    void onResult(Intent data) {
        if (data != null) {
            List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            if (path != null && path.size() != 0) {
                cropIco(path.get(0));
            }
        }
    }

    private void cropIco(String path) {
        CropDialog cropDialog = CropDialog_.builder().icoPath(path).build();
        cropDialog.setCropCallback(new CropDialog.CropCallback() {
            @Override
            public void onComplete(Bitmap bitmap) {
                File path = FileUtils.saveBitmapFile(getActivity(), bitmap, System.currentTimeMillis() + "");
                uploadAvatar(path);
            }
        });
        cropDialog.show(getFragmentManager(), "CropDialog");
    }

    private void uploadAvatar(File file) {
        showToast("正在上传");
        final UserBean userBean = preferenceHelper.getUser();
        mUserManager.uploadAvatar(userBean.getUsername(), userBean.getName(), userBean.getSessionId(), file, new FetchCallBack<String>() {
            @Override
            public void onSuccess(int status, int code, String s) {
                if (status == 1) {
                    showToast("上传成功！");
                    UiUtil.loadImage(getActivity(), userIcoImg, s);
                    mRongImManager.refreshUserInfoCache(userBean.getUsername(), userBean.getName(), s);
                } else {
                    if (code == Constant.Code.SESSION_ERROR) {
                        showToast("身份已过期，请重新登录！");
                    }
                }
            }

            @Override
            public void onError() {
                showToast("上传失败！");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUser();
    }

    private void updateUser() {
        userBean = preferenceHelper.getUser();
        mUserManager.updateUser(userBean.getUsername(), userBean.getSessionId(), new FetchCallBack<UserBean>() {
            @Override
            public void onSuccess(int status, int code, UserBean userBean) {
                if (status == 1) {
                    preferenceHelper.saveUser(userBean);
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
        UserBean userBean = preferenceHelper.getUser();
        userIcoImg.setImageResource(userBean.getSex() == 2 ? R.drawable.ic_user_default_woman : R.drawable.ic_user_default_man);
        if (!TextUtils.isEmpty(userBean.getAvatarUrl())) {
            UiUtil.loadImage(getActivity(), userIcoImg, userBean.getAvatarUrl());
        }
        userSexImg.setImageResource(userBean.getSex() == 2 ? R.drawable.ic_user_sex_female : R.drawable.ic_user_sex_male);
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
}
