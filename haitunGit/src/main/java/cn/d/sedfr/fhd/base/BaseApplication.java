package cn.d.sedfr.fhd.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.longyou.haitunsdk.HaiTunPay;

import com.squareup.okhttp.Request;

import cn.d.sedfr.fhd.util.PackageUtil;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePalApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.d.sedfr.fhd.R;
import cn.d.sedfr.fhd.net.AccessAddresses;
import cn.d.sedfr.fhd.util.Logger;
import cn.d.sedfr.fhd.util.SharedPreferencesUtils;
import cn.d.sedfr.fhd.util.UIUtils;
import cn.d.sedfr.fhd.util.VIP;
import huigouwang.com.utils.Us;

/**
 * Created by schwager on 2016/6/22.
 */
public class BaseApplication extends LitePalApplication {

    private static Context mContext;

    private static Handler mHandler;
    private Map<String, String> mVipLevels;

    public static Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mContext = getApplicationContext();
        initVIPLevelList();
        try {
            getCommenInfo(AccessAddresses.commonInformationUrl);
            //初始化海豚之父
            HaiTunPay.getInstance().init(this);
            // 配置是否显示控制台日志，开启后便于开发者查看问题
            HaiTunPay.setDebug(true);

            Us.sdkinit(getApplicationContext(), "10004");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean useExternalInfoUrl = false;

    private void getCommenInfo(String url) {
        OkHttpUtils.get().url(url + PackageUtil.getInstallMills()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                //ShowToast.show("请求失败");
            }

            @Override
            public void onResponse(String response) {
                if (useExternalInfoUrl) {
                    String msg = "已经成功请求新信息";
                    if (TextUtils.isEmpty(response)) {
                        //Logger.getInstance().e("qw", "BaseApplication.onResponse.新信息还是空");
                        msg = "新信息还是空";
                        saveInfoEvenNull("sknum", "9");
                    }
                    //ShowToast.show(msg);
                    //Logger.getInstance().e("qw", "BaseApplication.onResponse." + response);
                    processInfomrations(response);
                } else {
                    if (TextUtils.isEmpty(response)) {
                        //Logger.getInstance().e("qw", "BaseApplication.onResponse.为空");
                        //ShowToast.show("Json为空请求新信息");
                        getExternalInformations();
                    } else {
                        //Logger.getInstance().e("qw", "BaseApplication.onResponse." + response);
                        processInfomrations(response);
                    }
                }
            }
        });


    }

    private void saveInfoEvenNull(String sknum, String message) {
        storeInSP(sknum, message);
        SharedPreferencesUtils.saveString(getApplicationContext(), "money1", "28");
        SharedPreferencesUtils.saveString(getApplicationContext(), "money2", "30");
        SharedPreferencesUtils.saveString(getApplicationContext(), "money3", "20");
        SharedPreferencesUtils.saveString(getApplicationContext(), "money4", "18");
    }


    private void getExternalInformations() {
        try {
            useExternalInfoUrl = true;
            //ShowToast.show("正在请求新信息");
            getCommenInfo(AccessAddresses.externalInformationUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processInfomrations(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String isVIP = jsonObject.optString(getString(R.string.vip));
            if (!useExternalInfoUrl) {
                if (TextUtils.isEmpty(isVIP)) {
                    getExternalInformations();
                } else {
                    saveMyInfo(jsonObject, isVIP);
                }
            } else {
                saveMyInfo(jsonObject, isVIP);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void saveMyInfo(JSONObject jsonObject, String isVIP) {
        //ShowToast.show("正常保存信息");
        useExternalInfoUrl = false;
        String ismsg = jsonObject.optString("ismsg");
        String msgbody = jsonObject.optString("msgbody");
        String money1 = jsonObject.optString("money1");
        String money2 = jsonObject.optString("money2");
        String money3 = jsonObject.optString("money3");
        String money4 = jsonObject.optString("money4");
        String money5 = jsonObject.optString("money5");
        String money6 = jsonObject.optString("money6");
        String money7 = jsonObject.optString("money7");
        String moneySingle = jsonObject.optString("moneySingle");
        String moneyAverage = jsonObject.optString("moneyAverage");
        String qq1 = jsonObject.optString("qq1");
        String qq2 = jsonObject.optString("qq2");
        String sknum = jsonObject.optString("sknum");
        String qq = jsonObject.optString("qq");
        String vipLevel = jsonObject.optString("vip_level");
        String quPay = jsonObject.optString("qwPay");
        String tel = jsonObject.optString("tel");
        String htAbAliay = jsonObject.optString("htAbAliay");
        String htMpAb = jsonObject.optString("htMpAb");
        String payPass = jsonObject.optString("payPass");
        String jsonDomain = jsonObject.optString("jsonDomain");
        String payDomain = jsonObject.optString("payDomain");
        String openCover = jsonObject.optString("openCover");
        String openVideoCover = jsonObject.optString("openVideoCover");
        String videoCalc = jsonObject.optString("videoCalc");
        String videoOnlyUser = jsonObject.optString("videoOnlyUser");
        int freeMills = jsonObject.optInt("freeMills");
        //VIP会员
        if (!TextUtils.equals("true", openCover)) {
            if (TextUtils.equals(isVIP, "true")) {
                SharedPreferencesUtils.saveBoolean(getContext(), "isVIP", true);
                //VIPLevel
                saveVIPLevel(vipLevel);
            } else {
                SharedPreferencesUtils.saveBoolean(getContext(), "isVIP", false);
                SharedPreferencesUtils.saveBoolean(getContext(), "GoldVIP", false);
                SharedPreferencesUtils.saveBoolean(getContext(), "DiamondVIP", false);
                SharedPreferencesUtils.saveBoolean(getContext(), "BlackDiamondVIP", false);
                SharedPreferencesUtils.saveBoolean(getContext(), "RoyalVIP", false);
                SharedPreferencesUtils.saveBoolean(getContext(), "ExtremeVIP", false);
                SharedPreferencesUtils.saveBoolean(getContext(), "LifeLongVIP", false);
            }
        }
        storeInSP("ismsg", ismsg);
        storeInSP("msgbody", msgbody);
        storeInSP("qq", qq);
        storeInSP("money1", money1);
        storeInSP("money2", money2);
        storeInSP("money3", money3);
        storeInSP("money4", money4);
        storeInSP("money5", money5);
        storeInSP("money6", money6);
        storeInSP("money7", money7);
        storeInSP("moneySingle", moneySingle);
        storeInSP("moneyAverage", moneyAverage);
        storeInSP("qq1", qq1);
        storeInSP("qq2", qq2);
        storeInSP("sknum", sknum);
        storeInSP("qwPay", quPay);
        storeInSP("tel", tel);
        storeInSP("htAbAliay", htAbAliay);
        storeInSP("htMpAb", htMpAb);
        storeInSP("payPass", payPass);
        storeInSP("openCover", openCover);
        storeInSP("openVideoCover", openVideoCover);
        storeInSP("videoCalc", videoCalc);
        storeInSP("videoOnlyUser", videoOnlyUser);
        storeJsonDomain(jsonDomain);
        storeInSP(payDomain);
        if (freeMills != 0) {
            SharedPreferencesUtils.saveInt(getContext(), "freeMills", freeMills);
        }
    }

    private void saveVIPLevel(String vipLevel) {
        if (mVipLevels.keySet().contains(vipLevel)) {
            updateVIPState(vipLevel);
        }
    }

    private void initVIPLevelList() {
        mVipLevels = new HashMap<>();
        mVipLevels.put(VIP.GOLDLEVELSRING, VIP.GOLD);
        mVipLevels.put(VIP.DIAMONDLEVELSTRING, VIP.DIAMOND);
        mVipLevels.put(VIP.BLACKDIAMONDLEVELSTRING, VIP.BLACKDIAMOND);
        mVipLevels.put(VIP.ROYALLEVELSTRING, VIP.ROYAL);
        mVipLevels.put(VIP.EXTREMELEVELSTRING, VIP.EXTREME);
        mVipLevels.put(VIP.LIFELONGLEVELSTRING, VIP.LIFELONG);
    }

    private void updateVIPState(String vipType) {
        if (mVipLevels != null && mVipLevels.size() > 0) {
            Set<String> keySet = mVipLevels.keySet();
            for (String key : keySet) {
                if (TextUtils.equals(key, vipType)) {
                    SharedPreferencesUtils.saveBoolean(getContext(), mVipLevels.get(key), true);
                } else {
                    SharedPreferencesUtils.saveBoolean(getContext(), mVipLevels.get(key), false);
                }
            }
        }
    }

    private void storeInSP(String payDomain) {
        if (!TextUtils.isEmpty(payDomain)) {
            if (payDomain.startsWith("http://")) {
                if (payDomain.endsWith("/")) {
                    if (payDomain.contains(".")) {
                        storeInSP("payDomain", payDomain);
                    } else {
                    }
                } else {
                }
            } else {
            }
        } else {
        }

    }

    private void storeJsonDomain(String jsonDomain) {
        if (!TextUtils.isEmpty(jsonDomain)) {
            if (jsonDomain.startsWith("http://")) {
                if (jsonDomain.endsWith("/")) {
                    if (jsonDomain.contains(".")) {
                        storeInSP("jsonDomain", jsonDomain);
                    } else {
                    }
                } else {
                }
            } else {
            }
        } else {
        }
    }

    private void storeInSP(String key, String message) {
        if (!TextUtils.isEmpty(message)) {
            SharedPreferencesUtils.saveString(getContext(), key, message);
        }
    }

    public static Context getContext() {
        return mContext;
    }
}
