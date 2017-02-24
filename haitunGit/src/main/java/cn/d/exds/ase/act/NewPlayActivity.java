package cn.d.exds.ase.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.opendanmaku.DanmakuItem;
import com.opendanmaku.DanmakuView;
import com.opendanmaku.IDanmakuItem;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import cn.d.exds.ase.R;
import cn.d.exds.ase.base.BaseActivity;
import cn.d.exds.ase.base.BaseApplication;
import cn.d.exds.ase.bean.DanMuDataBean;
import cn.d.exds.ase.net.AccessAddresses;
import cn.d.exds.ase.ui.CommonVideoView;
import cn.d.exds.ase.util.EncryptUtil;
import cn.d.exds.ase.util.GsonTools;
import cn.d.exds.ase.util.Logger;
import cn.d.exds.ase.util.PackageUtil;
import cn.d.exds.ase.util.Pass;
import cn.d.exds.ase.util.PassEvent;
import cn.d.exds.ase.util.SharedPreferencesUtils;
import cn.d.exds.ase.util.UIUtils;
import de.greenrobot.event.EventBus;

public class NewPlayActivity extends BaseActivity {

    CommonVideoView videoView;
    private String shikan;
    private String videoName;
    private String playUrl;
    public boolean mIsPrivateVideo;
    public String mVideoId;
    private RelativeLayout progressBar;

    @Override
    public void initView() {
        setContentView(R.layout.content_main);
        Intent intent = getIntent();
        videoName = intent.getStringExtra("videoName");
        playUrl = intent.getStringExtra("playUrl");
        shikan = intent.getStringExtra("shikan");
        mIsPrivateVideo = intent.getBooleanExtra("isPrivateVideo", false);
        mVideoId = intent.getStringExtra("videoId");
        videoView = (CommonVideoView) findViewById(R.id.common_videoView);
        mDanmakuView = (DanmakuView) findViewById(R.id.danmakuView);
        progressBar = (RelativeLayout) findViewById(R.id.progress_com_circular);
    }


    @Override
    public void initListener() {

    }

    @Override
    protected void onDestroy() {
        if (videoView.mTimeThread != null) {
            Logger.getInstance().e("qw", "NewPlayActivity.onDestroy.暂定线程");
            videoView.mTimeThread.interrupt();
        }
        if (videoView.handler != null) {
            Logger.getInstance().e("qw", "NewPlayActivity.onDestroy.handler");
            videoView.handler.removeCallbacks(videoView.run);
        }
        if (videoView.mHandler != null) {
            Logger.getInstance().e("qw", "NewPlayActivity.onDestroy.mHandler");
            videoView.mHandler.removeMessages(MSG_TIME_KEY);
            videoView.mHandler.removeMessages(MSG_HIDE_CONTROLOR);
            videoView.mHandler.removeMessages(START_MAIN_DIALOG);
        }
        if (videoView.videoHandler != null) {
            Logger.getInstance().e("qw", "NewPlayActivity.onDestroy.videoHandler");
            videoView.videoHandler.removeMessages(UPDATE_VIDEO_SEEKBAR);
        }
        finish();
        super.onDestroy();
    }

    private final int UPDATE_VIDEO_SEEKBAR = 1000;
    private static final int MSG_TIME_KEY = 1001;
    private static final int MSG_HIDE_CONTROLOR = 1002;
    private static final int START_MAIN_DIALOG = 1003;

    private String mJsonDomain;

    @Override
    public void initData() {
        mJsonDomain = getCorrectJsonDomain();
        videoView.start(playUrl, new CommonVideoView.OnPlayStateLinstener() {
            @Override
            public void gameOver() {
                playOver();
            }

            @Override
            public void gameContinue() {
                playContinue();
            }

            @Override
            public void gameBegin() {
                playBegin();
            }

            @Override
            public void gameError() {
                playError();
            }

            @Override
            public void gameClose() {
                playClose();
            }


            @Override
            public void gameThread(Thread thread) {
                if (thread != null) {
                    mThread = thread;
                }
            }

            @Override
            public void gameForceStop() {
                Logger.getInstance().e("qw", "NewPlayActivity.gameForceStop.强制停止");
                playOver();
            }
        }, videoName, shikan, mIsPrivateVideo);

        if (!TextUtils.isEmpty(mVideoId)) {
            long sjc = System.currentTimeMillis();
            String key = EncryptUtil.encodeVideoPlayPage(mVideoId, sjc);
            String tongjiUrl = mJsonDomain + AccessAddresses.dianjiTongJiUrl + PackageUtil.getInstallMills() + "&videoId=" + mVideoId
                    + "&sjc=" + sjc + "&key=" + key;
            OkHttpUtils.get().url(tongjiUrl).build().execute(new StringCallback() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    processData(response);
                }
            });
        }
    }

    private List<String> mContents = new ArrayList<>();
    private List<IDanmakuItem> mIDanmakuItems;
    private DanmakuView mDanmakuView;

    private List<IDanmakuItem> initItems(List<String> datas) {
        List<IDanmakuItem> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            IDanmakuItem item = new DanmakuItem(this, datas.get(i), 0, 0);
            item.setTextSize(20);
            item.setSpeedFactor(2);
            list.add(item);
        }
        return list;
    }

    private void processData(String data) {
        try {
            DanMuDataBean danMuDataBean = GsonTools.changeGsonToBean(data, DanMuDataBean.class);
            List<DanMuDataBean.ConBean> con = danMuDataBean.getCon();
            if (con != null && con.size() > 0) {
                for (int i = 0; i < con.size(); i++) {
                    mContents.add(con.get(i).getContent());
                }
                mIDanmakuItems = initItems(mContents);
                mDanmakuView.addItem(mIDanmakuItems, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void playClose() {
        NewPlayActivity.this.finish();
    }


    private void playError() {
        showErrorDialog();
    }

    private void playBegin() {
        progressBar.setVisibility(View.GONE);
        BaseApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDanmakuView.show();
            }
        }, 3000);
    }

    private void playContinue() {
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("温馨提示").setMessage("开通会员可以拖动观看完整视频").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EventBus.getDefault().post(new PassEvent(Pass.ALERTDIALOG));
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                    }
                }).show();
    }


    private void playOver() {
        NewPlayActivity.this.finish();
        EventBus.getDefault().post(new PassEvent(Pass.ALERTDIALOG));
    }


    private Thread mThread;

    private void showErrorDialog() {
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("错误提示").setMessage("对不起,无法播放此视频").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        NewPlayActivity.this.finish();
                    }
                }).show();
    }
}
