package cn.mandroid.express.UI.dialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.edmodo.cropper.CropImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2015/12/16.
 */
@EFragment(R.layout.dialog_crop)
public class CropDialog extends BaseDialogFragment {
    @ViewById
    CropImageView cropImageView;
    @FragmentArg
    String icoPath;
    CropCallback cropCallback;

    @AfterViews
    void afterView() {
        Bitmap bitmap = BitmapFactory.decodeFile(icoPath);
        cropImageView.setImageBitmap(bitmap);

    }

    @Click(R.id.cropCompleteBut)
    void confirm() {
        Bitmap bitmap = cropImageView.getCroppedImage();
        cropCallback.onComplete(bitmap);
        dismissAllowingStateLoss();
    }

    public void setCropCallback(CropCallback cropCallback) {
        this.cropCallback = cropCallback;
    }

    public interface CropCallback {
        public void onComplete(Bitmap bitmap);
    }
}
