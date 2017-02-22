package cn.d.sedfr.fhd.util;

import android.widget.Toast;

import cn.d.sedfr.fhd.base.BaseApplication;

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
