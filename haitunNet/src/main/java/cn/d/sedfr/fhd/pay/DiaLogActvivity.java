package cn.d.sedfr.fhd.pay;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import cn.d.sedfr.fhd.util.SharedPreferencesUtils;
import cn.d.sedfr.fhd.R;
import cn.d.sedfr.fhd.util.ShowToast;
import cn.d.sedfr.fhd.util.UIUtils;


public class DiaLogActvivity extends Activity {

    private ImageButton ib_vip_exit;
    private RelativeLayout pay_ll_VIP;
    private TextView tv_money_VIP;
    private String gold_money;
    private String diamond_money;
    private String black_diamond_money;
    private String royal_money;
    private String extreme_money;
    private String lifelong_money;
    private String exit;
    private RadioButton rv_VIP;
    private TextView tv_money_false;
    private ImageView iv_dia_pay_top;
    private String money1;
    private String money2;
    private String money3;
    private String money4;
    private String money5;
    private String money6;
    private String money7;
    private TextView vip_Name;
    private TextView vip_Desc;
    private Button mPay;


    //准备提交的金额
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog_actvivity);
        int width = UIUtils.getWindowWidth();
        getWindow().setLayout(width - 90, WindowManager.LayoutParams.WRAP_CONTENT);
        exit = getIntent().getStringExtra("exit");
        initView();
        initListener();
        initData();
    }


    private void initView() {
        getMoneys();
        initViews();
        initButtons();
    }

    private void initListener() {
        ib_vip_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiaLogActvivity.this.finish();
            }
        });
    }

    /**
     * 连接网络获取数据
     */
    private void initData() {
        if (!UIUtils.isVIP()) {
            setVIPButtonMoney(gold_money);
        } else if (UIUtils.isGoldVIP()) {
            setVIPButtonMoney(diamond_money);
        } else if (UIUtils.isDiamondVIP()) {
            setVIPButtonMoney(black_diamond_money);
        } else if (UIUtils.isBlackDiamondVIP()) {
            setVIPButtonMoney(royal_money);
        } else if (UIUtils.isRoyalVIP()) {
            setVIPButtonMoney(extreme_money);
        } else if (UIUtils.isExtremeVIP()) {
            setVIPButtonMoney(lifelong_money);
        } else if (UIUtils.isLifeLongVIP()) {
            setVIPButtonMoney(gold_money);
        }
    }

    private void initButtons() {
        rv_VIP.setChecked(true);
        if (!UIUtils.isVIP()) {
            iv_dia_pay_top.setImageResource(R.drawable.p_top1);
            vip_Name.setText("开通黄金会员");
            vip_Desc.setText("海量完整成人视频永久观看权限");
            mPay.setText("立即开通");
            finalMoney = gold_money;
            vipKind = 1;
        } else if (UIUtils.isGoldVIP()) {
            iv_dia_pay_top.setImageResource(R.drawable.p_top2);
            vip_Desc.setText("升级后可无限制观看所有视频");
            vip_Name.setText("升级钻石会员");
            mPay.setText("立即升级");
            finalMoney = diamond_money;
            vipKind = 3;
        } else if (UIUtils.isDiamondVIP()) {
            iv_dia_pay_top.setImageResource(R.drawable.p_top3);
            vip_Name.setText("升级黑钻会员");
            vip_Desc.setText("最后一步仅" + black_diamond_money + "元解锁全部完整视频");
            mPay.setText("立即解锁");
            finalMoney = black_diamond_money;
            vipKind = 5;
        } else if (UIUtils.isBlackDiamondVIP()) {
            iv_dia_pay_top.setImageResource(R.drawable.p_top1);
            vip_Name.setText("升级皇冠会员");
            vip_Desc.setText("最后一步仅" + royal_money + "元解锁全部完整视频");
            mPay.setText("立即升级皇冠");
            finalMoney = royal_money;
            vipKind = 7;
        } else if (UIUtils.isRoyalVIP()) {
            iv_dia_pay_top.setImageResource(R.drawable.p_top2);
            vip_Name.setText("升级至尊会员");
            vip_Desc.setText("最后一步仅" + extreme_money + "元解锁全部完整视频");
            mPay.setText("立即升级至尊");
            finalMoney = extreme_money;
            vipKind = 9;
        } else if (UIUtils.isExtremeVIP()) {
            iv_dia_pay_top.setImageResource(R.drawable.p_top3);
            vip_Name.setText("升级终身会员");
            vip_Desc.setText("最后一步仅" + lifelong_money + "元解锁全部完整视频");
            mPay.setText("立即升级终身");
            finalMoney = lifelong_money;
            vipKind = 11;
        } else if (UIUtils.isLifeLongVIP()) {
            iv_dia_pay_top.setImageResource(R.drawable.p_top1);
            vip_Name.setText("开通黄金会员");
            vip_Desc.setText("最后一步仅" + gold_money + "元解锁全部完整视频");
            mPay.setText("立即开通黄金");
            finalMoney = gold_money;
            vipKind = 1;
        }
    }

    private void getMoneys() {
        money1 = SharedPreferencesUtils.getString(getApplicationContext(), "money1", "");
        money2 = SharedPreferencesUtils.getString(getApplicationContext(), "money2", "");
        money3 = SharedPreferencesUtils.getString(getApplicationContext(), "money3", "");
        money4 = SharedPreferencesUtils.getString(getApplicationContext(), "money4", "");
        money5 = SharedPreferencesUtils.getString(getApplicationContext(), "money5", "");
        money6 = SharedPreferencesUtils.getString(getApplicationContext(), "money6", "");
        money7 = SharedPreferencesUtils.getString(getApplicationContext(), "money7", "");
        //没有获取到网上的金额的时候
        if (TextUtils.isEmpty(money1)) {
            money1 = "28";
        }
        if (TextUtils.isEmpty(money2)) {
            money2 = "30";
        }
        if (TextUtils.isEmpty(money3)) {
            money3 = "20";
        }
        if (TextUtils.isEmpty(money4)) {
            money4 = "18";
        }
        if (TextUtils.isEmpty(money5)) {
            money5 = "38";
        }
        if (TextUtils.isEmpty(money6)) {
            money6 = "48";
        }
        if (TextUtils.isEmpty(money7)) {
            money7 = "58";
        }
        double int_Money1 = 0;
        double int_Money2 = 0;
        double int_Money3 = 0;
        double int_Money4 = 0;
        double int_Money5 = 0;
        double int_Money6 = 0;
        double int_Money7 = 0;
        try {
            int_Money1 = Double.parseDouble(money1);
            int_Money2 = Double.parseDouble(money2);
            int_Money3 = Double.parseDouble(money3);
            int_Money4 = Double.parseDouble(money4);
            int_Money5 = Double.parseDouble(money5);
            int_Money6 = Double.parseDouble(money6);
            int_Money7 = Double.parseDouble(money7);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("exit".equals(exit)) {
            gold_money = String.valueOf(int_Money4);
        } else {
            gold_money = String.valueOf(int_Money1);
        }
        diamond_money = String.valueOf(int_Money2);
        black_diamond_money = String.valueOf(int_Money3);
        royal_money = String.valueOf(int_Money5);
        extreme_money = String.valueOf(int_Money6);
        lifelong_money = String.valueOf(int_Money7);
    }

    private void initViews() {
        mPay = (Button) findViewById(R.id.dialog_btn_pay);
        iv_dia_pay_top = (ImageView) findViewById(R.id.iv_dia_pay_top);
        rv_VIP = (RadioButton) findViewById(R.id.rb_vip);
        ib_vip_exit = (ImageButton) findViewById(R.id.ib_vip_exit);
        tv_money_VIP = (TextView) findViewById(R.id.tv_money_vip);
        vip_Name = (TextView) findViewById(R.id.tv_vip);
        vip_Desc = (TextView) findViewById(R.id.tv_desc);
        tv_money_false = (TextView) findViewById(R.id.tv_money_vip_false);
        pay_ll_VIP = (RelativeLayout) findViewById(R.id.pay_ll_vip);
    }


    private String finalMoney;
    private int vipKind;

    private void setVIPButtonMoney(String data) {
        double dataRealMoney = 0;
        double dataFalseMoney = 0;
        String finalDataRealMoney = "";
        String finalDataFalseMoney = "";
        try {
            dataRealMoney = Double.parseDouble(data);
            dataFalseMoney = getDoubleMoney(data);
            DecimalFormat df = new DecimalFormat("###,###.00");
            finalDataRealMoney = "¥ " + df.format(dataRealMoney);
            finalDataFalseMoney = "¥ " + df.format(dataFalseMoney);
        } catch (Exception e) {
            e.printStackTrace();
            finalDataRealMoney = "¥ " + data;
            finalDataFalseMoney = "¥ " + String.valueOf(getDoubleMoney(data));
        }
        tv_money_VIP.setText(finalDataRealMoney);
        tv_money_false.setText(finalDataFalseMoney);
        tv_money_false.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private double getDoubleMoney(String diamond_money) {
        double d = 0;
        try {
            d = 2 * Double.parseDouble(diamond_money);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public void gotoPay(View view) {
        if (!TextUtils.isEmpty(gold_money) && !TextUtils.isEmpty(diamond_money)) {
            if ("exit".equals(exit)) {
                gotoPayConfirm(finalMoney, vipKind, true);
            } else {
                gotoPayConfirm(finalMoney, vipKind, false);
            }
        } else {
            ShowToast.show("金额不能为空");
        }
    }

    private void gotoPayConfirm(String moneys, int vipKinds, boolean type) {
        Intent intent = new Intent(this, PayConAct.class);
        intent.putExtra("money", moneys);
        intent.putExtra("vipKind", vipKinds);
        intent.putExtra("exit", type);
        startActivity(intent);
        finish();
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
