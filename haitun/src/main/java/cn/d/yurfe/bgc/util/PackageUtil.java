package cn.d.yurfe.bgc.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 获取package信息的工具类
 *
 * @author QuWang-1
 */
public class PackageUtil {

    /**
     * 返回app包名的版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {

        PackageManager manager = context.getPackageManager();

        try {
            PackageInfo packageInfo = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getPackageName(Context context) {
        String pack = "cn.d.yurfe.bgc";
        try {
            pack = context.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pack;
    }

    /**
     * 获取app的包得版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位
       /* try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }*/
        //使用硬件信息拼凑出来的15位号码
        //return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        Logger.getInstance().e("qw", "PackageUtil.getUniquePsuedoID.Build.MODEL= " + android.os.Build.MODEL);
        return m_szDevIDShort;
    }

    /**
     * 返回手机串号的方法
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        String result = "";
        if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(UIUtils.getContext(), "imei", ""))) {
            return SharedPreferencesUtils.getString(UIUtils.getContext(), "imei", "");
        } else {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                String deviceId;
                if (telephonyManager != null) {

                    deviceId = telephonyManager.getDeviceId();
                    if (deviceId.length() < 14) {
                        deviceId = Settings.Secure.getString(
                                context.getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                    }

                } else {
                    deviceId = Settings.Secure.getString(context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                }
                result = deviceId;
                // TODO: 2016/10/9
                if (deviceId.contains("meid:") && deviceId.contains("imei:")) {
                    String replace = deviceId.replace("meid:", "");
                    result = replace.replace("imei:", "");
                } else if (deviceId.contains("meid:")) {
                    result = deviceId.replace("meid:", "");
                } else if (deviceId.contains("imei:")) {
                    result = deviceId.replace("imei:", "");
                } else if (deviceId.contains(":")) {
                    result = deviceId.replace(":", "");
                } else if (deviceId.contains("meid") && deviceId.contains("imei")) {
                    String replace = deviceId.replace("meid", "");
                    result = replace.replace("imei", "");
                } else if (deviceId.contains("meid")) {
                    result = deviceId.replace("meid", "");
                } else if (deviceId.contains("imei")) {
                    result = deviceId.replace("imei", "");
                }
                if (result.length() >= 16) {
                    result = result.substring(0, 16);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(result)) {
                SharedPreferencesUtils.saveString(UIUtils.getContext(), "imei", getUniquePsuedoID());
                return getUniquePsuedoID();
            } else {
                SharedPreferencesUtils.saveString(UIUtils.getContext(), "imei", result);
                return result;
            }
        }
    }

    /**
     * 返回手机渠道号的方法
     *
     * @param context
     * @return
     */
    public static String getChannel(Context context) {
        /*try {
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (applicationInfo != null) {
                Bundle metaData = applicationInfo.metaData;
                if (metaData != null) {
                    String channel = metaData.getString("channel");
                    return channel;
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;*/

        /*String channelID = SharedPreferencesUtils.getString(context, "channelID", "");
        if (TextUtils.isEmpty(channelID)) {*/
        String channel = getChannelByFile(context);
        return channel;
        /*} else {
            return channelID;
        }*/
    }

    /**
     * 新版本的获取批量打包的渠道号
     *
     * @param context
     * @return
     */
    public static String getChannelByFile(Context context) {
        String channel = "";
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                Log.v("burning", "entryName==" + entryName);
                if (entryName.startsWith("META-INF/mtchannel")) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("_");
        if (split != null && split.length >= 2) {
            channel = ret.substring(split[0].length() + 1);
        } else {
            channel = "100";
        }
        SharedPreferencesUtils.saveString(context, "channelID", channel);
        return channel;
    }

    /**
     * 获取app的包安装毫秒值
     *
     * @return
     */
    public static String getInstallMills() {
        String installDateCurrentMills = SharedPreferencesUtils.getString(UIUtils.getContext(), "installDateCurrentMills", "");
        if (TextUtils.isEmpty(installDateCurrentMills)) {
            String value = String.valueOf(System.currentTimeMillis());
            installDateCurrentMills = value;
            SharedPreferencesUtils.saveString(UIUtils.getContext(), "installDateCurrentMills", value);
        }
        return installDateCurrentMills;
    }

    public static String getApkInstallMills(Context context) {
        byte[] buf = null;
        ZipFile zipfile = null;
        try {
            ApplicationInfo appinfo = context.getApplicationInfo();
            String sourceDir = appinfo.sourceDir;
            zipfile = new ZipFile(sourceDir);//打开ZIP、APK文件
            Enumeration<?> zipEnum = zipfile.entries(); //获取包中的文件入口（所以文件，包括各级目录下）
            while (zipEnum.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipEnum.nextElement();

                if (entry.getName().contains("META-INF") && entry.getName().startsWith("META-INF/mtchannel")) {
                    InputStream input = null;
                    BufferedReader bufferedReader = null;
                    try {
                        input = zipfile.getInputStream(entry);
                        if (input == null) {
                            Logger.getInstance().e("qw", "PackageUtil.getApkInstallMills.流为null");
                            return null;
                        }
                        bufferedReader = new BufferedReader(new InputStreamReader(input));
                        String line = null;
                        if ((line = bufferedReader.readLine()) != null) {
                            return line;
                        }
                    /*InputStream input = null;
                    input = zipfile.getInputStream(entry);
                    int size = input.available();//获取文件大小
                    buf = new byte[size];
                    int ret = 0;
                    do {
                        ret = input.read(buf, 0, size);
                    } while (ret != -1);
                    return new String(buf);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (input != null) {
                            try {
                                input.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            Logger.getInstance().e("qw", "PackageUtil.getApkInstallMills.报错");
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

}
