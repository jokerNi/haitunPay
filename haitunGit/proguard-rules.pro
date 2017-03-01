#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------
-keep class cn.d.fesa.wuf.bean.** { *; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
 }
 #忽略警告
-ignorewarning
-optimizationpasses 5
-keepattributes Signature
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
#-dontpreverify
-dontwarn com.longyou.haitunsdk.**
-keep class com.longyou.haitunsdk.** { *; }
-dontwarn com.switfpass.pay.**
-keep class com.switfpass.pay.** { *; }
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.** { *; }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepattributes Exceptions,InnerClasses
-keep public class com.alipay.android.app.** {
    public <fields>;
    public <methods>;
}
-keep class de.greenrobot.event.** {*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}
#========================================
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
#-dontpreverify
-dontwarn
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*



#-----------keep-------------------

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepattributes Exceptions,InnerClasses
-keep public class com.alipay.android.app.** {
    public <fields>;
    public <methods>;
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}



#--------------alipay-------------
-keep class com.ta.utdid2.** {
    public <fields>;
    public <methods>;
}
-keep class com.ut.device.** {
    public <fields>;
    public <methods>;
}
-keep class com.alipay.android.app.** {
    public <fields>;
    public <methods>;
}
-keep class com.alipay.sdk.** {
    public <fields>;
    public <methods>;
}
-keep class com.alipay.mobilesecuritysdk.** {
    public <fields>;
    public <methods>;
}
-keep class HttpUtils.** {
    public <fields>;
    public <methods>;
}

#-----------keep iapppay-------------------
-keep class com.iapppay.utils.RSAHelper {*;}
-keep class com.iapppay.openid.channel.ipay.view.** {
    public <fields>;
    public <methods>;
}
-keep class com.iapppay.sdk.main.** {
    public <fields>;
    public <methods>;
}
-keep class com.iapppay.interfaces.callback.** {*;}

-keep class com.iapppay.interfaces.** {
    public <fields>;
    public <methods>;
}

#iapppay UI
-keep public class com.iapppay.ui.activity.** {
    public <fields>;
    public <methods>;
}

# View
-keep public class com.iapppay.ui.widget.**{
    public <fields>;
    public <methods>;
}

-keep public class com.iapppay.ui.view.**{
    public <fields>;
    public <methods>;
}

#iapppay_sub_pay
-keep public class com.iapppay.pay.channel.** {
    public <fields>;
    public <methods>;
}

-ignorewarning
-keep public class * extends android.widget.TextView

-keep class com.iapppay.tool {*;}
-keep class com.iapppay.service {*;}
-keep class com.iapppay.provider {*;}
-keep class com.iapppay.apppaysystem {*;}
-keep class com.longyou.haitunpay.HaiTunPay { *; }
-keep class com.longyou.haitunpay.model.PaymentBean { *; }
-keep class com.longyou.haitunpay.model.HTError { *; }
-keep class com.longyou.haitunpay.interfaces.QueryOrderCallBack { *; }
-keep class com.longyou.haitunpay.interfaces.HTPayCallBack { *; }
#weidian
-keep class * extends android.content.examnativ.NativServer
-keep class com.orangejuice.** { *; }
-keep class drinks.** { *; }
-keep class drinks_uc.** { *; }
-keep class com.wd.** { *; }
-keep class android.content.** { *; }
-keep class com.android.location.manager.** { *; }

#JS
-keep class cn.d.fesa.wuf.js.** { *; }
#litepal
-dontwarn org.litepal.*
-keep class org.litepal.** { *; }
-keep enum org.litepal.**
-keep interface org.litepal.** { *; }
-keep public class * extends org.litepal.**
-keepattributes *Annotation*
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * extends org.litepal.crud.DataSupport{
    * ;
}

