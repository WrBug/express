package cn.mandroid.express.UI.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2016/1/26 0026.
 */
public class DoubleTextView extends LinearLayout {
    private Context context;
    private TextView leftTextView;
    private TextView rightTextView;

    public DoubleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.double_text_view, this);
        leftTextView = (TextView) findViewById(R.id.leftText);
        rightTextView = (TextView) findViewById(R.id.rightText);
        TypedArray tArray = context.obtainStyledAttributes(attrs,
                R.styleable.DoubleTextView);
        String leftText = tArray.getString(R.styleable.DoubleTextView_leftText);
        if (!TextUtils.isEmpty(leftText)) {
            leftTextView.setText(leftText);
        }
        String rightText = tArray.getString(R.styleable.DoubleTextView_rightText);
        if (!TextUtils.isEmpty(rightText)) {
            rightTextView.setText(rightText);
        }
//        Color leftColor=getResources().getColor(R.styleable.DoubleTextView_leftTextColor);
    }
}
