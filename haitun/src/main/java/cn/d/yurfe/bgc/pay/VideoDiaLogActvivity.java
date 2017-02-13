package cn.d.yurfe.bgc.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.util.SharedPreferencesUtils;
import cn.d.yurfe.bgc.util.ShowToast;
import cn.d.yurfe.bgc.util.UIUtils;


public class VideoDiaLogActvivity extends Activity implements View.OnClickListener {

    private String finalMoney;
    private String vipNumber;
    public RelativeLayout mPay_ll_one;
    public RelativeLayout mPay_ll_all;
    public RadioButton mRb_all;
    public RadioButton mRb_one;
    public TextView mTv_all;
    public TextView mTv_money_one;
    public TextView mTv_money_all;
    public ImageButton mIb_vip_exit;
    public String mUserId;
    public String mVideoId;
    public int mVideoNum;
    public String mMoneySingle;
    public String mMoneyAverage;
    public double mtotalMoneyDouble;
    public double mSingleMoneyDouble;
    public double mAverageMoneyDouble;
    public int mPos;
    public int mPagePos;
    public String mDesc;


    //准备提交的金额
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_dialog_actvivity);
        int width = UIUtils.getWindowWidth();
        getWindow().setLayout(width - 90, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
        initListener();
        initData();
    }


    private void initView() {
        getIntents();
        getMoneys();
        initViews();
        selectSingle();
    }

    private void getIntents() {
        Intent intent = getIntent();
        mUserId = intent.getStringExtra("userId");
        mVideoId = intent.getStringExtra("videoId");
        mVideoNum = intent.getIntExtra("videoNum", 0);
        mPos = intent.getIntExtra("pos", -1);
        mPagePos = intent.getIntExtra("pagePos", -1);
        mDesc = intent.getStringExtra("desc");
    }


    private void initListener() {
        mIb_vip_exit.setOnClickListener(this);
        mRb_one.setOnClickListener(this);
        mRb_all.setOnClickListener(this);
        mPay_ll_one.setOnClickListener(this);
        mPay_ll_all.setOnClickListener(this);
    }

    /**
     * 连接网络获取数据
     */
    private void initData() {
        mTv_money_one.setText(getMoneyString(getRightSingleMoneyDouble()));
        mTv_all.setText("购买她的所有视频(" + mVideoNum + "条)");
        mTv_money_all.setText(getMoneyString(getRightTotalMoneyDouble()));
    }

    private String getMoneyString(Double data) {
        String finalDataRealMoney = "";
        try {
            DecimalFormat df = new DecimalFormat("###,###.00");
            finalDataRealMoney = "¥ " + df.format(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataRealMoney;
    }

    private void initViews() {
        mIb_vip_exit = (ImageButton) findViewById(R.id.ib_vip_exit);
        mRb_one = (RadioButton) findViewById(R.id.rb_one);
        mRb_all = (RadioButton) findViewById(R.id.rb_all);
        TextView tv_one = (TextView) findViewById(R.id.tv_one);
        mTv_all = (TextView) findViewById(R.id.tv_all);
        mTv_money_one = (TextView) findViewById(R.id.tv_money_one);
        mTv_money_all = (TextView) findViewById(R.id.tv_money_all);
        mPay_ll_one = (RelativeLayout) findViewById(R.id.pay_ll_one);
        mPay_ll_all = (RelativeLayout) findViewById(R.id.pay_ll_all);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_vip_exit:
                VideoDiaLogActvivity.this.finish();
                break;
            case R.id.rb_one:
                selectSingle();
                break;
            case R.id.pay_ll_one:
                selectSingle();
                break;
            case R.id.rb_all:
                selectAll();
                break;
            case R.id.pay_ll_all:
                selectAll();
                break;
            default:

                break;
        }
    }

    private void selectSingle() {
        mRb_one.setChecked(true);
        mRb_all.setChecked(false);
        vipNumber = "v" + mVideoId;
        finalMoney = mMoneySingle;
    }

    private void selectAll() {
        mRb_one.setChecked(false);
        mRb_all.setChecked(true);
        vipNumber = "u" + mUserId;
        finalMoney = String.valueOf(getRightTotalMoneyDouble());
    }


    public double getRightTotalMoneyDouble() {
        try {
            mSingleMoneyDouble = Double.parseDouble(mMoneySingle);
            mAverageMoneyDouble = Double.parseDouble(mMoneyAverage);
            mtotalMoneyDouble = mAverageMoneyDouble * mVideoNum;
            if (mtotalMoneyDouble <= mSingleMoneyDouble) {
                //mtotalMoneyDouble = mSingleMoneyDouble * mVideoNum;
                mtotalMoneyDouble = mSingleMoneyDouble;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mtotalMoneyDouble;
    }

    public double getRightSingleMoneyDouble() {
        try {
            mSingleMoneyDouble = Double.parseDouble(mMoneySingle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSingleMoneyDouble;
    }

    public void gotoPay(View view) {
        if (!TextUtils.isEmpty(mMoneySingle) && !TextUtils.isEmpty(mMoneyAverage)) {
            gotoPayConfirm(finalMoney, vipNumber, false);
        } else {
            ShowToast.show("金额不能为空");
        }
    }

    private void gotoPayConfirm(String moneys, String vipNumber, boolean type) {
        Intent intent = new Intent(this, PayConAct.class);
        intent.putExtra("money", moneys);
        intent.putExtra("vipNumber", vipNumber);
        intent.putExtra("exit", type);
        intent.putExtra("pos", mPos);
        intent.putExtra("pagePos", mPagePos);
        intent.putExtra("desc", mDesc);
        startActivity(intent);
        finish();
    }

    private void getMoneys() {
        mMoneySingle = SharedPreferencesUtils.getString(getApplicationContext(), "moneySingle", "");
        mMoneyAverage = SharedPreferencesUtils.getString(getApplicationContext(), "moneyAverage", "");
        if (TextUtils.isEmpty(mMoneySingle)) {
            mMoneySingle = "3";
        }
        if (TextUtils.isEmpty(mMoneyAverage)) {
            mMoneyAverage = "1";
        }
    }

    //以下是点击周边无反应,返回无响应等
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(Activity context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = context.getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop)) || (y > (decorView.getHeight() + slop));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
