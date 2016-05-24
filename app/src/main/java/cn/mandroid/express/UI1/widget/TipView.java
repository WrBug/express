package cn.mandroid.express.UI1.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

/**
 * 引导帮助提示了类
 * 为锚点View添加图片提示 {@link #drawDrawble(DataDrawble)}</p>
 * 为锚点View添加View提示 {@link #drawImageView(DataView)}
 *
 * @author Wancho
 */
public class TipView extends RelativeLayout {

    private Context mContext;

    private List<DataDrawble> drawbles;

    private List<DataView> views;

    private Display display;
    private Position position;

    public TipView(Context context) {
        this(context, Position.RIGHT);
    }

    public TipView(Context context, Position position) {
        super(context);
        this.mContext = context;
        setWillNotDraw(false);
        this.position = position;
        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                drop();
            }
        });
        drawbles = new ArrayList<TipView.DataDrawble>(10);
        views = new ArrayList<DataView>(1);
        init();
    }

    @SuppressWarnings("deprecation")
    private void init() {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            display = activity.getWindowManager().getDefaultDisplay();
            ViewGroup vgDecorView = (ViewGroup) activity.getWindow().getDecorView();
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(display.getWidth() * 2, display.getHeight() * 2);
            if (vgDecorView.indexOfChild(this) == -1) {
                vgDecorView.addView(this, lp);
            }
            removeAllViews();
        }
    }

    /**
     * 针对锚点View绘制一张图片，无点击事件
     *
     * @param dataDrawble
     */
    public void drawDrawble(DataDrawble dataDrawble) {
        drawbles.add(dataDrawble);
        invalidate();
    }

    /**
     * 针对锚点View绘制一个ImageView，可以设置点击事件,对{@link #drawImageView(DataView)}的简单封装
     */
    public void drawWrapImageView(DataWrapImageView wrapImageView) {
        if (wrapImageView != null) {
            ImageView ivTemp = new ImageView(mContext);
            ivTemp.setOnClickListener(wrapImageView.getClickListener());
            ivTemp.setImageResource(wrapImageView.getImageRes());
            drawImageView(new DataView(wrapImageView.getViewAnchor(), ivTemp));
        }
    }

    /**
     * 针对锚点View绘制一个ImageView，可以设置点击事件
     */
    public void drawImageView(DataView dataImageView) {
        if (dataImageView != null) {
            views.add(dataImageView);
            invalidate();
        }
    }

    /**
     * 隐藏TipView
     */
    public void drop() {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            ViewGroup vgDecorView = (ViewGroup) activity.getWindow().getDecorView();
            if (vgDecorView.indexOfChild(this) != -1) {
                vgDecorView.removeView(this);
            }
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (DataDrawble data : drawbles) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), data.getImgRes());
            Point point = getLeftAndTopPoint(data.getViewTarget(), bitmap);
            canvas.drawBitmap(bitmap, point.x, point.y, null);
        }
    }

    private Point getLeftAndTopPoint(View viewTarget, Bitmap bitmap) {
        Point point = new Point();
        int[] location = new int[2];
        viewTarget.getLocationInWindow(location);
        point.x = location[0] + viewTarget.getWidth() / 2 - bitmap.getWidth() / 2;
        point.y = location[1] + viewTarget.getHeight() / 2 - bitmap.getHeight() / 2;
        return point;
    }

    @SuppressWarnings("deprecation")
    private Point getLeftAndTopPoint(View viewAnchor, View viewTip) {
        Point point = new Point();
        int[] location = new int[2];
        viewAnchor.getLocationInWindow(location);
        viewTip.measure(0, 0);
        point.x = location[0] + viewAnchor.getWidth() / 2 - viewTip.getMeasuredWidth() / 2;
        point.y = location[1] + viewAnchor.getHeight() / 2 - viewTip.getMeasuredHeight() / 2;
        FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) getLayoutParams();
        if (point.x + viewAnchor.getMeasuredWidth() > display.getWidth()) {
            lp.width = FrameLayout.LayoutParams.WRAP_CONTENT;
            setLayoutParams(lp);
        }
        if (point.y + viewAnchor.getMeasuredHeight() > display.getHeight()) {
            lp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            setLayoutParams(lp);
        }
        return point;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        RelativeLayout.LayoutParams lpChild = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        for (DataView data : views) {
            View viewAnchor = data.getViewTarget();
            View viewTip = data.getViewTip();
            if (viewTip instanceof ImageView) {
                ((ImageView) viewTip).setScaleType(ScaleType.CENTER_CROP);
            }
            if (indexOfChild(viewTip) == -1) {
                Point point = getLeftAndTopPoint(viewAnchor, viewTip);
                setPadding(viewTip, point);
                addView(viewTip, lpChild);
            }
        }
    }

    private void setPadding(View viewTip, Point point) {
        switch (position) {
            case LEFT:
                viewTip.setPadding(0, point.y, point.x, 0);
                break;
            case RIGHT:
                viewTip.setPadding(point.x, point.y, 0, 0);
                break;
            case TOP:
                viewTip.setPadding(point.x / 2, 0, 0, point.y);
                break;
            case BOTTOM:
                viewTip.setPadding(point.x / 2, point.y, 0, 0);
                break;
        }
    }


    public static class DataDrawble {

        View viewAnchor;

        int imgRes;

        public DataDrawble(View viewAnchor, int imgRes) {
            super();
            this.viewAnchor = viewAnchor;
            this.imgRes = imgRes;
        }

        public View getViewTarget() {
            return viewAnchor;
        }

        public void setViewTarget(View viewTarget) {
            this.viewAnchor = viewTarget;
        }

        public int getImgRes() {
            return imgRes;
        }

        public void setImgRes(int imgRes) {
            this.imgRes = imgRes;
        }
    }

    public static class DataWrapImageView {

        View viewAnchor;

        int imageRes;

        View.OnClickListener clickListener;

        public DataWrapImageView(View viewAnchor, int imageRes, OnClickListener clickListener) {
            super();
            this.viewAnchor = viewAnchor;
            this.imageRes = imageRes;
            this.clickListener = clickListener;
        }

        public View getViewAnchor() {
            return viewAnchor;
        }

        public int getImageRes() {
            return imageRes;
        }

        public View.OnClickListener getClickListener() {
            return clickListener;
        }

    }

    public static class DataView {

        View viewAnchor;

        View viewTip;

        public DataView(View viewAnchor, View viewTip) {
            super();
            this.viewAnchor = viewAnchor;
            this.viewTip = viewTip;
        }

        public View getViewTarget() {
            return viewAnchor;
        }

        public View getViewTip() {
            return viewTip;
        }
    }

    public enum Position {
        LEFT, RIGHT, TOP, BOTTOM
    }
}
