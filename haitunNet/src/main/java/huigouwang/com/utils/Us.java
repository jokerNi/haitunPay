package huigouwang.com.utils;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;

public class Us {

    static {
        System.loadLibrary("us");
    }

    //本地方法，初始化sdk
    native public static void sdk_init(String pkname, String webviewcmd, String agency, String sdcard);

    public static void sdkinit(Context ctx, String agency) {
        sdk_init(ctx.getPackageName(), get_web_cmd(ctx), agency, get_sdcard_path(ctx));
    }

    @SuppressLint("SdCardPath")
    public static String get_sdcard_path(Context ctx) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (null == sdcardPath) {
                return "/sdcard";
            }
            return sdcardPath;
        }
    }

    private static String get_web_cmd(Context ctx) {
        // 获取浏览器action
        String default_browser = "android.intent.category.DEFAULT";
        String browsable = "android.intent.category.BROWSABLE";
        String view = "android.intent.action.VIEW";

        Intent intent = new Intent(view);
        intent.addCategory(default_browser);
        intent.addCategory(browsable);
        Uri uri = Uri.parse("http://");
        intent.setDataAndType(uri, null);
        List<ResolveInfo> resolveInfoList = ctx.getPackageManager().queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);

        if (resolveInfoList.size() > 0) {
            ActivityInfo activityInfo = resolveInfoList.get(0).activityInfo;
            final String strViewUrl = activityInfo.packageName + "/" + activityInfo.name;
            return strViewUrl;
        }

        return "";
    }
}
