package cn.d.yurfe.bgc.util;

import android.widget.Toast;

import cn.d.yurfe.bgc.base.BaseApplication;

/**
 * Created by schwager on 2016/4/19.
 */
public class ShowToast {
    private static Toast mToast;

    public static void show(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.getContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }


}
