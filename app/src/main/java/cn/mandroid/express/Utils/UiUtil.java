package cn.mandroid.express.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015-12-13.
 */
public class UiUtil {
    private static Map<String, Bitmap> imgCache = new HashMap<>();

    public static void loadImage(Context context, final ImageView imageView, final String url) {
        if (imgCache.containsKey(url)) {
            imageView.setImageBitmap(imgCache.get(url));
            return;
        }
        Ion.with(context).load(url).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                if (e == null) {
                    imgCache.put(url, result);
                    imageView.setImageBitmap(result);
                } else {
                }
            }
        });
    }
}
