package cn.d.yurfe.bgc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.adapter.FragmentBPageAdapter;
import cn.d.yurfe.bgc.adapter.PrivateVideoPageAdapter;
import cn.d.yurfe.bgc.bean.FragmentBData;
import cn.d.yurfe.bgc.bean.PrivateVideoData;
import cn.d.yurfe.bgc.net.AccessAddresses;
import cn.d.yurfe.bgc.util.EncryptUtil;
import cn.d.yurfe.bgc.util.GsonTools;
import cn.d.yurfe.bgc.util.Logger;
import cn.d.yurfe.bgc.util.SharedPreferencesUtils;
import cn.d.yurfe.bgc.util.ShowToast;
import cn.d.yurfe.bgc.util.UIUtils;

/**
 * 项目名称：FragmentManager 类名称：Spec比
 * ialFragment 类描述： 创建人：lc 创建时间：2015-2-11 下午4:18:27
 * 修改人：131 修改时间：2015-2-11 下午4:18:27 修改备注：
 */
public class BFragment extends Fragment {
    private View rootView;
    public GridView mGridView;
    public TextView mFragment_b_tv_no;
    private String mJsonDomain;
    private String mUrl;
    private int mPage=1;
    private FragmentBPageAdapter mAdapter;
    private int mTotalPage;


    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.b, container, false);
            mGridView = (GridView) rootView.findViewById(R.id.fragemnt_b_video_gv);
            mFragment_b_tv_no = (TextView) rootView.findViewById(R.id.fragment_b_tv_no);
        } else {
            // 防止重复加载，出现闪退
            if (null != rootView) {
                ViewGroup parent = (ViewGroup) rootView.getParent();
                if (null != parent) {
                    parent.removeView(rootView);
                }
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        initData();
    }

    private void initListener() {
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (mAdapter != null) {
                    int lastIndex = mAdapter.getCount() - 1;
                    int lastVisiblePostion = mGridView.getLastVisiblePosition();
                    if (lastIndex == lastVisiblePostion && mPage < mTotalPage) {
                        mPage = mPage + 1;
                        getDataFromNet();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    private void initData() {
        mJsonDomain = getCorrectJsonDomain();
        mUrl = mJsonDomain + AccessAddresses.fragment_b_Url;
        getDataFromNet();
    }

    private String getCorrectJsonDomain() {
        String jsonDomain = SharedPreferencesUtils.getString(UIUtils.getContext(), "jsonDomain", "");
        if (!TextUtils.isEmpty(jsonDomain)) {
            if (jsonDomain.startsWith("http://")) {
                if (jsonDomain.endsWith("/")) {
                    if (jsonDomain.contains(".")) {
                        return jsonDomain;
                    } else {
                    }
                } else {
                }
            } else {
            }
        } else {
        }
        return AccessAddresses.final_JsonDomain;
    }

    private void getDataFromNet() {
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("User-Agent", "Mozilla/5.0 (Linux; Android ) Mobile huobaoapp");
        try {
            String key = "";
            long sjc = System.currentTimeMillis();
            key = EncryptUtil.encode(mPage, sjc);
            String url = mUrl + mPage + "&sjc=" + sjc + "&key=" + key;
            OkHttpUtils.get().url(url)
                    .headers(stringHashMap)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Request request, Exception e) {
                    ShowToast.show(e.getMessage());
                }

                @Override
                public void onResponse(String response) {
                    if (mPage == 1 ) {
                        processData(response);
                    } else {
                        loadMoreData(response);
                    }
                }
            });
        } catch (Exception e) {
            showIf();
            e.printStackTrace();
        }
    }

    private void loadMoreData(String data) {
        try {
            FragmentBData fragmentBData = GsonTools.changeGsonToBean(data, FragmentBData.class);
            mPage = fragmentBData.getPage();
            mTotalPage = fragmentBData.getTotalPage();
            if (mAdapter != null) {
                List<FragmentBData.ConBean> con = fragmentBData.getCon();
                mAdapter.addList(con);
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            showIf();
            e.printStackTrace();
        }
    }

    private void processData(String data) {
        try {
            FragmentBData fragmentBData = GsonTools.changeGsonToBean(data, FragmentBData.class);
            mPage = fragmentBData.getPage();
            mTotalPage = fragmentBData.getTotalPage();
            List<FragmentBData.ConBean> con = fragmentBData.getCon();
            if (con.size() > 0) {
                mAdapter = new FragmentBPageAdapter(UIUtils.getContext(), con);
                mGridView.setAdapter(mAdapter);
            } else {
                showIf();
            }
        } catch (Exception e) {
            showIf();
            e.printStackTrace();
        }
    }

    private void showIf() {
        mGridView.setVisibility(View.GONE);
        mFragment_b_tv_no.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPage = 1;
        mTotalPage = 1;
        mAdapter = null;
    }
}
