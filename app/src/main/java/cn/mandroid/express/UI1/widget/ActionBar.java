package cn.mandroid.express.UI1.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.mandroid.express.R;

public class ActionBar extends RelativeLayout implements OnClickListener {
    private ImageView leftImg;
    private ImageView rightImg;
    private TextView titleText;
    private OnHeadImgClickListener clickListenner;
    private Context context;

    public ActionBar(Context context, AttributeSet attr) {
        super(context, attr);
        // TODO Auto-generated constructor stub
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_header, this);
        leftImg = (ImageView) findViewById(R.id.headLeftImg);
        rightImg = (ImageView) findViewById(R.id.headRightImg);
        titleText = (TextView) findViewById(R.id.headText);
        TypedArray tArray = context.obtainStyledAttributes(attr,
                R.styleable.ActionBar);
        String title = tArray.getString(R.styleable.ActionBar_title);
        leftImg.setImageResource(tArray.getResourceId(R.styleable.ActionBar_leftImg, R.drawable.ic_head_back));
        if (!TextUtils.isEmpty(title)) {
            titleText.setText(title);
        }
        if (tArray.getBoolean(R.styleable.ActionBar_hideMoreView, false)) {
            rightImg.setVisibility(GONE);
        }
        if (tArray.getBoolean(R.styleable.ActionBar_hideBackView, false)) {
            leftImg.setVisibility(GONE);
        } else {
            setOnLeftImgClickListener();
        }
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }

    public void setLeftImgVisible(int visible) {
        leftImg.setVisibility(visible);
    }

    public void setRigthImgVisible(int visible) {
        rightImg.setVisibility(visible);
    }

    public void setOnHeadImgClickListener(OnHeadImgClickListener clickListenner) {
        this.clickListenner = clickListenner;
        leftImg.setOnClickListener(this);
        rightImg.setOnClickListener(this);
    }

    private void setOnLeftImgClickListener() {
        leftImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });
    }

    public void setOnRightImgClickListener(OnClickListener listener) {
        rightImg.setOnClickListener(listener);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.headLeftImg:
                clickListenner.leftImgClick(leftImg);
                break;
            case R.id.headRightImg:
                clickListenner.rightImgClick(rightImg);
                break;
            default:
                break;
        }
    }

    public interface OnHeadImgClickListener {
        public void leftImgClick(ImageView view);

        public void rightImgClick(ImageView view);
    }

}
