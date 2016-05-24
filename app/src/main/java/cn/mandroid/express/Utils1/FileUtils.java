package cn.mandroid.express.Utils1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.mandroid.express.R;

/**
 * 文件操作类
 * Created by Nereo on 2015/4/8.
 */
public class FileUtils {
    public static String FRIEND_LIST="friendsList";
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static File createTmpFile(Context context) {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(pic, fileName + ".jpg");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(cacheDir, fileName + ".jpg");
            return tmpFile;
        }
    }

    public static File saveBitmapFile(Context context, Bitmap bitmap, String fileName) {
        String cache = getDiskCacheDir(context);
        File file = new File(cache + "/" + fileName + ".png");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            MLog.i(bitmap.getHeight() + ":" + bitmap.getWidth());
            int quality = 100;
            if (bitmap.getWidth() > 1080) {
                quality = 100 * 1080 / bitmap.getWidth();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String readFile(Context context,String fileName)  {
        String res = "";
        try {
            FileInputStream fin = context.openFileInput(MD5.encode(fileName));
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public static void writeFile(Context context,String fileName,String writestr)  {
        try {
            FileOutputStream fout = context.openFileOutput(MD5.encode(fileName), Context.MODE_PRIVATE);
            byte[] bytes = writestr.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Uri res2Uri(Context context, int res) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
        File file = saveBitmapFile(context, bitmap, res + "");
        return Uri.fromFile(file);
    }

    public static Uri getDefalutManIco(Context context) {
        return res2Uri(context, R.drawable.ic_user_default_man);
    }

    public static Uri getDefalutWomanIco(Context context) {
        return res2Uri(context, R.drawable.ic_user_default_woman);
    }

    public static String getSdcardPath() {
        File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        return sdDir.toString();
    }
}
