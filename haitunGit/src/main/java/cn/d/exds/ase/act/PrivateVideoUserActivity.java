package cn.d.exds.ase.act;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import cn.d.exds.ase.R;
import cn.d.exds.ase.adapter.PrivateVideoActivityAdapter;
import cn.d.exds.ase.base.BaseActivity;
import cn.d.exds.ase.bean.PrivateVideoData;
import cn.d.exds.ase.net.AccessAddresses;
import cn.d.exds.ase.util.EncryptUtil;
import cn.d.exds.ase.util.GsonTools;
import cn.d.exds.ase.util.Pass;
import cn.d.exds.ase.util.PassEvent;
import cn.d.exds.ase.util.SharedPreferencesUtils;
import cn.d.exds.ase.util.ShowToast;
import cn.d.exds.ase.util.UIUtils;
import de.greenrobot.event.EventBus;

public class PrivateVideoUserActivity extends BaseActivity {

    public String mUserId;
    public String mUserName;
    public GridView mGridView;
    private String mUrl;
    private int mPage = 1;
    private int mTotalPage;
    public PrivateVideoActivityAdapter mAdapter;
    private String mJsonDomain;
    public int mPagePos;
    public boolean mHavePay;
    public RelativeLayout mProgress_com_circular;

    @Override
    public void initView() {
        setContentView(R.layout.activity_private_video_user);
        mProgress_com_circular = (RelativeLayout) findViewById(R.id.progress_com_circular);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        mUserId = intent.getStringExtra("userId");
        mUserName = intent.getStringExtra("userName");
        mPagePos = intent.getIntExtra("pagePos", -1);
        mHavePay = intent.getBooleanExtra("havePay", false);
        mJsonDomain = getCorrectJsonDomain();
        mUrl = mJsonDomain + AccessAddresses.confidential_Url;
        intiTitleViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(PassEvent event) {
        if (event.getPassRessult() == Pass.FLUSH_ITEM_Activity) {
            flushItem(event);
        }
    }

    private void flushItem(PassEvent passEvent) {
        mAdapter.updateItemDataNew(passEvent.getPos(), passEvent.getDesc(), passEvent.getUserId(), passEvent.isFlag());
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

    private void intiTitleViews() {
        ImageButton titlebar_ib_left = (ImageButton) findViewById(R.id.titlebar_ib_left);
        ImageButton titlebar_ib_right = (ImageButton) findViewById(R.id.titlebar_ib_right);
        TextView titlebar_iv_title = (TextView) findViewById(R.id.titlebar_tv_title);
        titlebar_ib_left.setVisibility(View.GONE);
        titlebar_ib_right.setVisibility(View.GONE);
        titlebar_iv_title.setText(mUserName);
        mGridView = (GridView) findViewById(R.id.video_list_gv);
    }

    @Override
    public void initListener() {
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

    @Override
    public void initData() {
        getDataFromNet();
    }

    private void getDataFromNet() {
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("User-Agent", "Mozilla/5.0 (Linux; Android ) Mobile huobaoapp");
        try {
            String key = "";
            long sjc = System.currentTimeMillis();
            key = EncryptUtil.encodePriateVideoPage(mPage, sjc, mUserId);
            String url = mUrl + mPage + "&userId=" + mUserId + "&sjc=" + sjc + "&key=" + key;
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
                    if (mPage == 1) {
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

    private void loadMoreData(String data) {
        try {
            PrivateVideoData privateVideoData = GsonTools.changeGsonToBean(data, PrivateVideoData.class);
            mPage = privateVideoData.getPage();
            mTotalPage = privateVideoData.getTotalPage();
            if (mAdapter != null) {
                mAdapter.addList(privateVideoData.getCon());
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void processData(String data) {
        try {
            PrivateVideoData privateVideoData = GsonTools.changeGsonToBean(data, PrivateVideoData.class);
            mPage = privateVideoData.getPage();
            mTotalPage = privateVideoData.getTotalPage();
            mAdapter = new PrivateVideoActivityAdapter(UIUtils.getContext(), privateVideoData.getCon(), mPagePos, mHavePay, new PrivateVideoActivityAdapter.MyPageAdapterClickListener() {
                @Override
                public void click(String userId, String videoId, int videoNum, String name, String url, int pos, String desc, int pagePos) {
                    UIUtils.gotoVideoPlayctivity(userId, videoId, videoNum, name, url, pos, desc, pagePos);
                }
            });
            mAdapter.setGrdiView(mGridView);
            mGridView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
