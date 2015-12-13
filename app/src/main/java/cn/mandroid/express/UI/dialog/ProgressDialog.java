package cn.mandroid.express.UI.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2015-12-12.
 */
public class ProgressDialog extends BaseDialog {
    private static ProgressDialog dialog;
    private ProgressDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_progress);
    }

    @Override
    public void show() {
        setCancelable(false);
        Handler handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                setCancelable(true);
                return false;
            }
        });
        handler.sendEmptyMessageDelayed(0,8000);
        super.show();

    }

    public static ProgressDialog instance(Context context){
        if(dialog==null){
            dialog=new ProgressDialog(context);
        }
        return dialog;
    }
}
