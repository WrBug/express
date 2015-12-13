package cn.mandroid.express.UI.widget;

import android.content.Context;
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
    private OnHeadImgClickListenner clickListenner;

    public ActionBar(Context context, AttributeSet attr) {
        super(context, attr);
        // TODO Auto-generated constructor stub
        LayoutInflater.from(context).inflate(R.layout.layout_header, this);
        leftImg = (ImageView) findViewById(R.id.headLeftImg);
        rightImg = (ImageView) findViewById(R.id.headRightImg);
        titleText = (TextView) findViewById(R.id.headText);
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

    public void setOnHeadImgClickListenner(OnHeadImgClickListenner clickListenner) {
        this.clickListenner = clickListenner;
        leftImg.setOnClickListener(this);
        rightImg.setOnClickListener(this);
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

    public interface OnHeadImgClickListenner {
        public void leftImgClick(ImageView view);

        public void rightImgClick(ImageView view);
    }

}
