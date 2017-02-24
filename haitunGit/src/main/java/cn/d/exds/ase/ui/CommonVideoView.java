package cn.d.exds.ase.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.d.exds.ase.R;
import cn.d.exds.ase.util.Logger;
import cn.d.exds.ase.util.SharedPreferencesUtils;
import cn.d.exds.ase.util.ShowToast;
import cn.d.exds.ase.util.UIUtils;

import static cn.d.exds.ase.R.id.videoPlayImg;


/**
 * qiangyu on 1/26/16 15:33
 * csdn博客:http://blog.csdn.net/yissan
 */
public class CommonVideoView extends FrameLayout implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, View.OnTouchListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, Animator.AnimatorListener {

    private final int UPDATE_VIDEO_SEEKBAR = 1000;

    private Context context;
    private RelativeLayout viewBox;
    private MyVideoView videoView;
    private LinearLayout videoPauseBtn;
    private LinearLayout videoControllerBottom;
    private ImageView videoPauseImg;
    private TextView videoCurTimeText;
    private TextView videoTotalTimeText;
    private SeekBar videoSeekBar;

    private RelativeLayout progressBar;

    private int duration;
    private String formatTotalTime;

    private Timer timer = new Timer();

    private float touchLastX;
    //定义用seekBar当前的位置，触摸快进的时候显示时间
    private int position;
    private int touchStep = 1000;//快进的时间，1秒
    private int touchPosition = -1;

