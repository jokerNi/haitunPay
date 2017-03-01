package cn.d.fesa.wuf.js;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import cn.d.fesa.wuf.R;
import cn.d.fesa.wuf.act.PayConfig;
import cn.d.fesa.wuf.net.AccessAddresses;
import cn.d.fesa.wuf.net.SubmitUtil;
import cn.d.fesa.wuf.ui.CustomDialog;
import cn.d.fesa.wuf.util.Logger;
import cn.d.fesa.wuf.util.PackageUtil;
import cn.d.fesa.wuf.util.SharedPreferencesUtils;
import cn.d.fesa.wuf.util.UIUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class YRWXPay extends Activity {
    private static String bundle = "end";
    private int hockTimes = 0;
    private boolean isHook;
    private boolean noWX;
    private WebView mWebView;
    private String mOut_trade_no;
    private String mTradNo;

    private String mPayUrl = PayConfig.PayUrl;
    private CustomDialog mCustomDialog;
    private String mSubmitAttach;
    private String mMoney;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        hockTimes = 0;
        noWX = false;
        setContentView(getContentView(this));
        mCustomDialog = new CustomDialog(this,
                R.layout.dialog_layout_normal, R.style.DialogTheme);
        mCustomDialog.setCancelable(true);
        mCustomDialog.setCanceledOnTouchOutside(false);
       /* mCustomDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                boolean b = false;
                switch (i) {
                    case KeyEvent.KEYCODE_BACK:
                        b = true;
                        break;
                    case KeyEvent.KEYCODE_HOME:
                        b = true;
                        break;
                    case KeyEvent.KEYCODE_MENU:
                        b = true;
                        break;
                    case KeyEvent.KEYCODE_SEARCH:
                        b = true;
                        break;
                }
                return b;
            }
        });*/
        mCustomDialog.show();
        //判断是否安装支付宝
        if (isAliayAvilible(UIUtils.getContext())) {
            mCustomDialog.setContentView(R.layout.dialog_layout_normal);
            mCustomDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.dismiss();

                }
            });
        } else {
            mCustomDialog.setContentView(R.layout.dialog_layout_install);
            mCustomDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.dismiss();
                    YRWXPay.this.finish();
                }
            });
        }
        //组合参数
        assembleUrl();
        mWebView.addJavascriptInterface(new JavaScriptObject(this), "qw");
        mWebView.loadUrl(mPayUrl);
    }

    private void assembleUrl() {
        String mpChannel = "RS190";
        mMoney = getIntent().getStringExtra("money");
        int vipkind = getIntent().getIntExtra("vipkind", 0);
        int number = (int) (Math.random() * 9000 + 1000);
        //接口的订单号
        mOut_trade_no = mpChannel + "_" + System.currentTimeMillis() + number;
        String device_info =
                PackageUtil.getImei(UIUtils.getContext());
        String body = "开通会员";
        String installDate = SharedPreferencesUtils.getString(UIUtils.getContext(), "installDate", "");
        if (TextUtils.isEmpty(installDate)) {
            installDate = UIUtils.getInstallDate();
        }
        mTradNo = genOutTradNo();
        Logger.getInstance().e("qw", "YRWXPay.assembleUrl.mOut_trade_no= " + mOut_trade_no);
        mSubmitAttach = PackageUtil.getImei(UIUtils.getContext()) +
                "*" + PackageUtil.getChannel(UIUtils.getContext()) +
                "*" + vipkind +
                "*" + AccessAddresses.app_Id +
                "*" + installDate +
                "*" + PackageUtil.getVersionName(UIUtils.getContext());
        String cpTradeNo = mTradNo + "-" + mSubmitAttach;
        String attach = cpTradeNo;
        String goods_tag = PayConfig.GOODS_TAG;
        String total_fee = UIUtils.getMoney(mMoney);

        String callback_url = AccessAddresses.CALLBACK_URL + mOut_trade_no;
        //一次提交
        SubmitUtil.upToServerFirst(mTradNo, mMoney, mSubmitAttach, 9);
        mPayUrl = mPayUrl + "?out_trade_no=" + mOut_trade_no + "&device_info=" + device_info + "&body=" + body + "&attach=" + attach + "&goods_tag=" + goods_tag
                + "&total_fee=" + total_fee + "&callback_url=" + callback_url;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private View getContentView(final Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(Color.WHITE);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mWebView = new WebView(context);
        layout.addView(mWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mWebView.setWebViewClient(new ReWebViewClient());
        WebSettings aaparamBundle = mWebView.getSettings();
        aaparamBundle.setSaveFormData(false);
        aaparamBundle.setJavaScriptEnabled(true);
        aaparamBundle.setBuiltInZoomControls(false);
        aaparamBundle.setSupportZoom(false);
        aaparamBundle.setAppCacheEnabled(true);
        aaparamBundle.setDatabaseEnabled(false);
        aaparamBundle.setDomStorageEnabled(true);
        return layout;
    }

    public class ReWebViewClient extends WebViewClient {
        private Intent mIntent;


        public void onPageFinished(WebView paramWebView, String paramString) {
            if (paramString.contains("success")) {
                bundle = "successpay";
            }
            super.onPageFinished(paramWebView, paramString);
        }

        public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap) {
            if (paramString.contains("success")) {
                bundle = "successpay";
            }
            super.onPageStarted(paramWebView, paramString, paramBitmap);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            Log.e("qw", "访问的url地址：" + url);
            if (parseScheme(url)) {
                try {
                    if (mIntent == null) {
                        mIntent = new Intent();
                        mIntent.setAction("android.intent.action.VIEW");
                        mIntent.setData(Uri.parse(url));
                    }
                    startActivity(mIntent);
                    isHook = true;
                    view.setVisibility(View.VISIBLE);
                    dimissCustomDialog();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (isCancle(url)) {
                //网址是继续支付 怎么处理 应该是轮询订单接口查询
                dealContinueButton();
            } else {
                view.loadUrl(url);
                view.setVisibility(View.GONE);
                mCustomDialog.show();
            }
            return true;
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }

    public static boolean isAliayAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.eg.android.AlipayGphone")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void dealContinueButton() {
        queryOrderStateWhilePressContinue(mOut_trade_no);
    }

    public static boolean ORDER_PAY_SUCCESS = false;

    public void queryOrderStateWhilePressContinue(String outrate_id) {

        OkHttpUtils.get().url(PayConfig.QUERY_URL + outrate_id).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                Logger.getInstance().e("qw", "PayConfirmActivity.onError.");
                dimissCustomDialog();
                YRWXPay.this.setResult(PAY_FAIL);
                YRWXPay.this.finish();
            }

            @Override
            public void onResponse(String response) {
                ORDER_PAY_SUCCESS = parseContinuePayButton(response);
                dimissCustomDialog();
                if (ORDER_PAY_SUCCESS) {
                    YRWXPay.this.setResult(PAY_SUCCESS);
                } else {
                    YRWXPay.this.setResult(PAY_CONTINUE);
                }
                YRWXPay.this.finish();
            }
        });

    }

    public boolean parseContinuePayButton(String response) {
        boolean flag = false;
        try {
            JSONObject jsonObject = new JSONObject(response);
            String message = jsonObject.optString("message");
            if (TextUtils.equals("success", message)) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean parseScheme(String url) {

        if (url.contains("platformapi/startApp")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCancle(String url) {
        if (url.contains("mclient.alipay.com/h5Continue")) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.getInstance().e("qw", "YRWXPay.onDestroy.");
    }


    private String genOutTradNo() {
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        int number = (int) (Math.random() * 9000 + 1000);
        return format + number;
    }


    public class JavaScriptObject {
        Context mContxt;

        public JavaScriptObject(Context mContxt) {
            this.mContxt = mContxt;
        }

        @JavascriptInterface
        public void succ(final String message) {
            Logger.getInstance().e("qw", "JavaScriptObject.haha.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Logger.getInstance().e("qw", "JavaScriptObject.succ.");
                    //二次提交
                    SubmitUtil.upToServerSecond(mTradNo, mMoney, mSubmitAttach, 9);
                    dimissCustomDialog();
                    YRWXPay.this.setResult(PAY_SUCCESS);
                    YRWXPay.this.finish();
                }
            });
        }

        @JavascriptInterface
        public void fai(final String message) {
            Logger.getInstance().e("qw", "JavaScriptObject.haha.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Logger.getInstance().e("qw", "JavaScriptObject.fail.");
                    dimissCustomDialog();
                    YRWXPay.this.setResult(PAY_FAIL);
                    YRWXPay.this.finish();
                }
            });
        }


    }

    public static final int PAY_SUCCESS = 10;
    public static final int PAY_FAIL = 11;
    public static final int PAY_CANCLE = 12;
    public static final int PAY_CONTINUE = 13;

    private void dimissCustomDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCustomDialog != null) {
                    mCustomDialog.dismiss();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            queryOrderStateWhilePressBack(mOut_trade_no);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void queryOrderStateWhilePressBack(String out_trade_no) {
        OkHttpUtils.get().url(PayConfig.QUERY_URL + out_trade_no).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                Logger.getInstance().e("qw", "PayConfirmActivity.queryOrderStateWhilePressBack.onError.");
                dimissCustomDialog();
                YRWXPay.this.setResult(PAY_FAIL);
                YRWXPay.this.finish();
            }

            @Override
            public void onResponse(String response) {
                ORDER_PAY_SUCCESS = parseContinuePayButton(response);
                dimissCustomDialog();
                if (ORDER_PAY_SUCCESS) {
                    YRWXPay.this.setResult(PAY_SUCCESS);
                } else {
                    YRWXPay.this.setResult(PAY_CANCLE);
                }
                YRWXPay.this.finish();
            }
        });
    }
}
