package cn.d.yurfe.bgc.page;

import android.content.Context;
import android.view.View;
import android.widget.GridView;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.base.BasePage;
import cn.d.yurfe.bgc.util.UIUtils;

/**
 * Created by BGFVG on 2017/1/20.
 */

public class LivingMemberPage extends BasePage {


    private View rootView;
    private GridView mGridView;

    public LivingMemberPage(Context context, String url, String type, boolean isShow) {
        super(context);
    }

    @Override
    public View initView() {
        rootView = UIUtils.inflate(R.layout.layout_living_page);
        mGridView = (GridView) rootView.findViewById(R.id.video_main_gv);
        return rootView;
    }

    @Override
    public void initListener() {
        /*mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });*/
    }

    @Override
    public void initData() {
        mTitle.setText(UIUtils.getContext().getResources().getText(R.string.sexylivingarea));
    }

    @Override
    public void onDestroy() {

    }
}
