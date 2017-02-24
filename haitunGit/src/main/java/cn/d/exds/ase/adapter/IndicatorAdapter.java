package cn.d.exds.ase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.d.exds.ase.fragment.AFragment;
import cn.d.exds.ase.fragment.BFragment;

/**
 * 项目名称：FragmentManager3
 * 类名称：MyAdapter
 * 类描述：
 * 创建人：lc
 * 创建时间：2015-3-3 下午4:20:26
 * 修改人：131
 * 修改时间：2015-3-3 下午4:20:26
 * 修改备注：
 */
public class IndicatorAdapter extends FragmentPagerAdapter {
    private List<String> title;
    private List<Fragment> views;

    public IndicatorAdapter(FragmentManager fm, List<String> title, List<Fragment> views) {
        super(fm);
        this.title = title;
        this.views = views;
    }

    @Override
    public Fragment getItem(int position) {
        return views.get(position);
    }

    @Override
    public int getCount() {
        return views.size();
    }


    //配置标题的方法
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

}


