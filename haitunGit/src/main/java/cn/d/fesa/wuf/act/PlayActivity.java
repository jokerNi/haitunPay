package cn.d.fesa.wuf.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.d.fesa.wuf.R;
import cn.d.fesa.wuf.ui.MyVideoView;
import cn.d.fesa.wuf.util.SharedPreferencesUtils;
import cn.d.fesa.wuf.util.UIUtils;

public class PlayActivity extends Activity {

    private static final int MSG_HIDE_CONTROLOR = 2;
    private static final int START_MAIN_DIALOG = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_HIDE_CONTROLOR:
                    hideControlor();
                    break;

                case START_MAIN_DIALOG:
                    startHomeActivity();
                    break;
            }
        }
    };
    private ImageView mVp_iv_mark1;
    private ImageView mVp_iv_mark2;
    private ImageView mGold_iv_mark1;
    private ImageView mGold_iv_mark2;
    private ImageView mVp_iv_mark3;
    private ImageView mVp_iv_mark4;
    private ImageView mGold_iv_mark3;
    private ImageView mGold_iv_mark4;

    private void startHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isShiKan", "true");
        startActivity(intent);
        finish();

    }

    private MyVideoView mm;
    private TextView name;
    private String videoName;
    private String playUrl;
    private boolean isContorlorShowing;
    private GestureDetector gestureDetector;
    private RelativeLayout ll_top;
    private ImageButton video_back;
    private RelativeLayout progress_circular;
    private String shikan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onDestroy() {
        //EventBus.getDefault().post(new PayEvent(111));
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferencesUtils.saveBoolean(getApplicationContext(), "hasComplete", false);
    }

    private void initListener() {

        mm.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progress_circular.setVisibility(View.GONE);
                mediaPlayer.start();
                name.setText(videoName);
                ll_top.setVisibility(View.VISIBLE);
                notifyHideControlor();
                //发送广播通知
                if ("true".equalsIgnoreCase(shikan)) {
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
                }

            }
        });

        // TODO: 2016/6/8 取消试看结束弹窗
        mm.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                       @Override
                                       public void onCompletion(MediaPlayer mediaPlayer) {
                                           SharedPreferencesUtils.saveBoolean(getApplicationContext(), "hasComplete", true);
                                           if ("true".equalsIgnoreCase(shikan)) {
                                               if (!UIUtils.isVIP()) {
                                                   SharedPreferencesUtils.saveBoolean(getApplicationContext(), "try_over", true);
                                                   PlayActivity.this.finish();
                                               }
                                           }
                                           if (UIUtils.isGoldVIP()) {
                                               SharedPreferencesUtils.saveBoolean(getApplicationContext(), "gold_over", true);
                                               PlayActivity.this.finish();
                                           } else if (UIUtils.isDiamondVIP()) {
                                               SharedPreferencesUtils.saveBoolean(getApplicationContext(), "diamond_over", true);
                                               PlayActivity.this.finish();
                                           } else if (UIUtils.isBlackDiamondVIP()) {
                                               SharedPreferencesUtils.saveBoolean(getApplicationContext(), "blackdiamond_over", true);
                                               PlayActivity.this.finish();
                                           }
                                       }
                                   }

        );

        mm.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                  @Override
                                  public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//                progress_circular.setVisibility(View.GONE);
                                      showErrorDialog();
                                      return true;
                                  }
                              }

        );

        gestureDetector = new

                GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // 隐藏/显示控制面板
                switchControlor();
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }


        }

        );

        video_back.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              PlayActivity.this.finish();
                                          }
                                      }

        );
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this).setTitle("错误提示").setMessage("对不起,无法播放此视频").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        PlayActivity.this.finish();
                    }
                }).show();
    }

    /*private void sendCast() {
        Intent intent2 = new Intent();
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra("over", "over");
        intent2.setAction("com.qu.huobao");
        this.sendBroadcast(intent2);
    }*/


    /**
     * 如果面板正在显示，则隐藏面板;如果面板已被隐藏，则将其显示出来
     */
    private void switchControlor() {
        if (isContorlorShowing) {
            // 显示状态,需要隐藏控制面板
            hideControlor();
        } else {
            // 隐藏状态，需要显示控制面板
            showControlor();
            notifyHideControlor();
        }
    }

    private void notifyHideControlor() {
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLOR, 2500);

    }

    private void hideControlor() {
        ll_top.setVisibility(View.GONE);
        isContorlorShowing = false;


    }

    private void showControlor() {
        ll_top.setVisibility(View.VISIBLE);
        isContorlorShowing = true;

    }

    private void initData() {
        //真实的播放地址
        Uri uri = Uri.parse(playUrl);
        mm.setVideoURI(uri);
        if ("true".equalsIgnoreCase(shikan)) {
            mm.setMediaController(new MediaController(this));
        } else {
            mm.setMediaController(new MediaController(this));
        }
    }

    private void initView() {
        Intent intent = getIntent();
        videoName = intent.getStringExtra("videoName");
        playUrl = intent.getStringExtra("playUrl");
        shikan = intent.getStringExtra("shikan");
        mm = (MyVideoView) findViewById(R.id.mm);
        name = (TextView) findViewById(R.id.moviename);
        mVp_iv_mark1 = (ImageView) findViewById(R.id.vp_iv_mark1);
        mVp_iv_mark2 = (ImageView) findViewById(R.id.vp_iv_mark2);
        mVp_iv_mark3 = (ImageView) findViewById(R.id.vp_iv_mark3);
        mVp_iv_mark4 = (ImageView) findViewById(R.id.vp_iv_mark4);
        mGold_iv_mark1 = (ImageView) findViewById(R.id.gold_iv_mark1);
        mGold_iv_mark2 = (ImageView) findViewById(R.id.gold_iv_mark2);
        mGold_iv_mark3 = (ImageView) findViewById(R.id.gold_iv_mark3);
        mGold_iv_mark4 = (ImageView) findViewById(R.id.gold_iv_mark4);
        ll_top = (RelativeLayout) findViewById(R.id.ll_top);
        progress_circular = (RelativeLayout) findViewById(R.id.progress_circular);
        video_back = (ImageButton) findViewById(R.id.video_back);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
