package cn.d.sedfr.fhd.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import cn.d.sedfr.fhd.R;
import cn.d.sedfr.fhd.adapter.PrivateVideoPageAdapter;
import cn.d.sedfr.fhd.bean.PrivateVideoData;
import cn.d.sedfr.fhd.net.AccessAddresses;
import cn.d.sedfr.fhd.util.EncryptUtil;
import cn.d.sedfr.fhd.util.GsonTools;
import cn.d.sedfr.fhd.util.SharedPreferencesUtils;
import cn.d.sedfr.fhd.util.ShowToast;
import cn.d.sedfr.fhd.util.UIUtils;


/**
 */
@SuppressLint("NewApi")
public class AFragment extends Fragment {
    private View rootView;
    public GridView mGridView;
    public TextView mFragment_a_tv_no;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.a, container, false);
            mGridView = (GridView) rootView.findViewById(R.id.fragemnt_a_video_gv);
            mFragment_a_tv_no = (TextView) rootView.findViewById(R.id.fragment_a_tv_no);
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

    private String mJsonDomain;
    private String mUrl;
    private int mPage = 1;
    private int mTotalPage;
    public PrivateVideoPageAdapter mAdapter;

    private void initData() {
        mJsonDomain = getCorrectJsonDomain();
        mUrl = mJsonDomain + AccessAddresses.fragment_a_Url;
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
            key = EncryptUtil.encodePriateVideoPage(mPage, sjc);
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
                    if (mPage == 1 && mPage>0) {
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
            //Logger.getInstance().e("qw", "AFragment.loadMoreData.data=  "+data);
            PrivateVideoData privateVideoData = GsonTools.changeGsonToBean(data, PrivateVideoData.class);
            mPage = privateVideoData.getPage();
            mTotalPage = privateVideoData.getTotalPage();
            if (mAdapter != null) {
                List<PrivateVideoData.ConBean> con = privateVideoData.getCon();
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
            //Logger.getInstance().e("qw", "AFragment.processData.data=  "+data);
            PrivateVideoData privateVideoData = GsonTools.changeGsonToBean(data, PrivateVideoData.class);
            mPage = privateVideoData.getPage();
            mTotalPage = privateVideoData.getTotalPage();
            List<PrivateVideoData.ConBean> con = privateVideoData.getCon();
            if (con.size() > 0) {
                mAdapter = new PrivateVideoPageAdapter(UIUtils.getContext(), con, new PrivateVideoPageAdapter.MyPageAdapterClickListener() {
                    @Override
                    public void click(String userId, String videoId, int videoNum, String name, String url, int pos, String desc) {
                        // TODO: 2017/1/24 直接播放
                        // UIUtils.gotoVideoPlayctivity(userId, videoId, videoNum, name, url, pos, desc);
                        UIUtils.movie(url, name,videoId);
                    }
                }, true);
                mAdapter.setGrdiView(mGridView);
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
        mFragment_a_tv_no.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPage = 1;
        mTotalPage = 1;
        mAdapter = null;
    }
}
