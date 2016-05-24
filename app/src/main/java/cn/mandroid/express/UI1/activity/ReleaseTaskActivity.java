package cn.mandroid.express.UI1.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;

import cn.mandroid.express.Events.RefreshEvent;
import cn.mandroid.express.Model1.Bean1.TaskDetailBean;
import cn.mandroid.express.Model1.Constant;
import cn.mandroid.express.Model1.DaoManager;
import cn.mandroid.express.Model1.FetchCallBack;
import cn.mandroid.express.Model1.TaskManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI1.common.BasicActivity;
import cn.mandroid.express.UI1.widget.ActionBar;
import cn.mandroid.express.UI1.widget.EditSpinner;
import cn.mandroid.express.Utils1.Base64;
import cn.mandroid.express.Utils1.Const;
import cn.pedant.sweetalert.SweetAlertDialog;

@EActivity(R.layout.activity_release_task)
public class ReleaseTaskActivity extends BasicActivity implements ActionBar.OnHeadImgClickListener {
    @ViewById
    ActionBar actionBar;
    @ViewById(R.id.expressCompanySpinner)
    EditSpinner expressCompanySpinner;
    @ViewById
    EditText courierNumberEdit;
    @ViewById
    EditText contactorEdit;
    @ViewById
    EditText phoneNumberEdit;
    @ViewById
    CheckBox heavyCheck;
    @ViewById
    CheckBox bigCheck;
    @ViewById
    EditSpinner depositorySpinner;
    @ViewById
    EditText depositoryDetailEdit;
    @ViewById
    EditSpinner destinationSpinner;
    @ViewById
    EditText expressPasswordEdit;
    @ViewById
    EditText remarkEdit;
    @ViewById
    Button submit;
    @Bean
    TaskManager mTaskManager;
    @Extra
    boolean isEdit;
    @Extra
    TaskDetailBean taskDetailBean;
    String expressCompany;
    String cacheEC;
    String courinerNumber;
    String depositoryDetail;
    String contactor;
    String phoneNumber;
    int heavy;
    int big;
    String depository;
    String cacheDP;
    String destination;
    String cacheDT;
    String expressPassword;
    String remark;

    @AfterViews
    void afterView() {
        setActionBar();
        setSpinner();
        if (isEdit) {
            setData();
        }
    }

    private void setData() {
        expressCompanySpinner.setText(taskDetailBean.getExpressCompany());
        courierNumberEdit.setText(taskDetailBean.getCourinerNumber());
        contactorEdit.setText(taskDetailBean.getContactor());
        phoneNumberEdit.setText(taskDetailBean.getPhoneNumber());
        heavyCheck.setChecked(taskDetailBean.getHeavy() == 1);
        bigCheck.setChecked(taskDetailBean.getBig() == 1);
        depositorySpinner.setText(taskDetailBean.getDepository());
        depositoryDetailEdit.setText(taskDetailBean.getDepositoryDetail());
        destinationSpinner.setText(taskDetailBean.getDestination());
        expressPasswordEdit.setText(taskDetailBean.getExpressPassword());
        remarkEdit.setText(taskDetailBean.getRemark());
    }

    @Click(R.id.submit)
    void submitClick() {
        saveData();
        if (checkData()) {
            TaskDetailBean bean = new TaskDetailBean();
            if (isEdit) {
                bean = taskDetailBean;
            }
            bean.setExpressCompany(expressCompany);
            bean.setCourinerNumber(courinerNumber);
            bean.setContactor(contactor);
            bean.setPhoneNumber(phoneNumber);
            bean.setHeavy(heavy);
            bean.setBig(big);
            bean.setDepository(depository);
            bean.setDepositoryDetail(depositoryDetail);
            bean.setDestination(destination);
            bean.setExpressPassword(expressPassword);
            bean.setRemark(remark);
            bean.setDate(System.currentTimeMillis() / 1000);
            bean.setReceiveUser(null);
            showProgressDialog("正在提交");
            mTaskManager.releaseTask(bean, new FetchCallBack<Integer>() {
                @Override
                public void onSuccess(int code, Integer integer) {
                    hideProgressDialog();
                    new SweetAlertDialog(context).changeAlertType(SweetAlertDialog.SUCCESS_TYPE).setContentText(isEdit ? "修改成功" : "发布成功").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent intent = new Intent();
                            intent.putExtra("action", new RefreshEvent(isEdit ? RefreshEvent.Action.REFRESHTASK : RefreshEvent.Action.REFRESHTASKLIST));
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }).show();
                }

                @Override
                public boolean onFail(int code, Integer integer) {
                    hideProgressDialog();
                    if (code == Constant.Code.TASK_IS_DELETE) {
                        DaoManager.deleteTaskFormList(taskDetailBean.getId());
                        Intent intent = new Intent();
                        intent.putExtra("action", new RefreshEvent(RefreshEvent.Action.DELETETASK));
                        setResult(RESULT_OK, intent);
                        finish();
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
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(expressCompany)) {
            showToast("请选择/填写快递公司");
            return false;
        }
        if (TextUtils.isEmpty(courinerNumber)) {
            showToast("请填写快递单号");
            return false;
        }
        if (TextUtils.isEmpty(contactor)) {
            showToast("请填写联系人");
            return false;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast("请填写联系号码");
            return false;
        }
        if (TextUtils.isEmpty(depository)) {
            showToast("请选择/填写快递存放地");
            return false;
        }
        if (TextUtils.isEmpty(destination)) {
            showToast("请选择/填写送达地点");
            return false;
        }
        return true;
    }

    private void saveData() {
        expressCompany = expressCompanySpinner.getEditString();
        courinerNumber = courierNumberEdit.getText().toString();
        contactor = contactorEdit.getText().toString();
        phoneNumber = phoneNumberEdit.getText().toString();
        heavy = heavyCheck.isChecked() ? 1 : 0;
        big = bigCheck.isChecked() ? 1 : 0;
        depository = depositorySpinner.getEditString();
        depositoryDetail = depositoryDetailEdit.getText().toString();
        destination = destinationSpinner.getEditString();
        expressPassword = expressPasswordEdit.getText().toString();
        remark = Base64.encode(remarkEdit.getText().toString());
    }

    private void setSpinner() {
        expressCompanySpinner.setItemData(Arrays.asList(Const.EXPRESS_COMPANY));
        depositorySpinner.setItemData(Arrays.asList(Const.DEPOSITORY));
        destinationSpinner.setItemData(Arrays.asList(Const.DESTINATION));

    }

    private void setActionBar() {
        actionBar.setTitle(isEdit ? "修改" : "发布");
        actionBar.setRigthImgVisible(View.GONE);
        actionBar.setOnHeadImgClickListener(this);
    }

    @Override
    public void leftImgClick(ImageView view) {
        finish();
    }

    @Override
    public void rightImgClick(ImageView view) {

    }
}
