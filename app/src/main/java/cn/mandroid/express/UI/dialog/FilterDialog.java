package cn.mandroid.express.UI.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.Arrays;

import cn.mandroid.express.Model.Bean.FilterBean;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.widget.EditSpinner;
import cn.mandroid.express.Utils.Const;

/**
 * Created by Administrator on 2016/2/26 0026.
 */
public class FilterDialog extends BaseDialog {
    private CheckBox penndingCheck;
    private CheckBox runningCheck;
    private CheckBox finishCheck;
    private CheckBox completeCheck;
    private EditSpinner depoSpinner;
    private EditSpinner destSpinner;
    private Button submit;
    private FilterCallback callback;
    private FilterBean mFilterBean;

    public FilterDialog(Context context, FilterBean bean) {
        super(context);
        mFilterBean = bean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filter);
        initView();
        initData();
    }

    public void setFilterCallback(FilterCallback callback) {
        this.callback = callback;
    }

    private void initData() {
        penndingCheck.setChecked(mFilterBean.isPennding());
        runningCheck.setChecked(mFilterBean.isRunning());
        finishCheck.setChecked(mFilterBean.isFinish());
        completeCheck.setChecked(mFilterBean.isComplete());
        destSpinner.setItemData(Arrays.asList(Const.DESTINATION));
        destSpinner.setText(mFilterBean.getDest());
        depoSpinner.setItemData(Arrays.asList(Const.DEPOSITORY));
        depoSpinner.setText(mFilterBean.getDepo());
    }

    private void initView() {
        penndingCheck = (CheckBox) findViewById(R.id.penndingCheck);
        runningCheck = (CheckBox) findViewById(R.id.runningCheck);
        finishCheck = (CheckBox) findViewById(R.id.finishCheck);
        completeCheck = (CheckBox) findViewById(R.id.completeCheck);
        depoSpinner = (EditSpinner) findViewById(R.id.depoSpinner);
        destSpinner = (EditSpinner) findViewById(R.id.destSpinner);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterBean bean = new FilterBean();
                bean.setDepo(depoSpinner.getEditString());
                bean.setDest(destSpinner.getEditString());
                bean.setFinish(finishCheck.isChecked());
                bean.setPennding(penndingCheck.isChecked());
                bean.setRunning(runningCheck.isChecked());
                bean.setComplete(completeCheck.isChecked());
                if (callback != null) {
                    callback.onClick(bean);
                }
                dismiss();
            }
        });
    }

    public interface FilterCallback {
        void onClick(FilterBean bean);
    }
}
