package cn.d.exds.ase.page;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Set;

import cn.d.exds.ase.R;
import cn.d.exds.ase.adapter.MyGridAdapter;
import cn.d.exds.ase.base.BasePage;
import cn.d.exds.ase.bean.MovieData;
import cn.d.exds.ase.util.GsonTools;
import cn.d.exds.ase.util.Logger;
import cn.d.exds.ase.util.Pass;
import cn.d.exds.ase.util.ShowToast;
import cn.d.exds.ase.util.UIUtils;
import cn.d.exds.ase.util.VIP;

/**
 * 此页面是黄金会员
 * Created by schwager on 2016/6/23.
 */
public class CommonMemberPage extends BasePage {


    private GridView mGridView;

    private String mUrl;

    private int page = 1;
    private MyGridAdapter mMyGridAdapter;
    private int mTotalPage;

    private String mType;
    private TextView middle_page_3000;
    private boolean mShow;
    private View rootView;
    public String mText;
    private String mTitleName;
    public RelativeLayout mProgress_com_circular;

    public CommonMemberPage(Context context, String url, String type, boolean isShow, String titleName) {
        super(context);
        this.mUrl = url;
        this.mType = type;
        this.mShow = isShow;
        this.mTitleName = titleName;
    }

    @Override
    public View initView() {
        // 填充页面
        rootView = UIUtils.inflate(R.layout.layout_commonmember_page);
        mProgress_com_circular = (RelativeLayout) rootView.findViewById(R.id.progress_com_circular);
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
        //修改顶部title名称
        mTitle.setText(mTitleName);
        //initCommonInfo();
        if (mShow) {
            middle_page_3000.setVisibility(View.VISIBLE);
        } else {
            middle_page_3000.setVisibility(View.GONE);
        }
        getDataFromNet();
    }

    private void initCommonInfo() {
        mText = VIP.VIPNAME;
        if (UIUtils.isGoldVIP()) {
            if (TextUtils.equals(mType, Pass.CAN_PLAY)) {
                mText = VIP.GOLDNAME;
            } else if (TextUtils.equals(mType, Pass.CANNOT_PLAY)) {
                mText = VIP.DIAMONDNAME;
            }
        } else if (UIUtils.isDiamondVIP()) {
            if (TextUtils.equals(mType, Pass.CAN_PLAY)) {
                mText = VIP.DIAMONDNAME;
            } else if (TextUtils.equals(mType, Pass.CANNOT_PLAY)) {
                mText = VIP.BLACKDIAMONDNAME;
            }
        } else if (UIUtils.isBlackDiamondVIP()) {
            if (TextUtils.equals(mType, Pass.CAN_PLAY)) {
                mText = VIP.BLACKDIAMONDNAME;
            } else if (TextUtils.equals(mType, Pass.CANNOT_PLAY)) {
                mText = VIP.ROYALNAME;
            }
        } else if (UIUtils.isRoyalVIP()) {
            if (TextUtils.equals(mType, Pass.CAN_PLAY)) {
                mText = VIP.ROYALNAME;
            } else if (TextUtils.equals(mType, Pass.CANNOT_PLAY)) {
                mText = VIP.EXTREMENAME;
            }
        } else if (UIUtils.isExtremeVIP()) {
            if (TextUtils.equals(mType, Pass.CAN_PLAY)) {
                mText = VIP.EXTREMENAME;
            } else if (TextUtils.equals(mType, Pass.CANNOT_PLAY)) {
                mText = VIP.LIFELONGNAME;
            }
        } else if (UIUtils.isLifeLongVIP()) {
            if (TextUtils.equals(mType, Pass.CAN_PLAY)) {
                mText = VIP.LIFELONGNAME;
            } else if (TextUtils.equals(mType, Pass.CANNOT_PLAY)) {

            }
        }
        mTitle.setText(mText);
    }

    @Override
    public void onDestroy() {
        page = 1;
        mTotalPage = 1;
        mMyGridAdapter = null;
        mText = "";
    }

    private void getDataFromNet() {
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("User-Agent", "Mozilla/5.0 (Linux; Android ) Mobile huobaoapp");
        try {
            String url = mUrl + page + "&play=" + mType;
            OkHttpUtils.get().url(url)
                    .headers(stringHashMap)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Request request, Exception e) {
                    ShowToast.show(e.getMessage());
                }

                @Override
                public void onResponse(String response) {
                    mProgress_com_circular.setVisibility(View.GONE);
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
