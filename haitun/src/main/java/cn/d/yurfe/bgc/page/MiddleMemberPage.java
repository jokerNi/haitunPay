package cn.d.yurfe.bgc.page;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.adapter.MyGridAdapter;
import cn.d.yurfe.bgc.base.BasePage;
import cn.d.yurfe.bgc.bean.MovieData;
import cn.d.yurfe.bgc.util.GsonTools;
import cn.d.yurfe.bgc.util.Logger;
import cn.d.yurfe.bgc.util.Pass;
import cn.d.yurfe.bgc.util.SharedPreferencesUtils;
import cn.d.yurfe.bgc.util.ShowToast;
import cn.d.yurfe.bgc.util.UIUtils;
import cn.d.yurfe.bgc.util.VIP;

import static cn.d.yurfe.bgc.util.Pass.CAN_PLAY;

/**
 * 此页面是黄金会员
 * Created by schwager on 2016/6/23.
 */
public class MiddleMemberPage extends BasePage {


    private GridView mGridView;
    private String mUrl;
    private int page = 1;
    private int mTotalPage;

    private MyGridAdapter mMyGridAdapter;
    private String mType;
    private TextView middle_page_3000;
    private boolean mShow;
    private View rootView;

    public MiddleMemberPage(Context context, String url, String type, boolean isShow) {
        super(context);
        this.mUrl = url;
        this.mType = type;
        this.mShow = isShow;
    }

    @Override
    public View initView() {
        // 填充页面
        rootView = UIUtils.inflate(R.layout.layout_middle_page);
        mGridView = (GridView) rootView.findViewById(R.id.main_gv);
        middle_page_3000 = (TextView) rootView.findViewById(R.id.middle_page_3000);
        return rootView;
    }


    @Override
    public void initListener() {
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (mMyGridAdapter != null) {
                    int lastIndex = mMyGridAdapter.getCount() - 1;
                    int lastVisiblePostion = mGridView.getLastVisiblePosition();
                    if (lastIndex == lastVisiblePostion && page < mTotalPage) {
                        page = page + 1;
                        getDataFromNet();
                        if (page == mTotalPage - 1) {
//                            ShowToast.show("开通钻石VIP查看完成列表");
                        }
                    }
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    @Override
    public void initData() {
        initMaps();
        initMiddleTitle();
        if (mShow) {
            middle_page_3000.setVisibility(View.VISIBLE);
        } else {
            middle_page_3000.setVisibility(View.GONE);
        }
        getDataFromNet();
    }

    private void initMaps() {
        initTitleLevelMap();
        initTitleNameMap();
    }

    private void initTitleLevelMap() {
        titleLevelMap = new HashMap<>();
        titleLevelMap.put(VIP.GOLD, VIP.GOLDLEVELINT);
        titleLevelMap.put(VIP.DIAMOND, VIP.DIAMONDLEVELINT);
        titleLevelMap.put(VIP.BLACKDIAMOND, VIP.BLACKDIAMONDLEVELINT);
        titleLevelMap.put(VIP.ROYAL, VIP.ROYALLEVELINT);
        titleLevelMap.put(VIP.EXTREME, VIP.EXTREMELEVELINT);
        titleLevelMap.put(VIP.LIFELONG, VIP.LIFELONGLEVELINT);
    }

    private void initTitleNameMap() {
        titleNameMap = new HashMap<>();
        titleNameMap.put(VIP.GOLDLEVELINT, VIP.GOLDNAME);
        titleNameMap.put(VIP.DIAMONDLEVELINT, VIP.DIAMONDNAME);
        titleNameMap.put(VIP.BLACKDIAMONDLEVELINT, VIP.BLACKDIAMONDNAME);
        titleNameMap.put(VIP.ROYALLEVELINT, VIP.ROYALNAME);
        titleNameMap.put(VIP.EXTREMELEVELINT, VIP.EXTREMENAME);
        titleNameMap.put(VIP.LIFELONGLEVELINT, VIP.LIFELONGNAME);
    }

    private Map<String, Integer> titleLevelMap;
    private Map<Integer, String> titleNameMap;

    private void initMiddleTitle() {
        Set<String> levels = titleLevelMap.keySet();
        for (String key : levels) {
            if (SharedPreferencesUtils.getBoolean(UIUtils.getContext(), key, false)) {
                if (TextUtils.equals(mType, Pass.CAN_PLAY)) {
                    String text = titleNameMap.get(titleLevelMap.get(key));
                    mTitle.setText(text);
                } else if (TextUtils.equals(mType, Pass.CANNOT_PLAY)) {
                    String text = titleNameMap.get(titleLevelMap.get(key) + 2);
                    mTitle.setText(text);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        page = 1;
        mTotalPage = 1;
        mMyGridAdapter = null;
    }

    private void getDataFromNet() {
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("User-Agent", "Mozilla/5.0 (Linux; Android ) Mobile huobaoapp");
        try {
            OkHttpUtils.get().url(mUrl + page)
                    .headers(stringHashMap)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Request request, Exception e) {
                    ShowToast.show(e.getMessage());
                }

                @Override
                public void onResponse(String response) {
                    if (page == 1) {
                        processData(response);
                    } else {
                        loadMoreData(response);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void processData(String response) {
        try {
            MovieData movieData = GsonTools.changeGsonToBean(response, MovieData.class);
            page = movieData.getPage();
            mTotalPage = movieData.getTotalPage();
            //设置适配器
            mMyGridAdapter = new MyGridAdapter(mContext, movieData.getCon(), mType);
            mGridView.setAdapter(mMyGridAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadMoreData(String response) {
        try {
            MovieData movieData = GsonTools.changeGsonToBean(response, MovieData.class);
            page = movieData.getPage();
            mTotalPage = movieData.getTotalPage();
            //设置适配器
            if (mMyGridAdapter != null) {
                mMyGridAdapter.addList(movieData.getCon());
                mMyGridAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
