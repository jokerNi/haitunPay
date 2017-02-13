package cn.d.yurfe.bgc.util;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.act.CommentActivity;
import cn.d.yurfe.bgc.act.HaveBeenPayedActivity;
import cn.d.yurfe.bgc.act.PrivateVideoUserActivity;
import cn.d.yurfe.bgc.act.SplashActivity;
import cn.d.yurfe.bgc.base.BaseApplication;
import cn.d.yurfe.bgc.act.MinePageActivity;
import cn.d.yurfe.bgc.bean.Users;
import cn.d.yurfe.bgc.bean.Videos;
import cn.d.yurfe.bgc.pay.DiaLogActvivity;
import cn.d.yurfe.bgc.pay.VideoDiaLogActvivity;
import jp.wasabeef.picasso.transformations.BlurTransformation;

import java.util.Calendar;

/**
 * Created by schwager on 2016/4/20.
 */
public class UIUtils {
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    /**
     * xml 转成View对象
     *
     * @param id
     * @return
     */
    public static View inflate(int id) {

        return LayoutInflater.from(getContext()).inflate(id, null, false);
    }

    public static Drawable getDrawable(int id) {

        return getContext().getResources().getDrawable(id);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static boolean isVIP() {
        return SharedPreferencesUtils.getBoolean(getContext(), VIP.ISVIP, false);
    }

    public static boolean isVIPClose() {
        return SharedPreferencesUtils.getBoolean(getContext(), VIP.VIPCLOSE, false);
    }

    public static boolean isGoldVIP() {
        return SharedPreferencesUtils.getBoolean(getContext(), VIP.GOLD, false);
    }

    public static boolean isDiamondVIP() {
        return SharedPreferencesUtils.getBoolean(getContext(), VIP.DIAMOND, false);
    }

    public static boolean isBlackDiamondVIP() {
        return SharedPreferencesUtils.getBoolean(getContext(), VIP.BLACKDIAMOND, false);
    }

    public static boolean isRoyalVIP() {
        return SharedPreferencesUtils.getBoolean(getContext(), VIP.ROYAL, false);
    }

    public static boolean isExtremeVIP() {
        return SharedPreferencesUtils.getBoolean(getContext(), VIP.EXTREME, false);
    }

    public static boolean isLifeLongVIP() {
        return SharedPreferencesUtils.getBoolean(getContext(), VIP.LIFELONG, false);
    }

    public static int getWindowWidth() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getWindowHeight() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int height = manager.getDefaultDisplay().getHeight();
        return height;
    }

