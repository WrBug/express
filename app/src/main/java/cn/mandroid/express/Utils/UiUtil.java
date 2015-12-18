package cn.mandroid.express.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015-12-13.
 */
public class UiUtil {
    public static void loadImage(Context context, final ImageView imageView, final String url) {
        Picasso.with(context).load(Uri.parse(url)).into(imageView);
//        Ion.with(context).load(url).asBitmap().setCallback(new FutureCallback<Bitmap>() {
//            @Override
//            public void onCompleted(Exception e, Bitmap result) {
//                if (e == null) {
//                    imageView.setImageBitmap(result);
//                } else {
//                }
//            }
//        });
    }
}
