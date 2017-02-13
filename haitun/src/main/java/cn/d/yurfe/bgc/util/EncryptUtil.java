package cn.d.yurfe.bgc.util;

/**
 * Created by BGFVG on 2017/1/23.
 */

public class EncryptUtil {

    public static String encode(int page, long sjc) {
        String key = "";
        key = sjc + PackageUtil.getImei(UIUtils.getContext()) + page + PackageUtil.getChannel(UIUtils.getContext()) + "!@#$%^&*~";
        key = CreateMD5.getMd5(key);
        return key;
    }
    public static String encode(int page, long sjc,String userId) {
        String key = "";
        key = sjc + PackageUtil.getImei(UIUtils.getContext()) + page + PackageUtil.getChannel(UIUtils.getContext()) +userId+ "!@#$%^&*~";
        key = CreateMD5.getMd5(key);
        return key;
    }
}
