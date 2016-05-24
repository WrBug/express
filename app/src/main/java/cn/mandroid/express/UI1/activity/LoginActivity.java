package cn.mandroid.express.UI1.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import org.json.JSONException;

import cn.mandroid.express.Events.ChatEvent;
import cn.mandroid.express.Model1.Bean1.UserBean;
import cn.mandroid.express.Model1.Constant;
import cn.mandroid.express.Model1.FetchCallBack;
import cn.mandroid.express.Model1.JwcManager;
import cn.mandroid.express.Model1.UserManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI1.common.BasicActivity;
import cn.mandroid.express.UI1.widget.ActionBar;
import cn.mandroid.express.Utils1.CheckUtil;
import cn.mandroid.express.Utils1.MD5;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
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
    EditText setPhoneEdit;
    @ViewById
    EditText verfyEdit;
    @ViewById
    Button getVerifyBut;
    @ViewById
    View verifyContainer;
    @ViewById
    EditText setPasswordEdit;
    @ViewById
    Button submit;
    @Bean
    JwcManager mJwcManager;
    @Bean
    UserManager mUserManager;
    String ticket;
    String username;
    String cookie;
    String name;
    String sex;
    String idcard;
    int showContainerNum = 1;
    EventHandler eventHandler;
    String phone;
    boolean isVerification;
    private long waitTime;
    private final int UPDATE_BTN = 0x0020;

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

    @Click(R.id.getVerifyBut)
    void getVerifyButClick() {
        isVerification = false;
        phone = setPhoneEdit.getText().toString();
        if (!CheckUtil.isMobileNumber(phone)) {
            showToast("手机号码有误");
            return;
        }
        showProgressDialog("请稍后");
        checkPhone();
    }

    private void checkPhone() {
        mUserManager.checkPhone(phone, new FetchCallBack() {
            @Override
            public void onSuccess(int code, Object o) {
                hideProgressDialog();
                getVerifyBut.setClickable(false);
                eventHandler = new EventHandler() {
                    @Override
                    public void afterEvent(int event, int result, Object data) {
                        Message message = new Message();
                        message.what = result;
                        message.arg1 = event;
                        message.obj = data;
                        handler.sendMessage(message);
                    }
                };
                SMSSDK.registerEventHandler(eventHandler);
                SMSSDK.getVerificationCode("86", phone);
            }

            @Override
            public boolean onFail(int code, Object o) {
                hideProgressDialog();
                return false;
            }

            @Override
            public boolean onError() {
                hideProgressDialog();

                return true;
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int result = msg.what;
            int event = msg.arg1;
            Object data = msg.obj;
            if (result == UPDATE_BTN) {
                long time = waitTime - System.currentTimeMillis() / 1000;
                if (time > 0) {
                    getVerifyBut.setText(time + "秒后重新获取");
                    handler.sendEmptyMessageDelayed(UPDATE_BTN, 1000);
                } else {
                    getVerifyBut.setClickable(true);
                    getVerifyBut.setText("获取验证码");
                    setPhoneEdit.setEnabled(true);
                }
                return;
            }
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    isVerification = true;
                    showToast("验证成功");
                    setPhoneEdit.setEnabled(false);
                    //提交验证码成功
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (data instanceof Boolean) {
                        if (((Boolean) data).booleanValue()) {
                            isVerification = true;
                            showToast("验证成功");
                            setPhoneEdit.setEnabled(false);
                            getVerifyBut.setText("验证成功");
                        } else {
                            showToast("验证码已发送到您手机，请注意查收");
                            waitTime = System.currentTimeMillis() / 1000 + 60;
                            handler.sendEmptyMessage(UPDATE_BTN);
                            setPhoneEdit.setEnabled(false);
                            verifyContainer.setVisibility(View.VISIBLE);
                        }
                    }
                    //获取验证码成功
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                Throwable throwable = (Throwable) data;
                try {
                    showSmsErrorToast(throwable);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            hideProgressDialog();
            super.handleMessage(msg);
        }
    };

    @Click(R.id.submitVerifyBut)
    void checkVerify() {
        showProgressDialog("正在验证");
        String verify = verfyEdit.getText().toString();
        SMSSDK.submitVerificationCode("86", phone, verify);
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
        if (!isVerification) {
            showToast("请先验证手机号");
            return;
        }
        String password = setPasswordEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            showToast("密码不能为空");
            return;
        }
        password = MD5.encode(password);
        showProgressDialog();
        mJwcManager.register(username, password, name, phone, idcard, sex, new FetchCallBack<UserBean>() {
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
            public boolean onFail(int code, UserBean bean) {
                hideProgressDialog();
                return false;
            }

            @Override
            public boolean onError() {
                hideProgressDialog();
                return true;
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
            public boolean onFail(int code, String s) {
                hideProgressDialog();
                return true;
            }

            @Override
            public boolean onError() {
                hideProgressDialog();
                showToast("网络连接失败,请稍后再试!");
                return false;
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
            public boolean onFail(int code, Integer integer) {
                hideProgressDialog();
                if (code == Constant.Code.JWC_NAME_ERROR) {
                    cookie = s;
                } else if (code == Constant.Code.JWC_IDCARD_ERROR) {
                    cookie = s;
                } else if (code == Constant.Code.JWC_COOKIE_ERROR) {
                    cookie = null;
                    showContainerNum = 1;
                    showContainerView();
                }
                return false;
            }

            @Override
            public boolean onError() {
                hideProgressDialog();
                return true;
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

    @Override
    protected void onDestroy() {
        if (eventHandler != null) {
            SMSSDK.unregisterEventHandler(eventHandler);
        }
        super.onDestroy();
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
            public boolean onFail(int code, JsonObject jsonObject) {
                hideProgressDialog();
                if (code == Constant.Code.NO_USER) {
                    mPreferenceHelper.saveUsername(username);
                    mPreferenceHelper.savePassword(pswd);
                    ticket = jsonObject.get("data").getAsString();
                    showContainerNum = 2;
                    showContainerView();
                }
                return false;
            }

            @Override
            public boolean onError() {
                hideProgressDialog();
                showToast("网络连接失败,请稍后再试!");
                return false;
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
