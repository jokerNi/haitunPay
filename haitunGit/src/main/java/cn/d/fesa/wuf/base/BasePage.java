package cn.d.fesa.wuf.base;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.d.fesa.wuf.R;
import cn.d.fesa.wuf.act.MinePageActivity;
import cn.d.fesa.wuf.util.UIUtils;

/**
 * Created by schwager on 2016/6/23.
 */
public abstract class BasePage {


    protected Context mContext;

    protected View mView;
    protected TextView mTitle;
    protected ImageButton mLeft;
    protected ImageButton mRight;

    public BasePage(Context context) {
        this.mContext = context;
        this.mView = initView();
        initTitle();
        initListener();
    }

    protected void initTitle() {
        mTitle = (TextView) mView.findViewById(R.id.titlebar_tv_title);
        mLeft = (ImageButton) mView.findViewById(R.id.titlebar_ib_left);
        mRight = (ImageButton) mView.findViewById(R.id.titlebar_ib_right);
        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.gotoMineActivity();
            }
        });
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.gotoDialogActivity();
            }
        });
        mLeft.setVisibility(View.GONE);
        mRight.setVisibility(View.GONE);
        if (UIUtils.isLifeLongVIP()) {
            mRight.setVisibility(View.GONE);
        }
    }

    public abstract View initView();

    public abstract void initListener();

    public abstract void initData();

    public abstract void onDestroy();

    public View getRootView() {
        return mView;
    }
}