    private boolean videoControllerShow = true;//底部状态栏的显示状态
    private boolean animation = false;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            videoHandler.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);
        }
    };

    public Handler videoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VIDEO_SEEKBAR:
                    if (videoView.isPlaying()) {
                        videoSeekBar.setProgress(videoView.getCurrentPosition());
                    }
                    break;
            }
        }
    };
    private int[] mTime;
    private ImageButton mVideo_back;
    public TimeThread mTimeThread;
    private Random mRandom;
    private long mStartTime;
    private long mRealStartTime;
    public int mStartProgress;
    public RelativeLayout mProgress_com_circular;
    public long mFreeMills;


    public CommonVideoView(Context context) {
        this(context, null);
    }

    public CommonVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
    }

    public void start(String url) {
        videoPauseBtn.setEnabled(false);
        videoSeekBar.setEnabled(false);
        videoView.setVideoURI(Uri.parse(url));
        videoView.start();
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        this.animation = false;
        this.videoControllerShow = !this.videoControllerShow;
        isVideoBoxHaveClick = false;
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    public interface OnPlayStateLinstener {
        void gameOver();

        void gameContinue();

        void gameBegin();

        void gameError();

        void gameClose();

        void gameThread(Thread thread);

        void gameForceStop();
    }

    private OnPlayStateLinstener mOnPlayStateLinstener;

    private String mName;
    private String mShiKan;
    private boolean isPrivateVideo;

    public void start(String url, OnPlayStateLinstener onPlayStateLinstener, String name, String shikan, boolean flag) {
        mStartTime = System.currentTimeMillis();
        this.mOnPlayStateLinstener = onPlayStateLinstener;
        this.mName = name;
        this.mShiKan = shikan;
        this.isPrivateVideo = flag;
        videoPauseBtn.setEnabled(false);
        videoSeekBar.setEnabled(false);
        videoView.setVideoURI(Uri.parse(url));
        moviename.setText(mName);
        videoView.start();
        mFreeMills = SharedPreferencesUtils.getInt(UIUtils.getContext(), "freeMills", 0);
    }

    private int newDuration = 0;

    public Handler handler = new Handler();
    private int currentPosition;
    public Runnable run = new Runnable() {
        public void run() {
            // 获得当前播放时间和当前视频的长度
            currentPosition = videoView.getCurrentPosition();
            if (TextUtils.equals(mShiKan, "true")) {
                if (currentPosition >= mFreeMills) {
                    mOnPlayStateLinstener.gameForceStop();
                }
            }
            handler.postDelayed(run, 500);
        }
    };

    @Override
    public void onPrepared(MediaPlayer mp) {
        duration = videoView.getDuration();
        mRandom = new Random(duration);
        dealDuration();
        if (UIUtils.isLifeLongVIP() || isPrivateVideo) {
            mTime = getMinuteAndSecond(duration);
        } else {
            mTime = getMinuteAndSecond(newDuration);
        }

        videoTotalTimeText.setText(String.format("%02d:%02d", mTime[0], mTime[1]));
        formatTotalTime = String.format("%02d:%02d", mTime[0], mTime[1]);
        if (UIUtils.isLifeLongVIP() || isPrivateVideo) {
            videoSeekBar.setMax(duration);
        } else {
            videoSeekBar.setMax(newDuration);
        }
        mp.start();
        mRealStartTime = System.currentTimeMillis();
        isVideoBoxHaveClick = false;
        hideControlerByAnimator(0);
        showControlor();
        showControlerByAnimator(500);
        addWaterMark();
        videoPauseBtn.setEnabled(true);
        videoSeekBar.setEnabled(true);
        videoPauseImg.setImageResource(R.drawable.icon_video_pause);
        timer.schedule(timerTask, 0, 1000);
        if (mFreeMills != 0) {
            handler.post(run);
        }
        notifyPlayActivityGameBegin();
    }

    private void dealDuration() {
        int max = 5400000;
        int min = 3600000;
        newDuration = mRandom.nextInt(max) % (max - min + 1) + min;
    }


    public void setFullScreen() {
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        videoView.requestLayout();
    }

    public void setNormalScreen() {
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
        videoView.requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
        initData();
    }


    private RelativeLayout videoControlTop;
    private TextView moviename;


    private ImageView mVp_iv_mark1;
    private ImageView mVp_iv_mark2;
    private ImageView mGold_iv_mark1;
    private ImageView mGold_iv_mark2;
    private ImageView mVp_iv_mark3;
    private ImageView mVp_iv_mark4;
    private ImageView mGold_iv_mark3;
    private ImageView mGold_iv_mark4;

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.common_video_view, null);
        viewBox = (RelativeLayout) view.findViewById(R.id.viewBox);
        videoView = (MyVideoView) view.findViewById(R.id.videoView);
        videoControlTop = (RelativeLayout) view.findViewById(R.id.ll_top);
        mVideo_back = (ImageButton) view.findViewById(R.id.video_back);
        moviename = (TextView) view.findViewById(R.id.moviename);

        mVp_iv_mark1 = (ImageView) view.findViewById(R.id.vp_iv_mark1);
        mVp_iv_mark2 = (ImageView) view.findViewById(R.id.vp_iv_mark2);
        mVp_iv_mark3 = (ImageView) view.findViewById(R.id.vp_iv_mark3);
        mVp_iv_mark4 = (ImageView) view.findViewById(R.id.vp_iv_mark4);
        mGold_iv_mark1 = (ImageView) view.findViewById(R.id.gold_iv_mark1);
        mGold_iv_mark2 = (ImageView) view.findViewById(R.id.gold_iv_mark2);
        mGold_iv_mark3 = (ImageView) view.findViewById(R.id.gold_iv_mark3);
        mGold_iv_mark4 = (ImageView) view.findViewById(R.id.gold_iv_mark4);

        videoPauseBtn = (LinearLayout) view.findViewById(R.id.videoPauseBtn);
        videoControllerBottom = (LinearLayout) view.findViewById(R.id.videoControllerLayout);
        videoCurTimeText = (TextView) view.findViewById(R.id.videoCurTime);
        videoTotalTimeText = (TextView) view.findViewById(R.id.videoTotalTime);
        videoSeekBar = (SeekBar) view.findViewById(R.id.videoSeekBar);
        videoPauseImg = (ImageView) view.findViewById(R.id.videoPauseImg);
        //mProgress_com_circular = (RelativeLayout) view.findViewById(R.id.progress_com_circular);
        mVideo_back.setOnClickListener(this);
        videoPauseBtn.setOnClickListener(this);
        videoSeekBar.setOnSeekBarChangeListener(this);
        videoPauseBtn.setOnClickListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        //注册在设置或播放过程中发生错误时调用的回调函数。如果未指定回调函数，或回调函数返回false，VideoView 会通知用户发生了错误。
        videoView.setOnErrorListener(this);
        viewBox.setOnTouchListener(this);
        viewBox.setOnClickListener(this);
        addView(view);
    }

    private boolean isVideoBoxHaveClick = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoPauseBtn:
                if (videoView.isPlaying()) {
                    videoView.pause();
                    videoPauseImg.setImageResource(R.drawable.icon_video_play);
                } else {
                    videoView.start();
                    videoPauseImg.setImageResource(R.drawable.icon_video_pause);
                }
                break;
            case R.id.viewBox:
                // 隐藏/显示控制面板
                if (!isVideoBoxHaveClick) {
                    long clickBoxTime = System.currentTimeMillis();
                    if ((clickBoxTime - mRealStartTime > 1000)) {
                        switchControlorByAnimator();
                        isVideoBoxHaveClick = true;
                    } else {
                    }

                } else {
                }
                break;
            case R.id.video_back:
                notifyPlayActivityGameClose();
                break;

            default:
                break;
        }
    }

    private void switchControlorByAnimator() {
        if (!animation && videoControllerShow) {
            hideControlerByAnimator(500);
        } else if (!animation) {
            showControlerByAnimator(500);
        }
    }

    private void showControlerByAnimator(long dura1) {
        float curY_videoControllerBottom = videoControllerBottom.getY();
        float curY_videoControlTop = videoControlTop.getY();
        animation = true;
        ObjectAnimator animatorVideoControllerBottom = ObjectAnimator.ofFloat(videoControllerBottom, "y",
                curY_videoControllerBottom, curY_videoControllerBottom - videoControllerBottom.getHeight());
        ObjectAnimator animatorVideoControlTop = ObjectAnimator.ofFloat(videoControlTop, "y",
                curY_videoControlTop, curY_videoControlTop + videoControlTop.getHeight());
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(animatorVideoControllerBottom, animatorVideoControlTop);
        animatorSet2.setDuration(dura1);
        animatorSet2.addListener(this);
        animatorSet2.start();

        notifyHideControlor();
    }

    private void hideControlerByAnimator(long dura2) {
        float curY_videoControllerBottom = videoControllerBottom.getY();
        float curY_videoControlTop = videoControlTop.getY();
        animation = true;
        ObjectAnimator animatorVideoControllerBottom = ObjectAnimator.ofFloat(videoControllerBottom, "y",
                curY_videoControllerBottom, curY_videoControllerBottom + videoControllerBottom.getHeight());
        ObjectAnimator animatorVideoControlTop = ObjectAnimator.ofFloat(videoControlTop, "y",
                curY_videoControlTop, curY_videoControlTop - videoControlTop.getHeight());
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(animatorVideoControllerBottom, animatorVideoControlTop);
        animatorSet1.setDuration(dura2);
        animatorSet1.addListener(this);
        animatorSet1.start();
    }


    private void initData() {
        mTimeThread = new TimeThread();
        mTimeThread.start();
    }


    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = MSG_TIME_KEY;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private static final int MSG_TIME_KEY = 1001;
    private static final int MSG_HIDE_CONTROLOR = 1002;
    private static final int START_MAIN_DIALOG = 1003;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HIDE_CONTROLOR:
