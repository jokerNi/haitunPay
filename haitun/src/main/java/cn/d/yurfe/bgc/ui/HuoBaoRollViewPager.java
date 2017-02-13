package cn.d.yurfe.bgc.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.util.Logger;

/**
 * Created by schwager on 2016/4/20.
 */
public class HuoBaoRollViewPager extends ViewPager {

    private List<View> mDotLists;
    private Context mContext;
    private ViewPageOnTouchListener mViewPageOnTouchListener;

    public interface ViewPageOnTouchListener {
        void onViewPageClickListener(int position);
    }


    public HuoBaoRollViewPager(Context context) {
        super(context);
    }

    public HuoBaoRollViewPager(Context context, List<View> mDotLists, ViewPageOnTouchListener viewPageOnTouchListener) {
        super(context);
        this.mContext = context;
        this.mDotLists = mDotLists;
        this.mViewPageOnTouchListener = viewPageOnTouchListener;
    }

    private int mCurrentItem;
    private boolean hasAdapter = false;
    private int oldPosition = 0;

    //轮播图继续跳动的方法
    public void start() {
        if (!hasAdapter) {
            // 没有adapter进入 重新设置adapter
            hasAdapter = true;
            RollViewPagerAdapter adapter = new RollViewPagerAdapter();
            HuoBaoRollViewPager.this.setAdapter(adapter);
            HuoBaoRollViewPager.this.setOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    int pos = position % mImageLists.size();
                    mCurrentItem = pos;

                    if (null != mTitleLists && mTitleLists.size() > 0 && null != mTopNewsTitle) {
                        mTopNewsTitle.setText(mTitleLists.get(pos));
                    }

                    if (null != mDotLists && mDotLists.size() > 0) {
                        mDotLists.get(pos).setBackgroundResource(R.drawable.dot_focus);
                        mDotLists.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);


                    }

                    oldPosition = pos;


                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }


                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


        }
    }


    // 事件分发处理
    // 是否滑动
    private boolean isMove = false;

    private int downX;
    private int downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                int currentX = (int) ev.getX();
                int currentY = (int) ev.getY();
                if (Math.abs(currentX - downX) > Math.abs(currentY - downY)) {
                    // 左右滑动viewPage
                    isMove = false;
                } else {
                    // 上下滑动ListView
                    isMove = true;
                }

                break;

        }
        //请求父类不要拦截我
        getParent().requestDisallowInterceptTouchEvent(!isMove);

        return super.dispatchTouchEvent(ev);
    }

    private List<String> mImageLists;

    /**
     * 设置背景图片
     *
     * @param imageLists
     */
    public void setImageRes(List<String> imageLists) {
        this.mImageLists = imageLists;

    }

    private List<String> mTitleLists;

    private TextView mTopNewsTitle;

    /**
     * 设置轮播图上面的标题
     *
     * @param topNewsTitle
     * @param titleLists
     */
    public void setTextTitle(TextView topNewsTitle, List<String> titleLists) {
        if (null != topNewsTitle && null != titleLists
                && titleLists.size() > 0) {
            this.mTopNewsTitle = topNewsTitle;
            this.mTitleLists = titleLists;
            mTopNewsTitle.setText(mTitleLists.get(0));
        }

    }

    private class RollViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            //初始化View的方法
            View view = View.inflate(mContext, R.layout.viewpager_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.top_vp_image);
            final int pos = position % mImageLists.size();
            try{
                // TODO: 2016/6/24 展示图片
                Picasso.with(mContext).load(mImageLists.get(pos)).into(imageView);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPageOnTouchListener.onViewPageClickListener(pos);
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
                Logger.getInstance().e("qw", "RollViewPagerAdapter.instantiateItem.数据异常");
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}































