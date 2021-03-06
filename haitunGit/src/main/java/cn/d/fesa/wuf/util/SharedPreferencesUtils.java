package cn.d.fesa.wuf.util;

import android.content.Context;
import android.content.SharedPreferences;

import javax.xml.validation.Validator;

/**
 * Created by schwager on 2016/4/20.
 */
public class SharedPreferencesUtils {

    private static final String SP_NAME = "config";
    private static SharedPreferences sp;

    public static void saveString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        sp.edit().putString(key, value).commit();

    }


    public static void saveInt(Context context, String key, Integer value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        sp.edit().putInt(key, value).commit();
    }

    public static Integer getInt(Context context, String key, Integer defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        return sp.getInt(key, defValue);
    }

    public static void saveBoolean(Context context, String key, Boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        return sp.getString(key, defValue);
    }

    public static Boolean getBoolean(Context context, String key, Boolean defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }

        return sp.getBoolean(key, defValue);
    }


}
