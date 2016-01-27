package cn.mandroid.express.UI.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.Event.ChatEvent;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.Constant;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.JwcManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.common.BasicActivity;
import cn.mandroid.express.UI.widget.ActionBar;
import cn.mandroid.express.Utils.MD5;
import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BasicActivity implements ActionBar.OnHeadImgClickListener {
    @ViewById
    ActionBar actionBar;
    @ViewById
    EditText usernameEdit;
    @ViewById
    EditText passwordEdit;
    @ViewById
    EditText nameEdit;
    @ViewById
    View checkInfoContainer;
    @ViewById
    View loginEditContainer;
    @ViewById
    View setPasswordContainer;
    @ViewById
    EditText idcardEdit;
    @ViewById
    EditText setPasswordEdit;
    @ViewById
    Button submit;
    @Bean
    JwcManager mJwcManager;
    String ticket;
    String username;
    String cookie;
    String name;
    String sex;
    String idcard;
    int showContainerNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterView() {
        usernameEdit.setText(mPreferenceHelper.getUsername());
        passwordEdit.setText(mPreferenceHelper.getPassword());
        setActionBar();
    }

    private void setActionBar() {
        actionBar.setTitle("登录");
        actionBar.setRigthImgVisible(View.GONE);
        actionBar.setOnHeadImgClickListener(this);
    }

    @Click(R.id.submit)
    void onSubmitClick() {
        switch (showContainerNum) {
            case 1:
                username = usernameEdit.getText().toString();
                doLogin();
                break;
            case 2:
                name = nameEdit.getText().toString();
                idcard = idcardEdit.getText().toString();
                if (TextUtils.isEmpty(cookie)) {
                    getCookie();
                } else {
                    checkInfo(cookie);
                }
                break;
            case 3:
                doRegister();
                break;
        }

    }

    private void doRegister() {
        String password = setPasswordEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            showToast("密码不能为空");
            return;
        }
        password = MD5.encode(password);
        showProgressDialog();
        mJwcManager.register(username, password, name, idcard, sex, new FetchCallBack<UserBean>() {
            @Override
            public void onSuccess(int code, UserBean userBean) {
                hideProgressDialog();
                mPreferenceHelper.saveUser(userBean);
                mPreferenceHelper.savePassword("");
                showToast("注册成功!");
                EventBus.getDefault().post(new ChatEvent(ChatEvent.Action.CONNECT));
                MainActivity_.intent(context).start();
                finish();

            }

            @Override
            public void onFail(int code, UserBean bean) {
                hideProgressDialog();
                showToast("注册失败,请稍后再试!");
            }

            @Override
            public void onError() {
                hideProgressDialog();
            }
        });
    }

    private void getCookie() {
        if (TextUtils.isEmpty(name)) {
            showToast("姓名输入有误");
            return;
        }
        if (TextUtils.isEmpty(idcard)) {
            showToast("身份证输入有误");
            return;
        }
        showProgressDialog();
        mJwcManager.getCookie(ticket, new FetchCallBack<String>() {
            @Override
            public void onSuccess(int code, String s) {
                hideProgressDialog();
                checkInfo(s);
            }

            @Override
            public void onFail(int code, String s) {
                hideProgressDialog();
            }

            @Override
            public void onError() {
                hideProgressDialog();
                showToast("网络连接失败,请稍后再试!");
            }
        });
    }

    private void checkInfo(final String s) {
        showProgressDialog();
        mJwcManager.checkInfo(username, name, idcard, s, new FetchCallBack<Integer>() {
            @Override
            public void onSuccess(int code, Integer sexNum) {
                hideProgressDialog();
                sex = sexNum + "";
                showToast("验证成功,请设置登录密码");
                cookie = s;
                showContainerNum = 3;
                showContainerView();
            }

            @Override
            public void onFail(int code, Integer integer) {
                hideProgressDialog();
                if (code == Constant.Code.JWC_NAME_ERROR) {
                    showToast("姓名有误,请重新输入");
                    cookie = s;
                } else if (code == Constant.Code.JWC_IDCARD_ERROR) {
                    showToast("身份证有误,请重新输入");
                    cookie = s;
                } else if (code == Constant.Code.JWC_COOKIE_ERROR) {
                    showToast("验证失败,请再试一次");
                    cookie = null;
                    showContainerNum = 1;
                    showContainerView();
                }
            }

            @Override
            public void onError() {
                hideProgressDialog();
            }
        });
    }

    private void showContainerView() {
        loginEditContainer.setVisibility(showContainerNum == 1 ? View.VISIBLE : View.GONE);
        checkInfoContainer.setVisibility(showContainerNum == 2 ? View.VISIBLE : View.GONE);
        setPasswordContainer.setVisibility(showContainerNum == 3 ? View.VISIBLE : View.GONE);
        switch (showContainerNum) {
            case 1:
                submit.setText("登录");
                actionBar.setTitle("登录");
                break;
            case 2:
                submit.setText("验证");
                actionBar.setTitle("验证");
                break;
            case 3:
                submit.setText("提交");
                actionBar.setTitle("注册");
                break;
        }
    }

    private void doLogin() {
        if (TextUtils.isEmpty(username)) {
            showToast("学号不得为空");
            return;
        }
        String password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            showToast("密码不得为空");
            return;
        }
        final String pswd = password.length() == 32 ? password : MD5.encode(password);
        showProgressDialog();
        mJwcManager.login(username, pswd, new FetchCallBack<JsonObject>() {
            @Override
            public void onSuccess(int code, JsonObject jsonObject) {
                hideProgressDialog();
                Gson gson = new Gson();
                UserBean userBean = gson.fromJson(jsonObject.get("data"), UserBean.class);
                mPreferenceHelper.saveUser(userBean);
                showToast("登录成功");
                EventBus.getDefault().post(new ChatEvent(ChatEvent.Action.CONNECT));
                MainActivity_.intent(context).start();
                finish();
            }

            @Override
            public void onFail(int code, JsonObject jsonObject) {
                hideProgressDialog();
                if (code == Constant.Code.JWC_PSWD_ERROR) {
                    showToast("教务处密码有误");
                } else if (code == Constant.Code.NO_USER) {
                    mPreferenceHelper.saveUsername(username);
                    mPreferenceHelper.savePassword(pswd);
                    ticket = jsonObject.get("data").getAsString();
                    showToast("请填写正确的姓名和身份证");
                    showContainerNum = 2;
                    showContainerView();
                } else if (code == Constant.Code.PASSWORD_ERROR) {
                    showToast("登录密码有误,请重新输入");
                }
            }

            @Override
            public void onError() {
                hideProgressDialog();
                showToast("网络连接失败,请稍后再试!");
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        checkStep();
    }

    private void checkStep() {
        switch (showContainerNum) {
            case 1:
                finish();
                break;
            case 2:
                showContainerNum = 1;
                showContainerView();
                break;
            case 3:
                showContainerNum = 2;
                showContainerView();
                break;
        }
    }

    @Override
    public void leftImgClick(ImageView view) {
        checkStep();
    }

    @Override
    public void rightImgClick(ImageView view) {

    }
}
