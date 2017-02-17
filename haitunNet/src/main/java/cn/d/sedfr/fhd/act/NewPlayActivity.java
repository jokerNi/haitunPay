package cn.d.sedfr.fhd.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.d.sedfr.fhd.R;
import cn.d.sedfr.fhd.base.BaseActivity;
import cn.d.sedfr.fhd.net.AccessAddresses;
import cn.d.sedfr.fhd.ui.CommonVideoView;
import cn.d.sedfr.fhd.util.EncryptUtil;
import cn.d.sedfr.fhd.util.Logger;
import cn.d.sedfr.fhd.util.PackageUtil;
import cn.d.sedfr.fhd.util.Pass;
import cn.d.sedfr.fhd.util.PassEvent;
import cn.d.sedfr.fhd.util.SharedPreferencesUtils;
import cn.d.sedfr.fhd.util.UIUtils;
import de.greenrobot.event.EventBus;

public class NewPlayActivity extends BaseActivity {

    CommonVideoView videoView;
    private String shikan;
    private String videoName;
    private String playUrl;
    public boolean mIsPrivateVideo;
    public String mVideoId;

    @Override
    public void initView() {
        setContentView(R.layout.content_main);
        Intent intent = getIntent();
        videoName = intent.getStringExtra("videoName");
        playUrl = intent.getStringExtra("playUrl");
        shikan = intent.getStringExtra("shikan");
        mIsPrivateVideo = intent.getBooleanExtra("isPrivateVideo", false);
        mVideoId = intent.getStringExtra("videoId");
        mVp_iv_mark1 = (ImageView) findViewById(R.id.vp_iv_mark1);
        mVp_iv_mark2 = (ImageView) findViewById(R.id.vp_iv_mark2);
        mVp_iv_mark3 = (ImageView) findViewById(R.id.vp_iv_mark3);
        mVp_iv_mark4 = (ImageView) findViewById(R.id.vp_iv_mark4);
        mGold_iv_mark1 = (ImageView) findViewById(R.id.gold_iv_mark1);
        mGold_iv_mark2 = (ImageView) findViewById(R.id.gold_iv_mark2);
        mGold_iv_mark3 = (ImageView) findViewById(R.id.gold_iv_mark3);
        mGold_iv_mark4 = (ImageView) findViewById(R.id.gold_iv_mark4);
        videoView = (CommonVideoView) findViewById(R.id.common_videoView);
    }


    private ImageView mVp_iv_mark1;
    private ImageView mVp_iv_mark2;
    private ImageView mGold_iv_mark1;
    private ImageView mGold_iv_mark2;
    private ImageView mVp_iv_mark3;
    private ImageView mVp_iv_mark4;
    private ImageView mGold_iv_mark3;
    private ImageView mGold_iv_mark4;

    @Override
    public void initListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mThread != null) {
            mThread.interrupt();
        }
    }

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
                    // Logger.getInstance().e("qw", "NewPlayActivity.onResponse.= "+response);
                }
            });
        } else {
            //Logger.getInstance().e("qw", "NewPlayActivity.initData.没有videoId");
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
       /* SharedPreferencesUtils.saveBoolean(getApplicationContext(), "hasComplete", true);
        if ("true".equalsIgnoreCase(shikan)) {
            if (!UIUtils.isVIP()) {
                SharedPreferencesUtils.saveBoolean(getApplicationContext(), "try_over", true);
                NewPlayActivity.this.finish();
            }
        }
        if (UIUtils.isGoldVIP()) {
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), "gold_over", true);
            NewPlayActivity.this.finish();
        } else if (UIUtils.isDiamondVIP()) {
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), "diamond_over", true);
            NewPlayActivity.this.finish();
        } else if (UIUtils.isBlackDiamondVIP()) {
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), "blackdiamond_over", true);
            NewPlayActivity.this.finish();
        }*/
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
