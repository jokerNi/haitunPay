package cn.d.yurfe.bgc.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ImageView;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.base.BaseActivity;
import cn.d.yurfe.bgc.ui.CommonVideoView;
import cn.d.yurfe.bgc.util.Logger;
import cn.d.yurfe.bgc.util.Pass;
import cn.d.yurfe.bgc.util.PassEvent;
import cn.d.yurfe.bgc.util.SharedPreferencesUtils;
import cn.d.yurfe.bgc.util.UIUtils;
import de.greenrobot.event.EventBus;

public class NewPlayActivity extends BaseActivity {

    CommonVideoView videoView;
    private String shikan;
    private String videoName;
    private String playUrl;
    private String mTestUrl;

    @Override
    public void initView() {
        setContentView(R.layout.content_main);
        Intent intent = getIntent();
        videoName = intent.getStringExtra("videoName");
        playUrl = intent.getStringExtra("playUrl");
        shikan = intent.getStringExtra("shikan");
        mVp_iv_mark1 = (ImageView) findViewById(R.id.vp_iv_mark1);
        mVp_iv_mark2 = (ImageView) findViewById(R.id.vp_iv_mark2);
        mVp_iv_mark3 = (ImageView) findViewById(R.id.vp_iv_mark3);
        mVp_iv_mark4 = (ImageView) findViewById(R.id.vp_iv_mark4);
        mGold_iv_mark1 = (ImageView) findViewById(R.id.gold_iv_mark1);
        mGold_iv_mark2 = (ImageView) findViewById(R.id.gold_iv_mark2);
        mGold_iv_mark3 = (ImageView) findViewById(R.id.gold_iv_mark3);
        mGold_iv_mark4 = (ImageView) findViewById(R.id.gold_iv_mark4);
        videoView = (CommonVideoView) findViewById(R.id.common_videoView);
        mTestUrl = "http://data.vod.itc.cn/?rb=1&prot=1&key=jbZhEJhlqlUN-Wj_HEI8BjaVqKNFvDrn&prod=flash&pt=1&new=/226/36/ClaEIbpyTzepDy2xJ9WjBB.mp4";
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
        /*if ("true".equalsIgnoreCase(shikan)) {
            mVp_iv_mark2.setVisibility(View.VISIBLE);
            mVp_iv_mark1.setVisibility(View.VISIBLE);
            mVp_iv_mark3.setVisibility(View.VISIBLE);
            mVp_iv_mark4.setVisibility(View.VISIBLE);
        } else {
            mVp_iv_mark2.setVisibility(View.INVISIBLE);
            mVp_iv_mark1.setVisibility(View.INVISIBLE);
            mVp_iv_mark3.setVisibility(View.INVISIBLE);
            mVp_iv_mark4.setVisibility(View.INVISIBLE);
        }
        if (UIUtils.isGoldVIP() && !UIUtils.isDiamondVIP()) {
            mGold_iv_mark1.setVisibility(View.VISIBLE);
            mGold_iv_mark2.setVisibility(View.VISIBLE);
            mGold_iv_mark3.setVisibility(View.VISIBLE);
            mGold_iv_mark4.setVisibility(View.VISIBLE);
        } else {
            mGold_iv_mark1.setVisibility(View.INVISIBLE);
            mGold_iv_mark2.setVisibility(View.INVISIBLE);
            mGold_iv_mark3.setVisibility(View.INVISIBLE);
            mGold_iv_mark4.setVisibility(View.INVISIBLE);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mThread != null) {
            mThread.interrupt();
        }
    }

    @Override
    public void initData() {
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
        }, videoName, shikan);
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