//                    hideControlor();
                    if (!animation && videoControllerShow && !isSeekbarMove) {
                        hideControlerByAnimator(500);
                    }
                    break;
                case START_MAIN_DIALOG:
                    break;
                case MSG_TIME_KEY:
                    setSystemTime();
                    break;
                default:
                    break;
            }
        }
    };

    private void setSystemTime() {
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);
    }

    private boolean isContorlorShowing;

    private void notifyHideControlor() {
        //发消息
        Message msg = new Message();
        msg.what = MSG_HIDE_CONTROLOR;
        mHandler.sendMessageDelayed(msg, 3000);
    }

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

    private void hideControlor() {
        if (!isSeekbarMove) {
            videoControlTop.setVisibility(View.GONE);
            videoControllerBottom.setVisibility(View.GONE);
            isContorlorShowing = false;
        }
    }

    private void showControlor() {
        videoControlTop.setVisibility(View.VISIBLE);
        videoControllerBottom.setVisibility(View.VISIBLE);
        isContorlorShowing = true;
    }


    private void addWaterMark() {
        if (!isPrivateVideo) {
            if ("true".equalsIgnoreCase(mShiKan)) {
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
    }

    private int genSecond() {
        int max = 59;
        int min = 0;
        return mRandom.nextInt(max) % (max - min + 1) + min;
    }

    private int genMinute() {
        int max = 90;
        int min = 60;
        return mRandom.nextInt(max) % (max - min + 1) + min;
    }

    private int[] getMinuteAndSecond(int mils) {
        mils /= 1000;
        int[] time = new int[2];
        time[0] = mils / 60;
        time[1] = mils % 60;
        return time;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        videoView.seekTo(0);
        videoPauseImg.setImageResource(R.drawable.icon_video_play);
        notifyPlayActivityGameOver();
        notifyPlayActivityGameThread(mTimeThread);
    }

    private void notifyPlayActivityGameBegin() {
        if (this.mOnPlayStateLinstener != null) {
            mOnPlayStateLinstener.gameBegin();
        }
    }


    private void notifyPlayActivityGameOver() {
        if (this.mOnPlayStateLinstener != null) {
            mOnPlayStateLinstener.gameOver();
        }
    }


    private void notifyPlayActivityGameContinue() {
        if (this.mOnPlayStateLinstener != null) {
            mOnPlayStateLinstener.gameContinue();
        }
    }

    private void notifyPlayActivityGameError() {
        if (this.mOnPlayStateLinstener != null) {
            mOnPlayStateLinstener.gameError();
        }
    }

    private void notifyPlayActivityGameClose() {
        if (this.mOnPlayStateLinstener != null) {
            mOnPlayStateLinstener.gameClose();
        }
    }

    private void notifyPlayActivityGameThread(Thread thread) {
        if (this.mOnPlayStateLinstener != null) {
            mOnPlayStateLinstener.gameThread(thread);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        notifyPlayActivityGameError();
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        long touchTime = System.currentTimeMillis();
        if ((touchTime - mStartTime > 2000)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!videoView.isPlaying()) {
                        return false;
                    }
                    float downX = event.getRawX();
                    touchLastX = downX;
                    this.position = videoView.getCurrentPosition();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!videoView.isPlaying()) {
                        return false;
                    }
                    float currentX = event.getRawX();
                    float deltaX = currentX - touchLastX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (deltaXAbs > 1) {
                        touchLastX = currentX;
                        if (deltaX > 1) {
                            position += touchStep;
                            if (position > duration) {
                                position = duration;
                            }
                            touchPosition = position;
                            int[] time = getMinuteAndSecond(position);
                        } else if (deltaX < -1) {
                            position -= touchStep;
                            if (position < 0) {
                                position = 0;
                            }
                            touchPosition = position;
                            int[] time = getMinuteAndSecond(position);
                            //mVideoView.seekTo(position);
                        }
                    } else {
                        //Logger.getInstance().e("qw", "CommonVideoView.onTouch.禁止触摸");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (touchPosition != -1) {
                        //videoView.seekTo(touchPosition);
                        touchPosition = -1;
                        if (videoControllerShow) {
                            return true;
                        }
                    }
                    break;
            }
        }
        return false;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int[] time = getMinuteAndSecond(progress);
        videoCurTimeText.setText(String.format("%02d:%02d", time[0], time[1]));
    }

    private boolean isSeekbarMove = false;

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isSeekbarMove = true;
        videoView.pause();
        mStartProgress = seekBar.getProgress();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeekbarMove = false;
        notifyHideControlor();
        if (UIUtils.isLifeLongVIP() || isPrivateVideo) {
            Logger.getInstance().e("qw", "CommonVideoView.onStopTrackingTouch.可以移动");
            videoView.seekTo(seekBar.getProgress());
            videoView.start();
            videoPauseImg.setImageResource(R.drawable.icon_video_pause);
        } else {
            Logger.getInstance().e("qw", "CommonVideoView.onStopTrackingTouch.不能移动");
            ShowToast.show("只有会员能拖动视频");
            videoView.start();
        }
    }
}
