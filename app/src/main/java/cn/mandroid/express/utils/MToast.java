package cn.mandroid.express.utils;

import android.content.Context;
import android.widget.Toast;

public class MToast {
    public static void show(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    public static void showError(Context context, int status) {

    }
    public static void showError(Context context, String msg) {
        show(context, msg);
    }

}
