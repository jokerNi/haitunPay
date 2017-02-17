package cn.d.sedfr.fhd.base;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by schwager on 2016/6/27.
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    public abstract void initView();

    public abstract void initListener();

    public abstract void initData();


}
