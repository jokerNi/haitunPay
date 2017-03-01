package cn.d.fesa.wuf.page;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.d.fesa.wuf.R;
import cn.d.fesa.wuf.adapter.PrivateVideoPageAdapter;
import cn.d.fesa.wuf.base.BasePage;
import cn.d.fesa.wuf.bean.PrivateVideoData;
import cn.d.fesa.wuf.bean.Users;
import cn.d.fesa.wuf.bean.Videos;
import cn.d.fesa.wuf.util.EncryptUtil;
import cn.d.fesa.wuf.util.GsonTools;
import cn.d.fesa.wuf.util.SharedPreferencesUtils;
import cn.d.fesa.wuf.util.ShowToast;
import cn.d.fesa.wuf.util.UIUtils;

/**
 * Created by BGFVG on 2017/1/20.
 */

public class PrivateVideoMemberPage extends BasePage {
    private View rootView;
    private GridView mGridView;
    private String mUrl;
    private int mPage = 1;
    private int mTotalPage;
    public PrivateVideoPageAdapter mAdapter;
    public String mOpenVideoCover;
    public TextView mTitlebar_tv_right;
    public RelativeLayout mProgress_com_circular;

    public PrivateVideoMemberPage(Context context, String url) {
        super(context);
        this.mUrl = url;
        Connector.getDatabase();
    }

    @Override
    public View initView() {
        rootView = UIUtils.inflate(R.layout.layout_video_page);
        mProgress_com_circular = (RelativeLayout) rootView.findViewById(R.id.progress_com_circular);
        mGridView = (GridView) rootView.findViewById(R.id.video_main_gv);
        mTitlebar_tv_right = (TextView) rootView.findViewById(R.id.tt_tv_right);
        mTitlebar_tv_right.setVisibility(View.VISIBLE);
        return rootView;
    }

    @Override
    public void initListener() {
        mTitlebar_tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/2/6 去一个已购买activity
                UIUtils.gotoHaveBeenPayedActivity();
            }
        });
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
        mTitle.setText(UIUtils.getContext().getResources().getText(R.string.privatearea));
        mOpenVideoCover = SharedPreferencesUtils.getString(UIUtils.getContext(), "openVideoCover", "");
        getDataFromNet();
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
            if (!TextUtils.equals(mOpenVideoCover, "true")) {
                //savePayedVideosBySP(privateVideoData);
                savePayedVideosByLitePal(privateVideoData);
            }
            mAdapter = new PrivateVideoPageAdapter(UIUtils.getContext(), privateVideoData.getCon(), new PrivateVideoPageAdapter.MyPageAdapterClickListener() {
                @Override
                public void click(String userId, String videoId, int videoNum, String name, String url, int pos, String desc) {
                    // TODO: 2017/1/24 购买videoId
                    UIUtils.gotoVideoPlayctivity(userId, videoId, videoNum, name, url, pos, desc);
                }
            }, false);
            mAdapter.setGrdiView(mGridView);
            mGridView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用litepal保存购买信息
     *
     * @param privateVideoData
     */
    private void savePayedVideosByLitePal(final PrivateVideoData privateVideoData) {
        new Thread() {
            @Override
            public void run() {
                List<String> users = privateVideoData.getUsers();
                List<String> videos = privateVideoData.getVideos();
                //Logger.getInstance().e("qw", "PrivateVideoMemberPage.run.users "+users.toString());
                //Logger.getInstance().e("qw", "PrivateVideoMemberPage.run.videos "+videos.toString());
                List<Users> usersList = new ArrayList<>();
                List<Videos> videosList = new ArrayList<>();
                if (users != null && users.size() > 0) {
                    for (String user : users) {
                        Users users1 = new Users();
                        users1.setUserId(user);
                        usersList.add(users1);
                    }
                }

                if (videos != null && videos.size() > 0) {
                    for (String video : videos) {
                        Videos video1 = new Videos();
                        video1.setVideoId(video);
                        videosList.add(video1);
                    }
                }

                if (usersList != null && usersList.size() > 0) {
                    DataSupport.deleteAll(Users.class);
                    DataSupport.saveAll(usersList);
                }

                if (videosList != null && videosList.size() > 0) {
                    DataSupport.deleteAll(Videos.class);
                    DataSupport.saveAll(videosList);
                }
            }
        }.start();
    }

    /**
     * 通过SP的形式保存信息
     *
     * @param privateVideoData
     */
    private void savePayedVideosBySP(PrivateVideoData privateVideoData) {
        List<String> users = privateVideoData.getUsers();
        List<String> videos = privateVideoData.getVideos();
        if (users != null && users.size() > 0) {
            for (String user : users) {
                SharedPreferencesUtils.saveBoolean(UIUtils.getContext(), "u" + user, true);
            }
        }
        if (videos != null && videos.size() > 0) {
            for (String video : videos) {
                SharedPreferencesUtils.saveBoolean(UIUtils.getContext(), "v" + video, true);
            }
        }
    }

    public Adapter getAdapter() {
        if (mAdapter == null) {
            return null;
        }
        return mAdapter;
    }


    @Override
    public void onDestroy() {
        mPage = 1;
        mTotalPage = 1;
        mAdapter = null;
    }
}
