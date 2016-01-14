package cn.mandroid.express.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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
    public static void loadImage(Context context, final ImageView imageView, final String url) {
        Picasso.with(context).load(Uri.parse(url)).into(imageView);
    }

    public static void loadImage(Context context, final ImageView imageView, final int res) {
        Picasso.with(context).load(FileUtils.res2Uri(context, res)).into(imageView);
    }
}
