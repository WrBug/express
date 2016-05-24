package cn.mandroid.express.UI1.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2016/1/27 0027.
 */
public class StepView extends RelativeLayout {
    private Context context;
    private TextView numText;
    private TextView bottomText;
    private ImageView imageView;
    private TextView dateText;

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.step_view, this);
        imageView = (ImageView) findViewById(R.id.imageView);
        numText = (TextView) findViewById(R.id.numText);
        bottomText = (TextView) findViewById(R.id.bottomText);

        TypedArray tArray = context.obtainStyledAttributes(attrs,
                R.styleable.StepView);
        int count = tArray.getInt(R.styleable.StepView_count, 0);
        numText.setText(count + "");
        String bottomText = tArray.getString(R.styleable.StepView_bottomText);
        this.bottomText.setText(bottomText);
    }

    public void setIsRunning(boolean isRunning) {
        if (isRunning) {
            numText.setTextColor(getResources().getColor(R.color.app_main));
            imageView.setImageResource(R.drawable.ic_task_detail_step_running);
            bottomText.setTextColor(getResources().getColor(R.color.app_main));
//            dateText.setText(date);
        } else {
            numText.setTextColor(getResources().getColor(R.color.trans_30_black));
            imageView.setImageResource(R.drawable.ic_task_detail_step_not_start);
            bottomText.setTextColor(getResources().getColor(R.color.trans_30_black));
//            dateText.setText(date);
        }
    }
}
