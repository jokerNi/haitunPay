package cn.d.exds.ase.page;

import android.content.Context;
import android.view.LayoutInflater;
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

import cn.d.exds.ase.R;
import cn.d.exds.ase.adapter.MyGridAdapter;
import cn.d.exds.ase.base.BasePage;
import cn.d.exds.ase.bean.MovieData;
import cn.d.exds.ase.ui.HeaderGridView;
import cn.d.exds.ase.ui.HuoBaoRollViewPager;
import cn.d.exds.ase.util.GsonTools;
import cn.d.exds.ase.util.Logger;
import cn.d.exds.ase.util.ShowToast;
import cn.d.exds.ase.util.UIUtils;

/**
 * 此页面是体验会员
 * Created by schwager on 2016/6/23.
 */
public class FreeMemberPage2 extends BasePage {


    private TextView mTitle;
    private HeaderGridView mGridView;

    private String mUrl;
    private ArrayList<String> mPlayUrls;
    private ArrayList<String> mTitleLists;
    private ArrayList<String> mImageLists;
    private LinearLayout top_roll_view;
    private TextView top_news_title;
    private LinearLayout dots_ll;
    private ScrollView mMain_srcoll;

    private String mType;
    private String mTitleName;
    public MovieData mMovieData;
    private LinearLayout mHead_view;

    public FreeMemberPage2(Context context, String url, String type, String titleName) {
        super(context);
        this.mUrl = url;
        this.mType = type;
        this.mTitleName = titleName;
    }

    @Override
    public View initView() {
        //填充首页
        View rootView = UIUtils.inflate(R.layout.layout_free_page_new);
        mTitle = (TextView) rootView.findViewById(R.id.titlebar_tv_title);
        mGridView = (HeaderGridView) rootView.findViewById(R.id.main_gv);
        mGridView.setFocusable(false);
        mMain_srcoll = (ScrollView) rootView.findViewById(R.id.main_srcoll);
        //初始化头布局中的控件
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
        getDataFromNet();
    }

    @Override
    public void onDestroy() {
        mGridView.removeHeaderView(top_roll_view);
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
            mGridView.addHeaderView(top_roll_view);
            MyGridAdapter myGridAdapter = new MyGridAdapter(mContext, mMovieData.getCon(), mType);
            mGridView.setAdapter(myGridAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initTopView(final MovieData movieData) {
        top_roll_view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_roll_view, null, true);
        top_news_title = (TextView) top_roll_view.findViewById(R.id.top_news_title);
        dots_ll = (LinearLayout) top_roll_view.findViewById(R.id.dots_ll);
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
                UIUtils.playVideoTrySee(mPlayUrls.get(position), mTitleLists.get(position), "true", movieData.getCon().get(position).getDianying_id());
            }
        });
        //调用设置title等控件的方法
        mHuoBaoRollViewPager.setTextTitle(top_news_title, mTitleLists);
        mHuoBaoRollViewPager.setImageRes(mImageLists);
        //自定义ViewPager的初始化
        mHuoBaoRollViewPager.start();
        //添加轮播图到顶布局
        //top_roll_view.removeAllViews();
        top_roll_view.addView(mHuoBaoRollViewPager);
        Logger.getInstance().e("qw", "FreeMemberPage2.initTopView.childCount= "+mHuoBaoRollViewPager.mImageLists.size());
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
