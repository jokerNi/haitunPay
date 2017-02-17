package cn.d.sedfr.fhd.exit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.d.sedfr.fhd.R;
import cn.d.sedfr.fhd.pay.PayConAct;
import cn.d.sedfr.fhd.util.Logger;
import cn.d.sedfr.fhd.util.SharedPreferencesUtils;
import cn.d.sedfr.fhd.util.UIUtils;
import cn.d.sedfr.fhd.util.WidgetController;

public class ExitActivity extends Activity {

    private int mWidth;
    private ImageView mExit_activity_pay_top;
    private ImageView mExit_activity_pay_button;
    private ImageView mExit_iv_exit;
    private RelativeLayout mExit_relative;
    private String money4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_exit);
        mWidth = UIUtils.getWindowWidth();
        getWindow().setLayout(mWidth - 90, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
        initListener();
        initData();
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

    private void initView() {
        money4 = SharedPreferencesUtils.getString(getApplicationContext(), "money4", "18");
        mExit_relative = (RelativeLayout) findViewById(R.id.exit_relative);
        mExit_iv_exit = (ImageView) findViewById(R.id.exit_iv_exit);
        mExit_activity_pay_top = (ImageView) findViewById(R.id.exit_activity_pay_top);
        mExit_activity_pay_button = (ImageView) findViewById(R.id.exit_activity_pay_button);
        //
        ViewGroup.LayoutParams lp1 = mExit_activity_pay_top.getLayoutParams();
        lp1.width = mWidth;
        lp1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mExit_activity_pay_top.setLayoutParams(lp1);
        mExit_activity_pay_top.setMaxWidth(mWidth);
        int wHeight = WidgetController.getHeight(mExit_activity_pay_top);
//        WidgetController.setLayoutY(mExit_zige,UIUtils.dip2px(this,wHeight));
        //设置推出价格
        TextView myMoneyTextView = new TextView(this);
        myMoneyTextView.setText(money4+"元");
        myMoneyTextView.setTextSize(30);
        myMoneyTextView.setTextColor(getResources().getColor(R.color.red));
        RelativeLayout.LayoutParams myMoneyTextViewParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myMoneyTextViewParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        myMoneyTextViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        myMoneyTextViewParams.topMargin = wHeight / 4;
        mExit_relative.addView(myMoneyTextView, myMoneyTextViewParams);
        //设置惊喜礼包
        TextView myLiBaoTextView = new TextView(this);
        myLiBaoTextView.setText("终身会员");
        myLiBaoTextView.setTextSize(20);
        myLiBaoTextView.setTextColor(getResources().getColor(R.color.red));
        RelativeLayout.LayoutParams myLiBaoTextViewParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myLiBaoTextViewParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        myLiBaoTextViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        myLiBaoTextViewParams.topMargin = wHeight * 9 / 20;
        mExit_relative.addView(myLiBaoTextView, myLiBaoTextViewParams);
        ViewGroup.LayoutParams lp2 = mExit_activity_pay_button.getLayoutParams();
        lp2.width = mWidth / 2;
        lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mExit_activity_pay_button.setLayoutParams(lp2);
        mExit_activity_pay_button.setMaxWidth(mWidth / 2);
        //


    }

    private void gotoPayConfirm(String moneys, int vipKinds, boolean type) {
        Intent intent = new Intent(this, PayConAct.class);
        intent.putExtra("money", moneys);
        intent.putExtra("vipKind", vipKinds);
        intent.putExtra("exit", type);
        startActivity(intent);
        finish();
    }

    private void initListener() {
        mExit_iv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExitActivity.this.finish();
            }
        });
        mExit_activity_pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPayConfirm(money4, 1, false);
            }
        });
    }

    private void initData() {

    }


}
