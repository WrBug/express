package cn.mandroid.express.ui.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import dmax.dialog.SpotsDialog;

/**
 * Created by Administrator on 2015-12-12.
 */
public class ProgressDialog extends SpotsDialog {
    private static ProgressDialog dialog;

    public ProgressDialog(Context context, CharSequence message) {
        super(context, message);
    }

    public ProgressDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {
        setCancelable(false);
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                setCancelable(true);
                return false;
            }
        });
        handler.sendEmptyMessageDelayed(0, 8000);
        super.show();
    }

//    public static ProgressDialog instance(Context context){
//        if(dialog==null){
//            dialog=new ProgressDialog(context);
//        }
//        return dialog;
//    }
}
