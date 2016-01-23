package cn.mandroid.express.UI.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.Model.Constant;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.common.BasicActivity;
import cn.mandroid.express.UI.widget.ActionBar;
import cn.mandroid.express.Utils.Const;
import cn.mandroid.express.Utils.UiUtil;

@EActivity(R.layout.activity_release_task)
public class ReleaseTaskActivity extends BasicActivity implements ActionBar.OnHeadImgClickListenner {
    @ViewById
    ActionBar actionBar;
    @ViewById(R.id.expressCompanySpinner)
    Spinner expressCompanySpinner;
    @ViewById(R.id.expressCompanyEdit)
    EditText expressCompanyEdit;
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
    Spinner depositorySpinner;
    @ViewById
    EditText depositoryEdit;
    @ViewById
    Spinner destinationSpinner;
    @ViewById
    EditText destinationEdit;
    @ViewById
    EditText expressPasswordEdit;
    @ViewById
    EditText remarkEdit;
    @ViewById
    Button submit;
    String expressCompany;
    String cacheEC;
    String courinerNumber;
    String contactor;
    String phoneNumber;
    int heavy;
    int big;
    String deposition;
    String cacheDP;
    String destination;
    String cacheDT;
    String expressPassword;
    String remark;

    @AfterViews
    void afterView() {
        setActionBar();
        setSpinner();
    }

    @ItemSelect(R.id.expressCompanySpinner)
    void expressCompanySelect(boolean selected, int position) {
        if (position == Const.EXPRESS_COMPANY.length - 1) {
            expressCompanyEdit.setVisibility(View.VISIBLE);
        } else {
            expressCompanyEdit.setVisibility(View.GONE);
            cacheEC = Const.EXPRESS_COMPANY[position];
        }
    }

    @ItemSelect(R.id.destinationSpinner)
    void destinationSelect(boolean selected, int position) {
        if (position == Const.DESTINATION.length - 1) {
            destinationEdit.setVisibility(View.VISIBLE);
        } else {
            destinationEdit.setVisibility(View.GONE);
            cacheDT = Const.DESTINATION[position];
        }
    }

    @ItemSelect(R.id.depositorySpinner)
    void depositorySpinnerSelect(boolean selected, int position) {
        if (position == Const.DEPOSITORY.length - 1) {
            depositoryEdit.setVisibility(View.VISIBLE);
        } else {
            depositoryEdit.setVisibility(View.GONE);
            cacheDP = Const.DEPOSITORY[position];
        }
    }

    @Click(R.id.submit)
    void submitClick() {
        saveData();
        if (checkData()) {

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
        if (TextUtils.isEmpty(deposition)) {
            return false;
        }
        if (TextUtils.isEmpty(destination)) {
            return false;
        }
        return true;
    }

    private void saveData() {
        expressCompany = expressCompanyEdit.getVisibility() == View.VISIBLE ? expressCompanyEdit.getText().toString() : cacheEC;
        courinerNumber = courierNumberEdit.getText().toString();
        contactor = contactorEdit.getText().toString();
        phoneNumber = phoneNumberEdit.getText().toString();
        heavy = heavyCheck.isChecked() ? 1 : 0;
        big = bigCheck.isChecked() ? 1 : 0;
        deposition = depositoryEdit.getVisibility() == View.VISIBLE ? depositoryEdit.getText().toString() : cacheDP;
        destination = destinationEdit.getVisibility() == View.VISIBLE ? destinationEdit.getText().toString() : cacheDT;
        expressPassword = expressPasswordEdit.getText().toString();
        remark = remarkEdit.getText().toString();
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, Const.EXPRESS_COMPANY);
        expressCompanySpinner.setAdapter(adapter);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Const.DEPOSITORY);
        depositorySpinner.setAdapter(adapter);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Const.DESTINATION);
        destinationSpinner.setAdapter(adapter);

    }

    private void setActionBar() {
        actionBar.setTitle("发布");
        actionBar.setRigthImgVisible(View.GONE);
        actionBar.setOnHeadImgClickListenner(this);
    }

    @Override
    public void leftImgClick(ImageView view) {
        finish();
    }

    @Override
    public void rightImgClick(ImageView view) {

    }
}
