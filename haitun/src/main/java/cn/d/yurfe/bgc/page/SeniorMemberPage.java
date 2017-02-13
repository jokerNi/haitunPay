package cn.d.yurfe.bgc.page;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.d.yurfe.bgc.base.BasePage;

/**
 * 此页面是钻石会员
 * Created by schwager on 2016/6/23.
 */
public class SeniorMemberPage extends BasePage {

    private TextView mTextView;

    public SeniorMemberPage(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        mTextView = new TextView(mContext);
        return mTextView;
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        mTextView.setText("第Senior页");
    }

    @Override
    public void onDestroy() {

    }
}
