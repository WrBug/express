package cn.mandroid.express.UI.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2015-10-21.
 */
public class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        super(context, R.style.MyDialog);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }
}
