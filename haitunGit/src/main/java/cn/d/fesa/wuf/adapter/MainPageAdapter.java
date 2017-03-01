package cn.d.fesa.wuf.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.d.fesa.wuf.base.BasePage;
import cn.d.fesa.wuf.ui.CustomViewPager;

/**
 * Created by schwager on 2016/4/18.
 */
public class MainPageAdapter extends PagerAdapter {


    private List<BasePage> mDataList;

    public MainPageAdapter() {
        this.mDataList = new ArrayList<>();
    }

    public void add(BasePage basePage) {
        //初始化数据
        mDataList.add(basePage);
    }

    public void reSet(int pos, BasePage basePage) {
        mDataList.set(pos, basePage);
    }
    public List<BasePage> getmDataList() {
        return mDataList;
    }

    public void clear(){
        mDataList.clear();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View rootView = mDataList.get(position).getRootView();
        container.addView(rootView);
        return rootView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((CustomViewPager) container).removeView(mDataList.get(position).getRootView());
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
