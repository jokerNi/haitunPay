package cn.d.fesa.wuf.act;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.d.fesa.wuf.R;
import cn.d.fesa.wuf.adapter.IndicatorAdapter;
import cn.d.fesa.wuf.fragment.AFragment;
import cn.d.fesa.wuf.fragment.BFragment;

public class HaveBeenPayedActivity extends FragmentActivity {

    public ImageButton mTitlebar_ib_left;
    private TabLayout tabs;
    private ViewPager viewPager;
    private List<String> mTitle = new ArrayList<String>();
    private List<Fragment> mFragment = new ArrayList<Fragment>();
    public TextView mTitlebar_tv_name;
    public RelativeLayout mProgress_com_circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have_been_payed);
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        mTitlebar_ib_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        mTitlebar_ib_left = (ImageButton) findViewById(R.id.titlebar_ib_left);
        mTitlebar_tv_name = (TextView) findViewById(R.id.titlebar_tv_name);
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        mProgress_com_circular = (RelativeLayout) findViewById(R.id.progress_com_circular);
        mTitle.add("视频");
        mTitle.add("用户");
        mFragment.add(new AFragment());
        mFragment.add(new BFragment());
    }

    private void initData() {
        mTitlebar_tv_name.setText("已购买记录");
        IndicatorAdapter adapter = new IndicatorAdapter(getSupportFragmentManager(), mTitle, mFragment);
        viewPager.setAdapter(adapter);
        //为TabLayout设置ViewPager
        tabs.setupWithViewPager(viewPager);
        //使用ViewPager的适配器
        tabs.setTabsFromPagerAdapter(adapter);
        mProgress_com_circular.setVisibility(View.GONE);
    }
}
