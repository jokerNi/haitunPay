package cn.d.sedfr.fhd.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.d.sedfr.fhd.ui.view.LazyViewPager;

/**
 * Created by schwager on 2016/4/18.
 */
public class CustomViewPager extends LazyViewPager {
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //重写事件不拦截的方法
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    /**
     * 重写事件不处理的方法????
     * 还是要处理的 否则
     * ViewPager失去滑动效果
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }


}
