package cn.d.fesa.wuf.net;

import android.os.Environment;
import android.text.TextUtils;

import com.squareup.okhttp.Request;
import com.switfpass.pay.utils.MD5;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.d.fesa.wuf.util.Logger;

/**
 * Created by BGFVG on 2017/2/27.
 */

public class ImageLoader {
    private static List<String> images = new ArrayList<>();
    public static String baseImageUrl = "http://i.utop.cc/handao_img/app_img/pay_img/";

    public static String imagepSDPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MD5.md5s("bjqw");

    static {
        File file = new File(imagepSDPath);
        if (!file.exists()) {
            file.mkdir();
        }
        initData();
    }

    public static boolean isExist() {
        for (String name : images) {
            String sdFileName = getSDFileName(name);
            File file = new File(imagepSDPath, sdFileName);
            if (!file.exists() || !file.isFile()) {
                return false;
            }
        }
        return true;
    }

    public static void load() {
        if (images != null && images.size() > 0) {
            for (String image : images) {
                String name = getSDFileName(image);
                String absolutePath = imagepSDPath;
                OkHttpUtils.get().url(image).build().execute(new FileCallBack(absolutePath, name) {
                    @Override
                    public void inProgress(float progress) {

                    }

                    @Override
                    public void onError(Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(File response) {
                    }
                });
            }
        }
    }

    private static String getSDFileName(String name) {
        if (!TextUtils.isEmpty(name)) {
            String result = (String) name.subSequence(name.lastIndexOf("/") + 1, name.length());
            return result;
        }
        return "p_top1.png";
    }

    private static void initData() {
        images.add(baseImageUrl + "p_top1.png");
        images.add(baseImageUrl + "p_top2.png");
        images.add(baseImageUrl + "p_top3.png");
        images.add(baseImageUrl + "pay_huangjin_top2.png");
    }
}
