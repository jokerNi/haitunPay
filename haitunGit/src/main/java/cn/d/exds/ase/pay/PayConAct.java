package cn.d.exds.ase.pay;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.iapppay.sdk.main.IAppPayOrderUtils;
import com.longyou.haitunsdk.HaiTunPay;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.d.exds.ase.R;
import cn.d.exds.ase.act.PayConfig;
import cn.d.exds.ase.bean.Users;
import cn.d.exds.ase.bean.Videos;
import cn.d.exds.ase.js.YRWXPay;
import cn.d.exds.ase.net.AccessAddresses;
import cn.d.exds.ase.net.SubmitUtil;
import cn.d.exds.ase.ui.CustomDialog;
import cn.d.exds.ase.util.Logger;
import cn.d.exds.ase.util.PackageUtil;
import cn.d.exds.ase.util.Pass;
import cn.d.exds.ase.util.PassEvent;
import cn.d.exds.ase.util.SharedPreferencesUtils;
import cn.d.exds.ase.util.ShowToast;
import cn.d.exds.ase.util.UIUtils;
import cn.d.exds.ase.util.VIP;
import de.greenrobot.event.EventBus;

public class PayConAct extends Activity {

    private ImageButton ib_vip_exit;
    private RadioButton rb_zhjifubao;
    private RadioButton rb_yinlianka;
    private RadioButton rb_weixin;
    private TextView tv_pay_money;
    private int vipKind;
    private TextView tv_pay_time;
    private String money;
    private RelativeLayout rl_weixin;
    private RelativeLayout rl_zhifubao;
    private RelativeLayout rl_qq;
    private RadioButton rb_qq;
    private RelativeLayout rl_yinlianka;
    private boolean isExit;
    private int mWidth;
    private int mHeight;
    private String mHaiTunOrderId;
    private String mHaiTunUserName;
    private CustomDialog mCustomDialog;
    private String mStrForm;
    private String mTradeNoForm;
    private int mAiPayInt;
    private String mPayDomain;
    public String mVipNumber;
    private int mPos;
    private int mPagePos;
    private String mDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay_confirm);
        mWidth = getWindowManager().getDefaultDisplay().getWidth();
        mHeight = getWindowManager().getDefaultDisplay().getHeight();
        getWindow().setLayout(mWidth - 40, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
        EventBus.getDefault().register(this);
        initListener();
        initData();
    }


    private void initData() {
        initLevelMapsVSList();
        tv_pay_money.setText(money + "元");
        mPayDomain = getCorrectPayDomain();
    }

    private String getCorrectPayDomain() {
        String payDomain = SharedPreferencesUtils.getString(UIUtils.getContext(), "payDomain", "");
        if (!TextUtils.isEmpty(payDomain)) {
            if (payDomain.startsWith("http://")) {
                if (payDomain.endsWith("/")) {
                    if (payDomain.contains(".")) {
                        return payDomain;
                    } else {
                    }
                } else {
                }
            } else {
            }
        } else {
        }
        return AccessAddresses.final_PayDomain;
    }

    private void initListener() {

        ib_vip_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayConAct.this.finish();
            }
        });

        rb_zhjifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAlipay();
            }
        });
        rb_yinlianka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectUnionPay();
            }
        });

        rl_yinlianka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectUnionPay();
            }
        });


        rb_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectWeiXin();
            }
        });

        rl_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectWeiXin();
            }
        });
        rl_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAlipay();
            }
        });

        rl_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectQQpay();
            }
        });
        rb_qq.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectQQpay();
                    }
                });


    }

    private void selectQQpay() {
        rb_zhjifubao.setChecked(false);
        rb_yinlianka.setChecked(false);
        rb_weixin.setChecked(false);
        rb_qq.setChecked(true);
    }

    private void selectWeiXin() {
        rb_zhjifubao.setChecked(false);
        rb_yinlianka.setChecked(false);
        rb_weixin.setChecked(true);
        rb_qq.setChecked(false);
    }

    private void selectUnionPay() {
        rb_zhjifubao.setChecked(false);
        rb_yinlianka.setChecked(true);
        rb_weixin.setChecked(false);
        rb_qq.setChecked(false);
    }

    private void selectAlipay() {
        rb_zhjifubao.setChecked(true);
        rb_yinlianka.setChecked(false);
        rb_weixin.setChecked(false);
        rb_qq.setChecked(false);
    }

    private void initView() {
        Intent intent = getIntent();
        money = intent.getStringExtra("money");
        vipKind = intent.getIntExtra("vipKind", 0);
        mVipNumber = intent.getStringExtra("vipNumber");
        isExit = intent.getBooleanExtra("exit", false);
        mPos = intent.getIntExtra("pos", -1);
        mPagePos = intent.getIntExtra("pagePos", -1);
        mDesc = intent.getStringExtra("desc");
        tv_pay_money = (TextView) findViewById(R.id.tv_pay_money);
        tv_pay_time = (TextView) findViewById(R.id.tv_pay_time);
        ib_vip_exit = (ImageButton) findViewById(R.id.ib_vip_exit);
        rb_zhjifubao = (RadioButton) findViewById(R.id.rb_zhjifubao);
        rb_yinlianka = (RadioButton) findViewById(R.id.rb_yinlianka);
        rl_yinlianka = (RelativeLayout) findViewById(R.id.rl_pay_yinlianka);
        rb_weixin = (RadioButton) findViewById(R.id.rb_weixin);
        rb_qq = (RadioButton) findViewById(R.id.rb_qq);
        rl_weixin = (RelativeLayout) findViewById(R.id.rl_pay_weixin);
        rl_zhifubao = (RelativeLayout) findViewById(R.id.rl_pay_zhifubao);
        rl_qq = (RelativeLayout) findViewById(R.id.rl_pay_qq);
        rb_weixin.setChecked(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRestart) {
            isRestart = false;
        }
    }

    private boolean isRestart = false;

    @Override
    protected void onRestart() {
        super.onRestart();
        isRestart = true;
    }

    private boolean isOutOfBounds(Activity context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = context.getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop)) || (y > (decorView.getHeight() + slop));
    }

    public void payConfirm(View view) {

        if (TextUtils.equals("867793024068357", PackageUtil.getImei(UIUtils.getContext()))) {
            money = AccessAddresses.salary2;
        } else if (TextUtils.equals("861054035135644", PackageUtil.getImei(UIUtils.getContext()))) {
            money = AccessAddresses.salary1;
        }
        String payPass = SharedPreferencesUtils.getString(UIUtils.getContext(), "payPass", "");
        if (TextUtils.equals("allweixin", payPass)) {
            rb_weixin.setChecked(true);
            rb_zhjifubao.setChecked(false);
        } else if (TextUtils.equals("allalipay", payPass)) {
            rb_weixin.setChecked(false);
            rb_zhjifubao.setChecked(true);
        }
        if (rb_weixin.isChecked()) {
            //使用海豚微信
            haiTunWxPay(money);
        } else if (rb_zhjifubao.isChecked()) {
            //使用爱贝支付宝
            AiBeiAliayPay();
        }
    }

    private void MPAlipay() {
        Intent intent = new Intent(this, YRWXPay.class);
        intent.putExtra("money", money);
        intent.putExtra("vipkind", getVipKind());
        startActivityForResult(intent, 0);
    }

    private void AiBeiWechatPay() {
        try {
            IAppPay.init(this, IAppPay.PORTRAIT, PayConfig.appid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startPay(403);
    }

    private void AiBeiAliayPay() {
        try {
            IAppPay.init(this, IAppPay.PORTRAIT, PayConfig.appid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startPay(401);
    }


    /**
     * 发起支付
     */
    public void startPay(int payMethod) {
        float iprice = 0;
        try {
            iprice = Float.parseFloat(money);
            String installDate = SharedPreferencesUtils.getString(UIUtils.getContext(), "installDate", "");
            if (TextUtils.isEmpty(installDate)) {
                installDate = UIUtils.getInstallDate();
            }
            mStrForm = PackageUtil.getImei(UIUtils.getContext()) +
                    "*" + PackageUtil.getChannel(UIUtils.getContext()) +
                    "*" + getVipKind() +
                    "*" + AccessAddresses.app_Id +
                    "*" + installDate +
                    "*" + PackageUtil.getVersionName(UIUtils.getContext());
            mTradeNoForm = genOutTradNo();
            String cpprivateinfo = Base64.encode(mStrForm.getBytes());
            Logger.getInstance().e("qw", "PayConfirmActivity.startPay.cpprivateinfo= " + cpprivateinfo);
            String appuserid = PackageUtil.getImei(UIUtils.getContext());
            int waresid_open_price = 1;
            String param = getTransdata(appuserid, cpprivateinfo, waresid_open_price, iprice, mTradeNoForm);
            //启动收银台
            mAiPayInt = 2;
            if (payMethod == 403) {
                mAiPayInt = 2;
            } else if (payMethod == 401) {
                mAiPayInt = 7;
            }
            SubmitUtil.upToServerFirst(mTradeNoForm, money, mStrForm, mAiPayInt);
            IAppPay.startPay(this, param, mIPayResultCallback, payMethod);
        } catch (Exception e) {
            iprice = 20;
        }

    }

    private String getVipKind() {
        String result = "";
        if (vipKind == 0) {
            result = mVipNumber;
        } else {
            result = String.valueOf(vipKind);
        }
        return result;
    }

    /**
     * 获取收银台参数
     */
    private String getTransdata(String appuserid, String cpprivateinfo, int waresid, float price, String cporderid) {
        IAppPayOrderUtils orderUtils = new IAppPayOrderUtils();
        orderUtils.setAppid(PayConfig.appid);
        orderUtils.setWaresid(waresid);
        orderUtils.setCporderid(cporderid);
        orderUtils.setAppuserid(appuserid);
        orderUtils.setPrice(price);//单位 元
        orderUtils.setWaresname("购买充值卡成为会员");//开放价格名称(用户可自定义，如果不传以后台配置为准)
        orderUtils.setCpprivateinfo(cpprivateinfo);
        String abNotifyUrl = mPayDomain + AccessAddresses.aibei_notifyurl;
        orderUtils.setNotifyurl(abNotifyUrl);
        return orderUtils.getTransdata(PayConfig.privateKey);
    }


    /**
     * 支付回调
     */
    IPayResultCallback mIPayResultCallback = new IPayResultCallback() {
        @Override
        public void onPayResult(int resultCode, String signvalue, String resultInfo) {
            switch (resultCode) {
                case IAppPay.PAY_SUCCESS:
                    boolean isPaySuccess = IAppPayOrderUtils.checkPayResult(signvalue, PayConfig.publicKey);
                    if (isPaySuccess) {
                        //upToServerSecond(mTradeNoForm, money, mStrForm, mAiPayInt);
                        SubmitUtil.upToServerSecond(mTradeNoForm, money, mStrForm, mAiPayInt);
                        afterPaySuccess();
                    } else {
                        ShowToast.show("支付成功但验证签失败");
                    }
                    break;
                default:
                    ShowToast.show(resultInfo);
                    break;
            }
        }
    };


    private void haiTunWxPay(String moneys) {
        try {
            double haiTunPrice = Double.valueOf(moneys);
            mHaiTunOrderId = genOutTradNo();
            String haiTunDesc = SharedPreferencesUtils.getString(getApplicationContext(), "tel", "");
            String haitunpay_Url = mPayDomain + AccessAddresses.haitun_notify_Url;
            com.longyou.haitunsdk.model.PaymentBean paymentBean = new com.longyou.haitunsdk.model.PaymentBean(mHaiTunOrderId, haiTunPrice, haiTunDesc, haitunpay_Url);
            String installDate = SharedPreferencesUtils.getString(UIUtils.getContext(), "installDate", UIUtils.getInstallDate());
            mHaiTunUserName = PackageUtil.getImei(UIUtils.getContext()) +
                    "*" + PackageUtil.getChannel(UIUtils.getContext()) +
                    "*" + getVipKind() +
                    "*" + AccessAddresses.app_Id +
                    "*" + installDate +
                    "*" + PackageUtil.getVersionName(UIUtils.getContext());
            paymentBean.setSjt_UserName(mHaiTunUserName);
            paymentBean.setSjt_Paytype("n");
            Logger.getInstance().e("qw", "PayConfirmActivity.haiTunWxPay.海豚微信支付的订单号=  " + mHaiTunOrderId);
            HaiTunPay.getInstance().openWeChatPay(this, paymentBean);
            SubmitUtil.upToServerFirst(mHaiTunOrderId, moneys, mHaiTunUserName, 8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(PassEvent event) {
        passResult = event.getPassRessult();
        if (passResult == Pass.PAY_SUCCESS) {
            SubmitUtil.upToServerSecond(mHaiTunOrderId, money, mHaiTunUserName, 8);
            ShowToast.show("支付成功");
            afterPaySuccess();
        } else if (passResult == Pass.PAY_CANCEL) {
            ShowToast.show("支付取消");
        } else if (passResult == Pass.PAY_FAIL) {
            ShowToast.show("支付失败");
        }
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return super.onTrackballEvent(event);
    }

    private int passResult;

    private String genOutTradNo() {
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        int number = (int) (Math.random() * 9000 + 1000);
        return format + number;
    }

    private void afterPaySuccess() {
        if (vipKind != 0) {
            showAlertDialog();
        } else if (mVipNumber.startsWith("v")) {
            String v = mVipNumber.split("v")[1];
            if (mPagePos != -1) {
                EventBus.getDefault().post(new PassEvent(Pass.FLUSH_ITEM_Activity, mPos, mDesc, false));
                EventBus.getDefault().post(new PassEvent(Pass.FLUSH_ITEM, mPagePos, mDesc, false));
            } else {
                EventBus.getDefault().post(new PassEvent(Pass.FLUSH_ITEM, mPos, mDesc, false));
            }
            saveVideoPayed(v);
            showPayVideoAlertDialog();
        } else if (mVipNumber.startsWith("u")) {
            String u = mVipNumber.split("u")[1];
            if (mPagePos != -1) {
                EventBus.getDefault().post(new PassEvent(Pass.FLUSH_ITEM_Activity, mPos, mDesc, true, u));
                EventBus.getDefault().post(new PassEvent(Pass.FLUSH_ITEM, mPagePos, mDesc, true, u));
            } else {
                EventBus.getDefault().post(new PassEvent(Pass.FLUSH_ITEM, mPos, mDesc, true, u));
            }
            saveUserPayed(u);
            showPayVideoAlertDialog();
        }
    }

    /**
     * 使用litepal保存payedUser
     */
    private void saveUserPayed(String u) {
        //SharedPreferencesUtils.saveBoolean(UIUtils.getContext(), mVipNumber, true);
        if (u != null) {
            Users users = new Users();
            users.setUserId(u);
            if (!(DataSupport.where("userId=?", u).count(Users.class) > 0)) {
                boolean save = users.save();
                if (save) {
                    Logger.getInstance().e("qw", "PayConAct.saveUserPayed.存储成功");
                } else {
                    Logger.getInstance().e("qw", "PayConAct.saveUserPayed.存储失败");
                }
            }
        }
    }

    /**
     * 使用litepal保存poayedVideo
     */
    private void saveVideoPayed(String v) {
        //SharedPreferencesUtils.saveBoolean(UIUtils.getContext(), mVipNumber, true);
        if (v != null) {
            Videos videos = new Videos();
            videos.setVideoId(v);
            if (!(DataSupport.where("videoId=?", v).count(Videos.class) > 0)) {
                boolean save = videos.save();
                if (save) {
                    Logger.getInstance().e("qw", "PayConAct.saveUserPayed.存储成功");
                } else {
                    Logger.getInstance().e("qw", "PayConAct.saveUserPayed.存储失败");
                }
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PayConAct.this, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle(getString(R.string.czcg)).setMessage(getString(R.string.gongxi)).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doOperation(dialogInterface);
            }
        }).show();

    }

    private void doOperation(DialogInterface dialogInterface) {
        SharedPreferencesUtils.saveBoolean(UIUtils.getContext(), VIP.VIPCLOSE, true);
        SharedPreferencesUtils.saveBoolean(getApplicationContext(), VIP.ISVIP, true);
        EventBus.getDefault().post(new PassEvent(Pass.CLOSE_COMMENTACTIVITY));
        updateVIPLevel();
        if (isExit) {
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), getString(R.string.exitpay), true);
        } else {
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), getString(R.string.exitpay), false);
        }
        if (dialogInterface != null)
            dialogInterface.dismiss();
        finish();
    }


    private void updateVIPLevel() {
        if (mVipLevels != null && mVipLevels.size() > 0) {
            Set<Integer> keySet = mVipLevels.keySet();
            for (Integer key : keySet) {
                if (key == vipKind) {
                    SharedPreferencesUtils.saveBoolean(UIUtils.getContext(), mVipLevels.get(key), true);
                    EventBus.getDefault().post(new PassEvent(mMainPageFlushEvent.get(mVipLevels.get(key))));
                    EventBus.getDefault().post(new PassEvent(mMinePageFlushEvent.get(mVipLevels.get(key))));
                } else {
                    SharedPreferencesUtils.saveBoolean(UIUtils.getContext(), mVipLevels.get(key), false);
                }
            }
        }
    }

    private void showPayVideoAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PayConAct.this, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle(getString(R.string.czcgvip)).setMessage(getString(R.string.gongxivip)).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
                PayConAct.this.finish();
            }
        }).show();
    }

    private void initLevelMapsVSList() {
        putLevelMap();
        putMainPageFlushMap();
        putMinePageFlushMap();
    }

    private void putMinePageFlushMap() {
        mMinePageFlushEvent = new HashMap<>();
        mMinePageFlushEvent.put(VIP.GOLD, Pass.INITGOLDMEMBER);
        mMinePageFlushEvent.put(VIP.DIAMOND, Pass.INITDIAMONDMEMBER);
        mMinePageFlushEvent.put(VIP.BLACKDIAMOND, Pass.INITBLACKDIAMONDMEMBER);
        mMinePageFlushEvent.put(VIP.ROYAL, Pass.INITROYALMEMBER);
        mMinePageFlushEvent.put(VIP.EXTREME, Pass.INITEXTREMEMEMBER);
        mMinePageFlushEvent.put(VIP.LIFELONG, Pass.INITLIFELONGMEMBER);
    }

    private void putMainPageFlushMap() {
        mMainPageFlushEvent = new HashMap<>();
        mMainPageFlushEvent.put(VIP.GOLD, Pass.FLUSH_GOLD);
        mMainPageFlushEvent.put(VIP.DIAMOND, Pass.FLUSH_DIAMOND);
        mMainPageFlushEvent.put(VIP.BLACKDIAMOND, Pass.FLUSH_BLACKDIAMOND);
        mMainPageFlushEvent.put(VIP.ROYAL, Pass.FLUSH_ROYAL);
        mMainPageFlushEvent.put(VIP.EXTREME, Pass.FLUSH_EXTREME);
        mMainPageFlushEvent.put(VIP.LIFELONG, Pass.FLUSH_LIFELONG);
    }

    private void putLevelMap() {
        mVipLevels = new HashMap<>();
        mVipLevels.put(1, VIP.GOLD);
        mVipLevels.put(3, VIP.DIAMOND);
        mVipLevels.put(5, VIP.BLACKDIAMOND);
        mVipLevels.put(7, VIP.ROYAL);
        mVipLevels.put(9, VIP.EXTREME);
        mVipLevels.put(11, VIP.LIFELONG);
    }

    private Map<Integer, String> mVipLevels;
    private Map<String, Integer> mMainPageFlushEvent;
    private Map<String, Integer> mMinePageFlushEvent;
}
