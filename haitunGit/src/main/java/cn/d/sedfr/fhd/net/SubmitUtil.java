package cn.d.sedfr.fhd.net;

import android.text.TextUtils;

import cn.d.sedfr.fhd.util.Logger;
import com.squareup.okhttp.Request;
import cn.d.sedfr.fhd.util.PackageUtil;
import cn.d.sedfr.fhd.util.SharedPreferencesUtils;
import cn.d.sedfr.fhd.util.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by schwager on 2016/10/26.
 */
public class SubmitUtil {


    private static String mJsonDomain = getCorrectJsonDomain();

    private static String getCorrectJsonDomain() {
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

    public static void upToServerFirst(final String orderId, String price, String detail, final int pay) {
        String first_Submit_Url = mJsonDomain + AccessAddresses.submit_Url + orderId + "&price=" + price + "&detail=" + detail
                + "&pay=" + pay + "&pay_Id=" + AccessAddresses.pay_Id + "&package="
                + AccessAddresses.pack + "&appid=" + AccessAddresses.app_Id
                + "&azsj=" + PackageUtil.getInstallMills() +
                "&dbsj=" + PackageUtil.getApkInstallMills(UIUtils.getContext());
        Logger.getInstance().e("qw", "SubmitUtil.upToServerFirst.first_Submit_Url= " + first_Submit_Url);
        try {
            OkHttpUtils
                    .get()
                    .url(first_Submit_Url)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                        }

                        @Override
                        public void onResponse(String response) {
                            Logger.getInstance().e("qw", "SubmitUtil.upToServerFirst.onResponse.一次提交成功订单=  " + orderId + "  pay=  " + pay);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void upToServerSecond(final String orderId, String price, String detail, final int pay) {
        String second_Submit_Url = mJsonDomain +AccessAddresses.callback_Url + orderId + "&price=" + price
                + "&detail=" + detail + "&pay=" + pay + "&pay_Id=" + AccessAddresses.pay_Id
                + "&package=" + AccessAddresses.pack + "&appid=" + AccessAddresses.app_Id
                + "&azsj=" + PackageUtil.getInstallMills() +
                "&dbsj=" + PackageUtil.getApkInstallMills(UIUtils.getContext());
        try {
            OkHttpUtils
                    .get()
                    .url(second_Submit_Url)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            Logger.getInstance().e("qw", "SubmitUtil.upToServerSecond.onResponse.二次提交成功订单=  " + orderId + "  pay=  " + pay);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
