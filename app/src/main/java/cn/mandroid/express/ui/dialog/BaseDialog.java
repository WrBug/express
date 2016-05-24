package cn.mandroid.express.ui.dialog;

import android.app.Dialog;
import android.content.Context;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2015-10-21.
 */
public class BaseDialog extends Dialog {
    protected Context context;

    public BaseDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context=context;
//        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

}
