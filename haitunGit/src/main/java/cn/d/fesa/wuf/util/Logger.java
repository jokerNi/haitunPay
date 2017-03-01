package cn.d.fesa.wuf.util;

import android.util.Log;

import cn.d.fesa.wuf.R;


/**
 * Created by Administrator on 2015/10/28.
 */
public class Logger {
    private static Logger instance = null;

    public synchronized static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null)
                    instance = new Logger();

            }
        }
        return instance;
    }

    public void e(String tag, String message) {
        if (Config.DEBUG) {
            Log.e(tag, message);
        }
    }
}

