package cn.mandroid.express.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015-12-13.
 */
public class UiUtil {
    public static void hideKeyboard(Context ctx) {
        if(ctx == null) return;
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(),  InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public static void loadImage(Context context, final ImageView imageView, final String url) {
        Picasso.with(context).load(Uri.parse(url)).into(imageView);
    }

    public static void loadImage(Context context, final ImageView imageView, final int res) {
        imageView.setImageResource(res);
    }
}
