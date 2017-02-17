package cn.d.sedfr.fhd.page;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import cn.d.sedfr.fhd.R;
import cn.d.sedfr.fhd.adapter.MyGridAdapter;
import cn.d.sedfr.fhd.base.BasePage;
import cn.d.sedfr.fhd.bean.MovieData;
import cn.d.sedfr.fhd.ui.AutoTextView;
import cn.d.sedfr.fhd.ui.HuoBaoRollViewPager;
import cn.d.sedfr.fhd.ui.NoScrollGridView;
import cn.d.sedfr.fhd.util.GsonTools;
import cn.d.sedfr.fhd.util.ShowToast;
import cn.d.sedfr.fhd.util.UIUtils;
import cn.d.sedfr.fhd.util.VIP;

/**
 * 此页面是体验会员
 * Created by schwager on 2016/6/23.
 */
public class FreeMemberPage extends BasePage {


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
    private String mTitleName;
    public MovieData mMovieData;

    public FreeMemberPage(Context context, String url, String type, String titleName) {
        super(context);
        this.mUrl = url;
        this.mType = type;
        this.mTitleName = titleName;
    }

    @Override
    public View initView() {
        //填充首页
        View rootView = UIUtils.inflate(R.layout.layout_free_page);
        mText = (AutoTextView) rootView.findViewById(R.id.mytext);
        mTitle = (TextView) rootView.findViewById(R.id.titlebar_tv_title);
        mGridView = (NoScrollGridView) rootView.findViewById(R.id.main_gv);
        mGridView.setFocusable(false);
        mMain_srcoll = (ScrollView) rootView.findViewById(R.id.main_srcoll);
        //初始化头布局中的控件
        top_news_viewpager = (LinearLayout) rootView.findViewById(R.id.top_news_viewpager);
        top_news_title = (TextView) rootView.findViewById(R.id.top_news_title);
        dots_ll = (LinearLayout) rootView.findViewById(R.id.dots_ll);
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
        //修改顶部title名称
        mTitle.setText(mTitleName);
        mMain_srcoll.smoothScrollTo(0, 0);
        getDataFromNet();
    }

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
            mMovieData = GsonTools.changeGsonToBean(response, MovieData.class);
            initTopView(mMovieData);
            //设置适配器
            MyGridAdapter myGridAdapter = new MyGridAdapter(mContext, mMovieData.getCon(), mType);
            mGridView.setAdapter(myGridAdapter);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initTopView(final MovieData movieData) {
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
                UIUtils.playVideoTrySee(mPlayUrls.get(position), mTitleLists.get(position), "true", movieData.getCon().get(position).getDianying_id());
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
