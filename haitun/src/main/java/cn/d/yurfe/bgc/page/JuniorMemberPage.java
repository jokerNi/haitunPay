package cn.d.yurfe.bgc.page;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import cn.d.yurfe.bgc.ui.AutoTextView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.adapter.MyGridAdapter;
import cn.d.yurfe.bgc.base.BasePage;
import cn.d.yurfe.bgc.bean.MovieData;
import cn.d.yurfe.bgc.ui.HuoBaoRollViewPager;
import cn.d.yurfe.bgc.ui.NoScrollGridView;
import cn.d.yurfe.bgc.util.GsonTools;
import cn.d.yurfe.bgc.util.Logger;
import cn.d.yurfe.bgc.util.ShowToast;
import cn.d.yurfe.bgc.util.UIUtils;

/**
 * 此页面是体验会员
 * Created by schwager on 2016/6/23.
 */
public class JuniorMemberPage extends BasePage {


    private TextView mTitle;
    private NoScrollGridView mGridView;

    private String mUrl;
    private ArrayList<String> mPlayUrls;
    private ArrayList<String> mTitleLists;
    private ArrayList<String> mImageLists;
    private LinearLayout top_news_viewpager;
    private TextView top_news_title;
    private LinearLayout dots_ll;
    private ScrollView mMain_srcoll;

    private String mType;
    private AutoTextView mText;

    public JuniorMemberPage(Context context, String url, String type) {
        super(context);
        this.mUrl = url;
        this.mType = type;
    }

    @Override
    public View initView() {
        //填充首页
        View rootView = UIUtils.inflate(R.layout.layout_junior_page);
        mText = (AutoTextView) rootView.findViewById(R.id.mytext);
        mTitle = (TextView) rootView.findViewById(R.id.titlebar_tv_title);
        mGridView = (NoScrollGridView) rootView.findViewById(R.id.main_gv);
        mGridView.setFocusable(false);
        mMain_srcoll = (ScrollView) rootView.findViewById(R.id.main_srcoll);
        //初始化头布局中的控件
        top_news_viewpager = (LinearLayout) rootView.findViewById(R.id.top_news_viewpager);
        top_news_title = (TextView) rootView.findViewById(R.id.top_news_title);
        dots_ll = (LinearLayout) rootView.findViewById(R.id.dots_ll);
        //修改顶部title名称
        mTitle.setText("体验区");
        return rootView;
    }

    @Override
    public void initListener() {
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    @Override
    public void initData() {
        mMain_srcoll.smoothScrollTo(0, 0);
        getDataFromNet();
        /*mText.setText("巴基斯坦");
        mText.setVisibility(View.VISIBLE);
        mText.startScroll();*/
        //mHandler.sendEmptyMessageDelayed(11, 10000);
    }

   /* private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 11:
                    mText.stopScroll();
                    break;
            }

        }
    };*/

    @Override
    public void onDestroy() {

    }

    /**
     *
     */
    private void getDataFromNet() {
        OkHttpUtils.get().url(mUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                ShowToast.show(e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                processData(response);
            }
        });


    }

    /**
     * @param response
     */
    private void processData(String response) {
        try {
            MovieData movieData = GsonTools.changeGsonToBean(response, MovieData.class);
            initTopView(movieData);
            //设置适配器

            MyGridAdapter myGridAdapter = new MyGridAdapter(mContext, movieData.getCon(), mType);
            mGridView.setAdapter(myGridAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initTopView(MovieData movieData) {
        //轮播图的播放地址
        mPlayUrls = new ArrayList<>();
        // 轮播图的标题
        mTitleLists = new ArrayList<String>();
        // 轮播图的背景图片
        mImageLists = new ArrayList<String>();
        //添加数据
        for (MovieData.TopEntity topData : movieData.getTop()) {
            mPlayUrls.add(topData.getVideo());
            mTitleLists.add(topData.getTitle());
            mImageLists.add(topData.getPic());
        }
        //初始化点集合
        initDot(mTitleLists.size());
        //创建Viewpager
        HuoBaoRollViewPager mHuoBaoRollViewPager = new HuoBaoRollViewPager(mContext, mDotLists, new HuoBaoRollViewPager.ViewPageOnTouchListener() {
            @Override
            public void onViewPageClickListener(int position) {
                // TODO: 2016/6/24 点击事件
                UIUtils.playVideoTrySee(mPlayUrls.get(position), mTitleLists.get(position), "true");
            }
        });
        //调用设置title等控件的方法
        mHuoBaoRollViewPager.setTextTitle(top_news_title, mTitleLists);
        mHuoBaoRollViewPager.setImageRes(mImageLists);
        //自定义ViewPager的初始化
        mHuoBaoRollViewPager.start();
        //添加轮播图到顶布局
        top_news_viewpager.removeAllViews();
        top_news_viewpager.addView(mHuoBaoRollViewPager);
    }

    //存放点的集合
    private List<View> mDotLists = new ArrayList<>();

    /**
     * @param size
     */
    private void initDot(int size) {
        //首先清空点的集合
        dots_ll.removeAllViews();
        mDotLists.clear();
        for (int i = 0; i < size; i++) {
            View view = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2px(mContext, 3), UIUtils.dip2px(mContext, 3));
            params.leftMargin = UIUtils.dip2px(mContext, 3);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.dot_focus);
            } else {
                view.setBackgroundResource(R.drawable.dot_normal);
            }
            view.setLayoutParams(params);
            //添加点到布局 集合
            dots_ll.addView(view);
            mDotLists.add(view);
        }

    }


}
