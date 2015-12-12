package cn.mandroid.express.UI.dialog;

import android.content.Context;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2015-12-12.
 */
public class ProgressDialog extends BaseDialog {
    private static ProgressDialog dialog;
    private ProgressDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_progress);
        setCancelable(false);
    }
    public static ProgressDialog instance(Context context){
        if(dialog==null){
            dialog=new ProgressDialog(context);
        }
        return dialog;
    }
}
