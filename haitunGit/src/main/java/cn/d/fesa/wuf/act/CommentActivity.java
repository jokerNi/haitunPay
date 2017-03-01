package cn.d.fesa.wuf.act;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.d.fesa.wuf.R;
import cn.d.fesa.wuf.adapter.CommentListAdapter;
import cn.d.fesa.wuf.adapter.CommentTopAdapter;
import cn.d.fesa.wuf.base.BaseActivity;
import cn.d.fesa.wuf.bean.CommenData;
import cn.d.fesa.wuf.util.Logger;
import cn.d.fesa.wuf.util.PassEvent;
import cn.d.fesa.wuf.net.AccessAddresses;
import cn.d.fesa.wuf.ui.NoScrollGridViewTwo;
import cn.d.fesa.wuf.util.GsonTools;
import cn.d.fesa.wuf.util.Pass;
import cn.d.fesa.wuf.util.SharedPreferencesUtils;
import cn.d.fesa.wuf.util.UIUtils;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import de.greenrobot.event.EventBus;

/**
 * Created by schwager on 2016/7/25.
 */
public class CommentActivity extends BaseActivity {

    private NoScrollGridViewTwo mCom_gv1;
    private NoScrollGridViewTwo mCom_gv2;
    private TextView mTv_name;
    private ImageView mIv_play;
    private String mVideoId;
    private String mUrl;
    private CommentTopAdapter mTopAdapter;
    private ScrollView mMain_srcoll;
    private RelativeLayout mProgress_com_circular;
    private CommentListAdapter mCommentListAdapter;
    private EditText mSearch_et_input;
    private ImageView mCom_iv_send;
    private String mJsonDomain;
    public String mPlayUrl;
    public String mVideoName;
    public String mPlayType;
    public boolean mIsPrivateVideo;

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

    @Override
    public void initView() {
        setContentView(R.layout.layout_commentactivity);
        //获取jsonDomain
        mJsonDomain = getCorrectJsonDomain();
        EventBus.getDefault().register(this);
        mMain_srcoll = (ScrollView) findViewById(R.id.main_srcoll);
        mCom_gv1 = (NoScrollGridViewTwo) findViewById(R.id.com_gv1);
        mCom_gv2 = (NoScrollGridViewTwo) findViewById(R.id.com_gv2);
        mTv_name = (TextView) findViewById(R.id.titlebar_tv_comment_name);
        mIv_play = (ImageView) findViewById(R.id.comment_iv_playvideo);
        getIntentData();
        mProgress_com_circular = (RelativeLayout) findViewById(R.id.progress_com_circular);
        mUrl = mJsonDomain + AccessAddresses.comment_Url;
        mSearch_et_input = (EditText) findViewById(R.id.search_et_input);
        mCom_iv_send = (ImageView) findViewById(R.id.com_iv_send);
    }

    private void getIntentData() {
        mVideoId = getIntent().getStringExtra("videoId");
        mPlayUrl = getIntent().getStringExtra("playUrl");
        mVideoName = getIntent().getStringExtra("videoName");
        mPlayType = getIntent().getStringExtra("playType");
        mIsPrivateVideo = getIntent().getBooleanExtra("isPrivateVideo", false);
    }

    @Override
    public void initListener() {
        mTv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentActivity.this.finish();
            }
        });
        mIv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.gotoDialogActivity();
            }
        });
        mCom_iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyBord();
                ifGoToVIP();
            }
        });
    }

    private void ifGoToVIP() {
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle("开通会员")
                .setMessage("开通会员才能发表评论")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        UIUtils.gotoDialogActivity();
                    }
                }).show();

    }

    public void onEventMainThread(PassEvent event) {
        int pay = event.getPassRessult();
        if (pay == Pass.CLOSE_COMMENTACTIVITY) {
            CommentActivity.this.finish();
        }
    }


    @Override
    public void initData() {
        getDataFromNet();
        if (TextUtils.equals(Pass.CAN_PLAY, mPlayType)) {
            UIUtils.movie(mPlayUrl, mVideoName, mVideoId, mIsPrivateVideo);
        } else if (TextUtils.equals(Pass.TRY_PLAY, mPlayType)) {
            UIUtils.playVideoTrySee(mPlayUrl, mVideoName, "true", mVideoId);
        }
    }

    private void getDataFromNet() {
        showProgressView();
        String url = mUrl + mVideoId;
        //Logger.getInstance().e("qw", "CommentActivity.getDataFromNet.= "+url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                processData(response);

            }
        });
    }

    private void dismissProgressView() {
        mProgress_com_circular.setVisibility(View.GONE);
    }

    private void showProgressView() {
        mProgress_com_circular.setVisibility(View.VISIBLE);
    }

    private void hideSoftKeyBord() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearch_et_input.getWindowToken(), 0);
    }

    private void processData(String data) {
        try {
            mMain_srcoll.smoothScrollTo(0, 0);
            CommenData commenData = GsonTools.changeGsonToBean(data, CommenData.class);
            mTv_name.setText(commenData.getTop_video().get(0).getTitle());
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIv_play.getLayoutParams();
            layoutParams.width = UIUtils.getWindowWidth();
            Picasso.with(UIUtils.getContext()).load(commenData.getOne_video().get(0).getPic()).transform(transformation).into(mIv_play);
            mTopAdapter = new CommentTopAdapter(UIUtils.getContext(), commenData.getTop_video());
            mTopAdapter.setOnitemVideoClick(new CommentTopAdapter.OnItemVideoClickListener() {
                @Override
                public void onItemClick(String msg) {
                    mVideoId = msg;
                    //getDataFromNet();
                    UIUtils.gotoDialogActivity();
                }
            });
            mCom_gv1.setAdapter(mTopAdapter);
            mCommentListAdapter = new CommentListAdapter(UIUtils.getContext(), commenData.getComment());
            mCom_gv2.setAdapter(mCommentListAdapter);
            dismissProgressView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Transformation transformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth = mIv_play.getWidth();
            if (source.getWidth() == 0) {
                return source;
            }
            //如果图片小于设置的宽度，则返回原图
            if (source.getWidth() < targetWidth) {
                return source;
            } else {
                //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                if (targetHeight != 0 && targetWidth != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                } else {
                    return source;
                }
            }
        }

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    };
}
