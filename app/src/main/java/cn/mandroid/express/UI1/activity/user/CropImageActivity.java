package cn.mandroid.express.UI1.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.edmodo.cropper.CropImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import cn.mandroid.express.R;
import cn.mandroid.express.UI1.common.BasicActivity;
import cn.mandroid.express.UI1.widget.ActionBar;
import cn.mandroid.express.Utils1.FileUtils;

@EActivity(R.layout.activity_crop_image)
public class CropImageActivity extends BasicActivity implements ActionBar.OnHeadImgClickListener {
    @ViewById
    ActionBar actionBar;
    @ViewById
    CropImageView cropImageView;
    @Extra
    String imagePath;

    @AfterViews
    void afterView() {
        actionBar.setOnHeadImgClickListener(this);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        cropImageView.setImageBitmap(bitmap);
    }

    @Override
    public void leftImgClick(ImageView view) {
        Bitmap bitmap = cropImageView.getCroppedImage();
        File file = FileUtils.saveBitmapFile(context, bitmap, "avatar");
        Intent intent = new Intent();
        intent.putExtra("file", file);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void rightImgClick(ImageView view) {

    }
}
