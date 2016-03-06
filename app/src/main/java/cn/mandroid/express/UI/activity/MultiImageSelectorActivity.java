package cn.mandroid.express.UI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;

import cn.mandroid.express.R;
import cn.mandroid.express.UI.common.BasicActivity;

/**
 * 多图选择
 * Created by Nereo on 2015/4/7.
 */
@EActivity(R.layout.activity_default)
public class MultiImageSelectorActivity extends BasicActivity implements MultiImageSelectorFragment.Callback {
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";

    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;
    @Extra
    ArrayList<String> resultList = new ArrayList<>();
    @ViewById(R.id.commit)
    Button mSubmitButton;
    @Extra
    int defaultCount;
    @Extra
    int mode;
    @Extra
    boolean isShow;

    @AfterViews
    void afterView() {
        if (defaultCount == 0) {
            defaultCount = 9;
        }
        MultiImageSelectorFragment fragment = MultiImageSelectorFragment_.builder().selectedList(resultList).desireImageCount(defaultCount).mode(mode).showCamera(isShow).build();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, fragment)
                .commit();
        // 完成按钮
        if (resultList == null || resultList.size() <= 0) {
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        } else {
            mSubmitButton.setText("完成(" + resultList.size() + "/" + defaultCount + ")");
            mSubmitButton.setEnabled(true);
        }
    }
    @Click(R.id.btn_back)
    void backClick(){
        setResult(RESULT_CANCELED);
        finish();
    }
    @Click(R.id.commit)
    void commit(){
        if (resultList != null && resultList.size() > 0) {
            // 返回已选择的图片数据
            Intent data = new Intent();
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }
    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if (resultList.size() > 0) {
            mSubmitButton.setText("完成(" + resultList.size() + "/" + defaultCount + ")");
            if (!mSubmitButton.isEnabled()) {
                mSubmitButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
            mSubmitButton.setText("完成(" + resultList.size() + "/" + defaultCount + ")");
        } else {
            mSubmitButton.setText("完成(" + resultList.size() + "/" + defaultCount + ")");
        }
        // 当为选择图片时候的状态
        if (resultList.size() == 0) {
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
