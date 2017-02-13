package cn.d.yurfe.bgc.net;

import cn.d.yurfe.bgc.util.CreateMD5;
import cn.d.yurfe.bgc.util.Logger;
import cn.d.yurfe.bgc.util.PackageUtil;
import cn.d.yurfe.bgc.util.SharedPreferencesUtils;
import cn.d.yurfe.bgc.util.UIUtils;

/**
 * Created by schwager on 2016/6/22.
 */
public class AccessAddresses {
    public static String pay_Id = "htmpabhd";
    public static String app_Id = "7";

    public static final String final_PayDomain = "http://php.qyjuju.com/json2/";
    public static final String final_JsonDomain = "http://php7.qyjuju.com/json2/";
    public static String versionName = PackageUtil.getVersionName(UIUtils.getContext());
    public static String channel = PackageUtil.getChannel(UIUtils.getContext());
    public static String imei = PackageUtil.getImei(UIUtils.getContext());
    public static String pack = PackageUtil.getPackageName(UIUtils.getContext());
    public static String azsj = "";
    public static String submit = "1";
    public static String salary = "1";
    //信息 更新
    public static String commonInformationUrl = "http://php6.qyjuju.com/json2/my.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&v=" + versionName
            + "&channel=" + channel + "&imei=" + imei + "&md5="
            + CreateMD5.getMd5(imei + channel + imei) + "&azsj=" + azsj;

    public static String externalInformationUrl = "http://api.zj66.net/json2/my.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&v=" + versionName
            + "&channel=" + channel + "&imei=" + imei + "&md5="
            + CreateMD5.getMd5(imei + channel + imei) + "&azsj=" + azsj;

    //haitun_notifyUrl
    public static final String haitun_notify_Url = /*payDomain + */"pay/haitun/";
    public static String CALLBACK_URL = /*payDomain + */"pay/mpAlipayWap/mpPayCallback.php?id=";
    public static String aibei_notifyurl = /*payDomain + */"/pay/ipay/";
    //弹出升级的时候提交参数
    public static String show_UpDate_Url = /*jsonDomain +*/ "upstat.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&v=" + versionName
            + "&channel=" + channel + "&imei=" + imei + "&md5="
            + CreateMD5.getMd5(imei + channel + imei) + "&azsj=" + azsj;
    //确认升级的时候提交参数
    public static String confirm_UpDate_Url = /*jsonDomain + */"upstat.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&v=" + versionName
            + "&channel=" + channel + "&imei=" + imei + "&md5="
            + CreateMD5.getMd5(imei + channel + imei) + "&submit=" + submit + "&azsj=" + azsj;
    //之前url参数信息
    public static String vistor1_Url = /*jsonDomain + */"visitor1.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id;
    public static String vistor2_Url = /*jsonDomain + */"visitor2.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";
    public static String vistor3_Url = /*jsonDomain + */"visitor3.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";
    public static String gold1_Url = /*jsonDomain + */"gold1.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";
    public static String gold2_Url = /*jsonDomain + */"gold2.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";
    public static String vr_Url = /*jsonDomain + */"vr.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";
    public static String diamond1_Url = /*jsonDomain + */"diamond1.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";
    public static String diamond2_Url = /*jsonDomain + */"diamond2.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";
    public static String blackdiamond_Url2 = /*jsonDomain + */"blackdiamond.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";
    //submit
    public static String submit_Url = /*jsonDomain + */"pay/sysk/submitorder.php?orderid=";
    //callback
    public static String callback_Url = /*jsonDomain +*/ "pay/sysk/callback.php?orderid=";

    //CommentActivity请求参数
    public static String comment_Url = "comments.php?id=";
    public static String stat_Url = /*jsonDomain + */"stat.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&v=" + versionName
            + "&channel=" + channel + "&imei=" + imei + "&md5="
            + CreateMD5.getMd5(imei + channel + imei) + "&azsj=" + azsj;


    public static String update_Url = /*jsonDomain + */"update.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&v=" + versionName
            + "&channel=" + channel + "&imei=" + imei + "&md5="
            + CreateMD5.getMd5(imei + channel + imei) + "&azsj=" + azsj;
    //
    public static String free_Url = "free.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id;
    public static String living_Url = "living.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";
    public static String confidential_Url = "confidential.php?pay_Id=" + pay_Id + "&package=" + pack + "&channel=" + channel + "&imei=" + imei + "&appid=" + app_Id + "&page=";
    public static String bbs_Url = "bbs.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";

    public static String fragment_a_Url = "buyVideo.php?pay_Id=" + pay_Id + "&package=" + pack
            + "&channel=" + channel + "&imei=" + imei + "&appid=" + app_Id + "&page=";
    public static String fragment_b_Url = "buyPerson.php?pay_Id=" + pay_Id + "&package="
            + pack + "&channel=" + channel + "&imei=" + imei + "&appid=" + app_Id + "&page=";


    //GOLD VIP
    public static String gold_Url = "vipLevel.php?level=1" + "&pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";

    //DIAMOND VIP
    public static String diamond_Url = "vipLevel.php?level=3" + "&pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";

    //BLACK VIP
    public static String blackdiamond_Url = "vipLevel.php?level=5" + "&pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";

    //ROYAL VIP
    public static String royal_Url = "vipLevel.php?level=7" + "&pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";

    //EXTREME VIP
    public static String extreme_Url = "vipLevel.php?level=9" + "&pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";

    //LIFELONG VIP
    public static String lifelong_Url = "vipLevel.php?level=11" + "&pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";

    //ALLTOP VIP
    public static String allVideos_Url = "allVideos.php?pay_Id=" + pay_Id + "&package=" + pack + "&appid=" + app_Id + "&page=";
}