    public static void playVideoTrySee(String playurl, String name, String shikan) {
        if (true) {
            String sknum = SharedPreferencesUtils.getString(getContext(), "sknum", "");
            int numSkum = 0;
            try {
                numSkum = Integer.parseInt(sknum);
                if (SharedPreferencesUtils.getInt(getContext(), "havesknum", 0) == 0) {
                    //可以试看电影
                    shiKanVideo(playurl, name, shikan);
                    //说明是第一次试看
                    SharedPreferencesUtils.saveInt(getContext(), "havesknum", 1);
                    //同时保存下来试看的电影名字
                    SharedPreferencesUtils.saveString(getContext(), "shikanName", name + "&");

                } else {
                    //首先查看试看的数量是否已经超过设定值
                    Integer havesknum = SharedPreferencesUtils.getInt(getContext(), "havesknum", 0);
                    if (havesknum >= numSkum) {
                        // 说明超过了试看的设定值了,告诉用户
                        ShowToast.show("试看次数已达到，请开通会员");
                        gotoDialogActivity();
                    } else {
                        //可以试看电影
                        shiKanVideo(playurl, name, shikan);
                        //查看试看的电影是否已经看过了.看过不添加数量
                        String shikanName = SharedPreferencesUtils.getString(getContext(), "shikanName", "");
                        if (!shikanName.contains(name)) {

                            //添加电影名字
                            String newShiKanName = new StringBuilder(shikanName).append(name).append("&").toString();
                            SharedPreferencesUtils.saveString(getContext(), "shikanName", newShiKanName);
                            //试看数量加1
                            SharedPreferencesUtils.saveInt(getContext(), "havesknum", havesknum + 1);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //可以试看电影
            shiKanVideo(playurl, name, shikan);
        }

    }

    public static void gotoDialogActivity() {
        Intent intent = new Intent(getContext(), DiaLogActvivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    public static void gotoDialogActivity(String message) {
        Intent intent = new Intent(getContext(), DiaLogActvivity.class);
        intent.putExtra("exit", message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    private static void shiKanVideo(String playurl, String name, String shikan) {
        try {
            Intent intent = new Intent("cn.d.yurfe.bgc");
            intent.setDataAndType(Uri.parse(playurl), "video/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("playUrl", playurl);
            intent.putExtra("videoName", name);
            intent.putExtra("shikan", shikan);
            getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playVideo(String playurl, String name, String type) {
        if ("gold".equals(type)) {
            if (UIUtils.isGoldVIP()) {
                Intent intent = new Intent("cn.d.yurfe.bgc");
                intent.setDataAndType(Uri.parse(playurl), "video/*");
                intent.putExtra("playUrl", playurl);
                intent.putExtra("videoName", name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            } else {
                gotoDialogActivity();
            }
        } else if ("diamond".equals(type)) {
            if (UIUtils.isDiamondVIP()) {
                movie(playurl, name);
            } else {
                gotoDialogActivity();
            }
        }


    }

    public static void gotoComentActivity(String playurl, String name, String type, String id) {
        Logger.getInstance().e("qw", "UIUtils.gotoComentActivity." + type);
        if (Pass.TRY_PLAY.equals(type)) {
            playVideoTrySee(playurl, name, "true");
        } else if (Pass.CAN_PLAY.equals(type)) {
            movie(playurl, name);
        } else if (Pass.CANNOT_PLAY.equals(type)) {
            AlertCmomment(id);
        }
    }

    private static void AlertCmomment(String id) {
        String dianying_id = id;
        Intent intent = new Intent(getContext(), CommentActivity.class);
        intent.putExtra("movieId", dianying_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    public static void movie(String playurl, String name) {
        try {
            Intent intent = new Intent("cn.d.yurfe.bgc");
            intent.setDataAndType(Uri.parse(playurl), "video/*");
            intent.putExtra("playUrl", playurl);
            intent.putExtra("videoName", name);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getInstallDate() {
        String installDate = "";
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;// 月
        int day = calendar.get(Calendar.DAY_OF_MONTH);// 日
        installDate = String.format("%02d", month) + String.format("%02d", day);
        return installDate;
    }

    /**
     * 获取正确格式的金额
     *
     * @param amount
     * @return
     */
    public static String getMoney(String amount) {
        if (amount == null) {
            return "";
        }
        // 金额转化为分为单位
        String currency = amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.toString();
    }

    public static String getApkDetail() {
        PackageManager packageManager = getContext().getPackageManager();
        try {

            PackageInfo packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
            long firstInstallTime = packageInfo.firstInstallTime;//应用第一次安装的时间
            int versionCode = packageInfo.versionCode;//应用现在的版本号
            String versionName = packageInfo.versionName;//应用现在的版本名称
            System.out.println("firstInstallTime=" + firstInstallTime + ",versionCode=" + versionCode + ",versionName=" + versionName);

            //如下可获得更多信息
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            String name = applicationInfo.name;
            String packageName = applicationInfo.packageName;
            String permission = applicationInfo.permission;
            return String.valueOf(firstInstallTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void gotoMineActivity() {
        Intent intent = new Intent(getContext(), MinePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    public static void gotoVideoUserActivity(String userId, String userName) {
        Intent intent = new Intent(getContext(), PrivateVideoUserActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    public static void gotoVideoUserActivity(String userId, String userName, boolean flag) {
        Intent intent = new Intent(getContext(), PrivateVideoUserActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("havePay", flag);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }


    public static void gotoVideoUserActivity(String userId, String userName, int pos) {
        Intent intent = new Intent(getContext(), PrivateVideoUserActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("pagePos", pos);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    public static void gotoVideoPlayctivity(String userId, String videoId, int videoNum, String name, String url, int pos, String desc) {
        //jumpBySP(userId, videoId, videoNum, name, url, pos, desc);
        jumpByLitePal(userId, videoId, videoNum, name, url, pos, desc, -1);
    }

    public static void gotoVideoPlayctivity(String userId, String videoId, int videoNum, String name, String url, int pos, String desc, int pagePos) {
        //jumpBySP(userId, videoId, videoNum, name, url, pos, desc, pagePos);
        jumpByLitePal(userId, videoId, videoNum, name, url, pos, desc, pagePos);
    }

    private static void jumpByLitePal(final String userId, final String videoId, final int videoNum, final String name, final String url, final int pos, final String desc, final int pagePos) {
        new Thread() {
            @Override
            public void run() {
                int countUser = DataSupport.where("userId=?", userId).count(Users.class);
                int countVideo = DataSupport.where("videoId=?", videoId).count(Videos.class);
                Handler handler = BaseApplication.getHandler();
                if (handler != null) {
                    if (countUser > 0 || countVideo > 0) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                movie(url, name);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                gotoVideoDialogAvtivity(userId, videoId, videoNum, pos, desc, pagePos);
                            }
                        });
                    }
                }
            }
        }.start();

    }

    private static void jumpBySP(String userId, String videoId, int videoNum, String name, String url, int pos, String desc) {
        Boolean aBoolean1 = SharedPreferencesUtils.getBoolean(getContext(), "u" + userId, false);
        Boolean aBoolean2 = SharedPreferencesUtils.getBoolean(getContext(), "v" + videoId, false);
        if (aBoolean1 || aBoolean2) {
            movie(url, name);
        } else {
            gotoVideoDialogAvtivity(userId, videoId, videoNum, pos, desc);
        }
    }


    private static void jumpBySP(String userId, String videoId, int videoNum, String name, String url, int pos, String desc, int pagePos) {
        Boolean aBoolean1 = SharedPreferencesUtils.getBoolean(getContext(), "u" + userId, false);
        Boolean aBoolean2 = SharedPreferencesUtils.getBoolean(getContext(), "v" + videoId, false);
        if (aBoolean1 || aBoolean2) {
            movie(url, name);
        } else {
            gotoVideoDialogAvtivity(userId, videoId, videoNum, pos, desc, pagePos);
        }
    }


    private static void gotoVideoDialogAvtivity(String userId, String videoId, int videoNum) {
        Intent intent = new Intent(getContext(), VideoDiaLogActvivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("videoId", videoId);
        intent.putExtra("videoNum", videoNum);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    private static void gotoVideoDialogAvtivity(String userId, String videoId, int videoNum, int pos, String desc) {
        Intent intent = new Intent(getContext(), VideoDiaLogActvivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("videoId", videoId);
        intent.putExtra("videoNum", videoNum);
        intent.putExtra("pos", pos);
        intent.putExtra("desc", desc);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    private static void gotoVideoDialogAvtivity(String userId, String videoId, int videoNum, int pos, String desc, int pagePos) {
        Intent intent = new Intent(getContext(), VideoDiaLogActvivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("videoId", videoId);
        intent.putExtra("videoNum", videoNum);
        intent.putExtra("pos", pos);
        intent.putExtra("desc", desc);
        intent.putExtra("pagePos", pagePos);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    public static void gotoHaveBeenPayedActivity() {
        Intent intent = new Intent(getContext(), HaveBeenPayedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }
}
