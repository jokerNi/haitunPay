package cn.d.yurfe.bgc.util;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.net.AccessAddresses;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

public class UpdateManager {
    private Context mContext;


    private String DEST;

    private String netVersionName;
    private String apkUrl;
    private String desc;
    private float parseVersionName;
    private String forceUpdate;
    private String CONNECTIONURL;
    private String versionName;
    private String mJsonDomain;

    public UpdateManager(Context mContextl) {
        super();
        this.mContext = mContextl;
        versionName = PackageUtil.getVersionName(mContext);
        parseVersionName = Float.parseFloat(versionName);
        mJsonDomain = getCorrectJsonDomain();
        CONNECTIONURL = mJsonDomain + AccessAddresses.update_Url + PackageUtil.getInstallMills();
        DEST = Environment.getExternalStorageDirectory().getPath()
                + "/" + "huobaotv.apk";
    }

    private String getCorrectJsonDomain() {
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

    // 外部接口让主Activity调用，新建线程检查服务 器版本更新
    public void checkUpdate() {
        new Thread() {
            public void run() {
                checkVersionTask();
            }
        }.start();
    }

    /**
     * 检查版本
     */
    protected void checkVersionTask() {
        OkHttpUtils.get().url(CONNECTIONURL).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(mContext, "您的网络连接有问题" + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                // 解析JSON串
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    netVersionName = jsonObject
                            .getString("versionName");
                    float parseNetVersionName = Float
                            .parseFloat(netVersionName);
                    apkUrl = jsonObject.getString("apkUrl");
                    desc = jsonObject.getString("desc");
                    forceUpdate = jsonObject.getString("update");
                    // 判断版本号是否
                    if (!netVersionName.equals(versionName)) {
                        if (parseVersionName < parseNetVersionName) {
                            // 说明本地版本比服务器小则升级
                            showUpdateDialog();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 显示升级对话框
     */
    protected void showUpdateDialog() {
        //第一次弹出升级对话框 发送统计信息
        try {
            sendUpdataFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Builder builder = new Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setIcon(R.drawable.dialog_icon);
        builder.setTitle("发现新版本:" + netVersionName);
        // 设置描述信息
        builder.setMessage(desc);
        builder.setCancelable(false);
        builder.setPositiveButton("立即升级",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定升级的时候发送二次统计信息
                        try {
                            sendUpdataSecond();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 升级下载APK
                        dialog.dismiss();
                        downLoadApk();
                    }
                });
        if ("off".endsWith(forceUpdate)) {
            builder.setNegativeButton("稍后再说",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // 不升级 则进入主界面
                            // 消失对话框
                            dialog.dismiss();
                        }
                    });
        }
        builder.show();
    }

    public int flagFirst = 1;
    public int flagSecond = 1;

    private void sendUpdataSecond() {
        //点击确定升级的时候发送二次统计信息
        String confirm_Update_Url = mJsonDomain + AccessAddresses.confirm_UpDate_Url + PackageUtil.getInstallMills();
        OkHttpUtils.get().url(confirm_Update_Url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                flagSecond = flagSecond + 1;
                if (flagSecond < 4) {
                    sendUpdataSecond();
                }
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }

    private void sendUpdataFirst() {
        //第一次弹出升级对话框 发送统计信息
        String first_Update_Url = mJsonDomain + AccessAddresses.show_UpDate_Url + PackageUtil.getInstallMills();
        OkHttpUtils.get().url(first_Update_Url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                flagFirst = flagFirst + 1;
                if (flagFirst < 4) {
                    sendUpdataFirst();
                }
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }

    /**
     * 下载apk
     */
    protected void downLoadApk() {
        final ProgressDialog progressDialog = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        // 首先判断有没有SD卡挂载
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 连接网络下载apk文件
            OkHttpUtils//
                    .get()//
                    .url(apkUrl)//
                    .build()//
                    .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "huobaotv.apk")//
                    {
                        @Override
                        public void inProgress(float progress) {
                            // 设置最大进度
                            progressDialog.setMax(100);
                            // 设置当前的进度
                            progressDialog.setProgress((int) (100 * progress));
                        }

                        @Override
                        public void onError(Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(File file) {
                            String absolutePath = file.getAbsolutePath();
                            // 消除进度条对话框
                            progressDialog.dismiss();
                            // 安装最新版本
                            alertInstallDialog(absolutePath);
                            installerApk(absolutePath);
                        }
                    });
        } else {
            // TODO 待处理直接下载到内存卡上面
            Toast.makeText(mContext, "SD卡没有挂载!!!", Toast.LENGTH_SHORT).show();
        }

    }

    private void alertInstallDialog(final String apkPath) {
        AlertDialog alertDialog = new Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)

                .setIcon(R.drawable.dialog_icon)
                .setTitle("下载完成")
                        // 设置描述信息
                .setMessage("请您安装新版本!")

                .setCancelable(false)

                .setPositiveButton("立即安装",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                installerApk(apkPath);
                            }
                        }).create();
        try {

            Field field = alertDialog.getClass().getDeclaredField("mAlert");
            field.setAccessible(true);
            //   获得mAlert变量的值
            Object obj = field.get(alertDialog);
            field = obj.getClass().getDeclaredField("mHandler");
            field.setAccessible(true);
            //   修改mHandler变量的值，使用新的ButtonHandler类
            field.set(obj, new ButtonHandler(alertDialog));
        } catch (Exception e) {
            e.printStackTrace();
        }


      /*  try {
            Field field = alertDialog.getClass()
                    .getSuperclass().getDeclaredField(
                            "mShowing");
            field.setAccessible(true);
            //   将mShowing变量设为false，表示对话框已关闭
            field.set(alertDialog, false);
        } catch (Exception e) {

        }*/
        //   显示对话框
        alertDialog.show();
    }

    /**
     * 安装apk
     *
     * @param path
     */
    protected void installerApk(String path) {
        File apkfile = new File(path);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(new File(DEST)),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    class ButtonHandler extends Handler {

        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            mDialog = new WeakReference<DialogInterface>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog
                            .get(), msg.what);
                    break;
            }
        }
    }

}
