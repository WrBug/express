package cn.mandroid.express.UI.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import cn.mandroid.express.R;
import cn.mandroid.express.Utils.MLog;

/**
 * Created by Administrator on 2016/1/17 0017.
 */
public class LoadMoreListView extends ListView implements View.OnTouchListener {
    private float startY;
    private float endY;
    View footView;
    PushToLoadListenner pushToLoadListenner;

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        footView = LayoutInflater.from(context).inflate(R.layout.list_view_foot_view, null);
        addFooterView(footView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                MLog.i("sssstartY:" + event.getY());
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                MLog.i("now:" + this.getLastVisiblePosition() + ":" + getCount());
//                if(isBottom(event)&&)
                break;
            case MotionEvent.ACTION_UP:
                MLog.i("sssendY:" + event.getY());
                if (isBottom(event) && startY - event.getY() > 500) {
                    if (pushToLoadListenner != null) {
                        pushToLoadListenner.pushToLoad();
                    }
                }
                break;
        }
        return false;
    }

    private boolean isBottom(MotionEvent event) {
        return (this.getLastVisiblePosition() == (getCount() - 1));
    }

    public void setOnPushToLoadListenner(PushToLoadListenner loadListenner) {
        pushToLoadListenner = loadListenner;
        footView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pushToLoadListenner.pushToLoad();
            }
        });
    }

    public interface PushToLoadListenner {
        void pushToLoad();
    }
}
