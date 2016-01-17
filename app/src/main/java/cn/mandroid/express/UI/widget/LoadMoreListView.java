package cn.mandroid.express.UI.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import cn.mandroid.express.Utils.MLog;

/**
 * Created by Administrator on 2016/1/17 0017.
 */
public class LoadMoreListView extends ListView implements View.OnTouchListener {
    private float startY;
    private float endY;
    PushToLoadListenner pushToLoadListenner;

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                MLog.i("sssstartY:" + event.getY());
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
//                MLog.i("now:" + this.getLastVisiblePosition() + ":" + getCount());
                break;
            case MotionEvent.ACTION_UP:
//                MLog.i("sssendY:" + event.getY());
                if ((this.getLastVisiblePosition() == (getCount() - 1)) && startY - event.getY() > 500) {
                    if (pushToLoadListenner != null) {
                        pushToLoadListenner.pushToLoad();
                    }
                }
                break;
        }
        return false;
    }

    public void setOnPushToLoadListenner(PushToLoadListenner loadListenner) {
        pushToLoadListenner = loadListenner;
    }

    public interface PushToLoadListenner {
        void pushToLoad();
    }
}
