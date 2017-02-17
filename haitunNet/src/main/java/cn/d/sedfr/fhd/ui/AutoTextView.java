package cn.d.sedfr.fhd.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoTextView extends TextView implements Runnable {
    private int currentScrollX; // 当前滚动的位置
    private boolean isStop = false;
    private int textWidth;
    private boolean isMeasure = false;

    public AutoTextView(Context context) {
        super(context);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        currentScrollX = this.getWidth();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isMeasure) {
            getTextWidth();// 文字宽度只需要获取一次就可以了
            isMeasure = true;
        }
    }

    private void getTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        textWidth = (int) paint.measureText(str);
    }

    @Override
    /*
     * public void run() { currentScrollX-=2;//滚动速度.+号表示往左边-
	 * scrollTo(currentScrollX,0); if(isStop){ return; }
	 * if(getScrollX()<=-(this.getWidth())){ scrollTo(textWidth,0);
	 * currentScrollX=textWidth; } postDelayed(this, 5); }
	 */
    public void run() {
        currentScrollX += 2;// 滚动速度.+号表示往左边-
        scrollTo(currentScrollX, 0);
        if (isStop) {
            return;
        }
        if (getScrollX() >= (textWidth)) {
            currentScrollX = -(this.getWidth());// 当前出现的位置
        }
        postDelayed(this, mSpeed);
    }

    private int mSpeed = 7;

    public void setSpeed(int speed) {
        this.mSpeed = speed;
    }

    // 开始滚动
    public void startScroll() {
        isStop = false;
        this.removeCallbacks(this);
        post(this);
    }

    // 停止滚动
    public void stopScroll() {
        isStop = true;
    }

    // 从头开始滚动
    public void startFromHead() {
        currentScrollX = 0;
        startScroll();
    }

}

