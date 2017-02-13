package cn.d.yurfe.bgc.act;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.squareup.okhttp.Request;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.adapter.MainPageAdapter;
import cn.d.yurfe.bgc.adapter.PrivateVideoPageAdapter;
import cn.d.yurfe.bgc.base.BaseActivity;
import cn.d.yurfe.bgc.base.BaseApplication;
import cn.d.yurfe.bgc.base.BasePage;
import cn.d.yurfe.bgc.page.CommonMemberPage;
import cn.d.yurfe.bgc.page.MinePage;
import cn.d.yurfe.bgc.util.PassEvent;
import cn.d.yurfe.bgc.exit.ExitActivity;
import cn.d.yurfe.bgc.net.AccessAddresses;
import cn.d.yurfe.bgc.page.FreeMemberPage;
import cn.d.yurfe.bgc.page.PrivateVideoMemberPage;
import cn.d.yurfe.bgc.ui.CustomViewPager;
import cn.d.yurfe.bgc.ui.view.LazyViewPager;
import cn.d.yurfe.bgc.util.PackageUtil;
import cn.d.yurfe.bgc.util.Pass;
import cn.d.yurfe.bgc.util.SharedPreferencesUtils;
import cn.d.yurfe.bgc.util.ShowToast;
import cn.d.yurfe.bgc.util.UIUtils;
import cn.d.yurfe.bgc.util.UpdateManager;
import cn.d.yurfe.bgc.util.VIP;
import de.greenrobot.event.EventBus;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class MainActivity extends BaseActivity {

    private CustomViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private MainPageAdapter mAdapter;

    private int[] radios = {R.id.rb_main_first, R.id.rb_main_second, R.id.rb_main_third, R.id.rb_main_forth, R.id.rb_main_fifth};
    private RadioButton rb_first;
    private RadioButton rb_second;
    private RadioButton rb_third;
    private RadioButton rb_forth;
    private RadioButton rb_fifth;
    private boolean isExit = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
                case 1:
                    break;
            }

        }
    };
    private String mJsonDomain;
    private Drawable freeDrawable;
    private Drawable goldDrawable;
    private Drawable diamondDrawable;
    private Drawable blackDiamondDrawable;
    private Drawable royalDrawable;
    private Drawable extremeDrawable;
    private Drawable lifeLongDrawable;
    private Drawable sexyDrawable;
    private Drawable privateDrawable;
    private Drawable mineDrawable;
    private Drawable bbsDrawable;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!UIUtils.isVIP()) {
            if (!isExit) {
                isExit = true;
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                startActivity(new Intent(this, ExitActivity.class));
                ShowToast.show("恭喜您获得优惠大礼包！");
            }
        } else if (UIUtils.isLifeLongVIP()) {
            if (!isExit) {
                isExit = true;
                ShowToast.show("再按一次返回键退出应用");
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                MainActivity.this.finish();
            }
        } else {
            if (!isExit) {
                isExit = true;
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                UIUtils.gotoDialogActivity();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mJsonDomain = getCorrectJsonDomain();
        sendStatisticalData();
        new UpdateManager(this).checkUpdate();
        initViewPager();
        initRadioButton();
        initRadioButtonDrawable();
    }

    @Override
    public void initListener() {
        mViewPager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mAdapter.getmDataList().size(); i++) {
                    BasePage basePage = mAdapter.getmDataList().get(i);
                    if (i == position) {
                        if (basePage != null) {
                            basePage.initData();
                        }
               /*     } else {
                        if (basePage != null) {
                            basePage.onDestroy();
                        }*/
                    }
                }
                mRadioGroup.check(radios[position]);
            }

            public void onPageScrollStateChanged(int state) {
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                changeColor();
                switch (i) {
                    case R.id.rb_main_first:
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_main_second:
                        mViewPager.setCurrentItem(1, false);
                        break;

                    case R.id.rb_main_third:
                        mViewPager.setCurrentItem(2, false);
                        break;

                    case R.id.rb_main_forth:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_main_fifth:
                        mViewPager.setCurrentItem(4, false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        SharedPreferencesUtils.saveBoolean(getApplicationContext(), "try_over", false);
        mAdapter = new MainPageAdapter();
        if (!UIUtils.isVIP()) {
            initFree();
        } else if (UIUtils.isGoldVIP()) {
            initGold();
        } else if (UIUtils.isDiamondVIP()) {
            initDiamond();
        } else if (UIUtils.isBlackDiamondVIP()) {
            initBlackDiamond();
        } else if (UIUtils.isRoyalVIP()) {
            initRoyal();
        } else if (UIUtils.isExtremeVIP()) {
            initExtreme();
        } else if (UIUtils.isLifeLongVIP()) {
            initLifeLong();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //试看结束的时候提示用户
        showWatchMovieCompleteToast();
    }

    public void onEventMainThread(PassEvent event) {
        int pass = event.getPassRessult();
        switch (pass) {
            case Pass.FLUSH_ITEM:
                flushItem(event);
                break;
            case Pass.ALERTDIALOG:
                UIUtils.gotoDialogActivity();
                break;
            case Pass.FLUSH_FREE:
                initFree();
                break;
            case Pass.FLUSH_GOLD:
                initGold();
                break;
            case Pass.FLUSH_DIAMOND:
                initDiamond();
                break;
            case Pass.FLUSH_BLACKDIAMOND:
                initBlackDiamond();
                break;
            case Pass.FLUSH_ROYAL:
                initRoyal();
                break;
            case Pass.FLUSH_EXTREME:
                initExtreme();
                break;
            case Pass.FLUSH_LIFELONG:
                initLifeLong();
                break;
            default:
                break;
        }
    }

    private void flushItem(PassEvent passEvent) {
        int item = 0;
        if (UIUtils.isLifeLongVIP()) {
            item = 1;
        } else {
            item = 2;
        }
        PrivateVideoMemberPage privateVideoMemberPage = (PrivateVideoMemberPage) mAdapter.getmDataList().get(item);
        ((PrivateVideoPageAdapter) privateVideoMemberPage.getAdapter()).updateItemDataNew(passEvent.getPos(), passEvent.getDesc(), passEvent.getUserId(), passEvent.isFlag());
    }

    private void initRadioButtonDrawable() {
        freeDrawable = UIUtils.getDrawable(R.drawable.free_selector);
        goldDrawable = UIUtils.getDrawable(R.drawable.gold_selector);
        diamondDrawable = UIUtils.getDrawable(R.drawable.diamond_selector);
        blackDiamondDrawable = UIUtils.getDrawable(R.drawable.blackdiamond_selector);
        royalDrawable = UIUtils.getDrawable(R.drawable.royal_selector);
        extremeDrawable = UIUtils.getDrawable(R.drawable.extreme_selector);
        lifeLongDrawable = UIUtils.getDrawable(R.drawable.lifelong_selector);
        sexyDrawable = UIUtils.getDrawable(R.drawable.sexyliving_selector);
        privateDrawable = UIUtils.getDrawable(R.drawable.privatevideo_selector);
        mineDrawable = UIUtils.getDrawable(R.drawable.wode_selector);
        bbsDrawable = UIUtils.getDrawable(R.drawable.bbs_selector);
    }

    private void initRadioButton() {
        mRadioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        rb_first = (RadioButton) findViewById(R.id.rb_main_first);
        rb_second = (RadioButton) findViewById(R.id.rb_main_second);
        rb_third = (RadioButton) findViewById(R.id.rb_main_third);
        rb_forth = (RadioButton) findViewById(R.id.rb_main_forth);
        rb_fifth = (RadioButton) findViewById(R.id.rb_main_fifth);
    }

    private void initViewPager() {
        mViewPager = (CustomViewPager) findViewById(R.id.main_viewpager);
    }

    private boolean isFirtIn = SharedPreferencesUtils.getBoolean(BaseApplication.getContext(), "isFirst", false);

    private void sendStatisticalData() {
        if (!isFirtIn) {
            //第一步先去给服务器发送统计信息
            String fisrtInfoUrl = mJsonDomain + AccessAddresses.stat_Url + PackageUtil.getInstallMills();
            try {
                OkHttpUtils.get().url(fisrtInfoUrl).build().execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        SharedPreferencesUtils.saveBoolean(getApplicationContext(), "isFirst", true);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 切换菜单字体的颜色
     */
    private void changeColor() {
        int childCount = mRadioGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RadioButton childAt = (RadioButton) mRadioGroup.getChildAt(i);
            if (childAt.isChecked()) {
                childAt.setTextColor(getResources().getColor(R.color.selectRadioButton));
            } else {
                childAt.setTextColor(getResources().getColor(R.color.normalRadioButton));
            }
        }
    }


    private void fillFree() {
        clearAdapter();
        mAdapter.add(new FreeMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.free_Url, Pass.TRY_PLAY, VIP.FREENAME));
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.gold_Url, Pass.CANNOT_PLAY, true, VIP.GOLDNAME));
        fillCommonFixedPages();
        invalidateFlushPages();
    }

    private void fillGold() {
        clearAdapter();
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.gold_Url, Pass.CAN_PLAY, false, VIP.GOLDNAME));
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.diamond_Url, Pass.CANNOT_PLAY, true, VIP.DIAMONDNAME));
        fillCommonFixedPages();
        invalidateFlushPages();
    }

    private void fillDiamond() {
        clearAdapter();
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.diamond_Url, Pass.CAN_PLAY, false, VIP.DIAMONDNAME));
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.blackdiamond_Url, Pass.CANNOT_PLAY, true, VIP.BLACKDIAMONDNAME));
        fillCommonFixedPages();
        invalidateFlushPages();
    }

    private void fillBlackDiamond() {
        clearAdapter();
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.blackdiamond_Url, Pass.CAN_PLAY, false, VIP.BLACKDIAMONDNAME));
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.royal_Url, Pass.CANNOT_PLAY, true, VIP.ROYALNAME));
        fillCommonFixedPages();
        invalidateFlushPages();
    }

    private void fillRoyal() {
        clearAdapter();
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.royal_Url, Pass.CAN_PLAY, false, VIP.ROYALNAME));
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.extreme_Url, Pass.CANNOT_PLAY, true, VIP.EXTREMENAME));
        fillCommonFixedPages();
        invalidateFlushPages();
    }

    private void fillExtreme() {
        clearAdapter();
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.extreme_Url, Pass.CAN_PLAY, false, VIP.EXTREMENAME));
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.lifelong_Url, Pass.CANNOT_PLAY, true, VIP.LIFELONGNAME));
        fillCommonFixedPages();
        invalidateFlushPages();
    }

    private void fillLifeLong() {
        clearAdapter();
        mAdapter.add(new CommonMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.allVideos_Url, Pass.CAN_PLAY, false, VIP.LIFELONGNAME));
        fillCommonFixedPages();
        invalidateFlushPages();
    }


    private void fillCommonFixedPages() {
        /*mAdapter.add(new LivingMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.vistor3_Url, "wm", false));
        mAdapter.add(new PrivateVideoMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.confidential_Url));
        mAdapter.add(new BBSMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.vistor3_Url, "wm", false));*/
        mAdapter.add(new PrivateVideoMemberPage(getApplicationContext(), mJsonDomain + AccessAddresses.confidential_Url));
        mAdapter.add(new MinePage(getApplicationContext()));
        //设置适配器
        mViewPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private void showAllRadioButtons() {
        rb_first.setVisibility(View.VISIBLE);
        rb_second.setVisibility(View.VISIBLE);
        rb_third.setVisibility(View.VISIBLE);
        rb_forth.setVisibility(View.VISIBLE);
        rb_fifth.setVisibility(View.GONE);
    }

    private void showLastRadioButtons() {
        rb_first.setVisibility(View.VISIBLE);
        rb_second.setVisibility(View.VISIBLE);
        rb_third.setVisibility(View.VISIBLE);
        rb_forth.setVisibility(View.GONE);
        rb_fifth.setVisibility(View.GONE);
    }

    private String getCorrectJsonDomain() {
        String jsonDomain = SharedPreferencesUtils.getString(UIUtils.getContext(), "jsonDomain", "");
        if (!TextUtils.isEmpty(jsonDomain)) {
            if (jsonDomain.startsWith("http://")) {
                if (jsonDomain.endsWith("/")) {
                    if (jsonDomain.contains(".")) {
                        return jsonDomain;
                    } else {
                    }
                } else {
                }
            } else {
            }
        } else {
        }
        return AccessAddresses.final_JsonDomain;
    }


    private void initFree() {
        initFreeVIPRadioButton();
        fillFree();
    }


    private void initGold() {
        initGoldVIPRadioButton();
        fillGold();
    }

    private void initDiamond() {
        initDiamondVIPRadioButton();
        fillDiamond();
    }

    private void initBlackDiamond() {
        initBalackDiamondVIPRadioButton();
        fillBlackDiamond();
    }

    private void initRoyal() {
        initRoyalVIPRadioButton();
        fillRoyal();
    }

    private void initExtreme() {
        initEXtremeVIPRadioButton();
        fillExtreme();
    }

    private void initLifeLong() {
        initLifeLongVIPRadioButton();
        fillLifeLong();
    }

    private void initFreeVIPRadioButton() {
        rb_first.setCompoundDrawablesWithIntrinsicBounds(null, freeDrawable, null, null);
        rb_first.setText(getResources().getText(R.string.freearea));
        rb_second.setCompoundDrawablesWithIntrinsicBounds(null, goldDrawable, null, null);
        rb_second.setText(getResources().getText(R.string.goldarea));
        showAllRadioButtons();
        setCommonRadioButton();
    }

    private void initGoldVIPRadioButton() {
        rb_first.setCompoundDrawablesWithIntrinsicBounds(null, goldDrawable, null, null);
        rb_first.setText(getResources().getText(R.string.goldarea));
        rb_second.setCompoundDrawablesWithIntrinsicBounds(null, diamondDrawable, null, null);
        rb_second.setText(getResources().getText(R.string.diamondarea));
        showAllRadioButtons();
        setCommonRadioButton();
    }

    private void initDiamondVIPRadioButton() {
        rb_first.setCompoundDrawablesWithIntrinsicBounds(null, diamondDrawable, null, null);
        rb_first.setText(getResources().getText(R.string.diamondarea));
        rb_second.setCompoundDrawablesWithIntrinsicBounds(null, blackDiamondDrawable, null, null);
        rb_second.setText(getResources().getText(R.string.blackdiamondarea));
        showAllRadioButtons();
        setCommonRadioButton();
    }

    private void initBalackDiamondVIPRadioButton() {
        rb_first.setCompoundDrawablesWithIntrinsicBounds(null, blackDiamondDrawable, null, null);
        rb_first.setText(getResources().getText(R.string.blackdiamondarea));
        rb_second.setCompoundDrawablesWithIntrinsicBounds(null, royalDrawable, null, null);
        rb_second.setText(getResources().getText(R.string.royalarea));
        showAllRadioButtons();
        setCommonRadioButton();
    }

    private void initRoyalVIPRadioButton() {
        rb_first.setCompoundDrawablesWithIntrinsicBounds(null, royalDrawable, null, null);
        rb_first.setText(getResources().getText(R.string.royalarea));
        rb_second.setCompoundDrawablesWithIntrinsicBounds(null, extremeDrawable, null, null);
        rb_second.setText(getResources().getText(R.string.extremearea));
        showAllRadioButtons();
        setCommonRadioButton();
    }

    private void initEXtremeVIPRadioButton() {
        rb_first.setCompoundDrawablesWithIntrinsicBounds(null, extremeDrawable, null, null);
        rb_first.setText(getResources().getText(R.string.extremearea));
        rb_second.setCompoundDrawablesWithIntrinsicBounds(null, lifeLongDrawable, null, null);
        rb_second.setText(getResources().getText(R.string.lifelongarea));
        showAllRadioButtons();
        setCommonRadioButton();
    }


    private void initLifeLongVIPRadioButton() {
        rb_first.setCompoundDrawablesWithIntrinsicBounds(null, lifeLongDrawable, null, null);
        rb_first.setText(getResources().getText(R.string.lifelongarea));
        showLastRadioButtons();
        setLastRadioButton();
    }

    private void setLastRadioButton() {
       /* rb_second.setCompoundDrawablesWithIntrinsicBounds(null, sexyDrawable, null, null);
        rb_second.setText(getResources().getText(R.string.sexylivingarea));
        rb_third.setCompoundDrawablesWithIntrinsicBounds(null, privateDrawable, null, null);
        rb_third.setText(getResources().getText(R.string.privatearea));
        rb_forth.setCompoundDrawablesWithIntrinsicBounds(null, bbsDrawable, null, null);
        rb_forth.setText(getResources().getText(R.string.bbsarea));*/
        rb_second.setCompoundDrawablesWithIntrinsicBounds(null, privateDrawable, null, null);
        rb_second.setText(getResources().getText(R.string.privatearea));
        rb_third.setCompoundDrawablesWithIntrinsicBounds(null, mineDrawable, null, null);
        rb_third.setText(getResources().getText(R.string.wode));
    }

    private void setCommonRadioButton() {
    /*    rb_third.setCompoundDrawablesWithIntrinsicBounds(null, sexyDrawable, null, null);
        rb_third.setText(getResources().getText(R.string.sexylivingarea));
        rb_forth.setCompoundDrawablesWithIntrinsicBounds(null, privateDrawable, null, null);
        rb_forth.setText(getResources().getText(R.string.privatearea));
        rb_fifth.setCompoundDrawablesWithIntrinsicBounds(null, bbsDrawable, null, null);
        rb_fifth.setText(getResources().getText(R.string.bbsarea));*/
        rb_third.setCompoundDrawablesWithIntrinsicBounds(null, privateDrawable, null, null);
        rb_third.setText(getResources().getText(R.string.privatearea));
        rb_forth.setCompoundDrawablesWithIntrinsicBounds(null, mineDrawable, null, null);
        rb_forth.setText(getResources().getText(R.string.wode));
    }

    private void invalidateFlushPages() {
        //showSecondPage();
        //在此刷新默认界面
        showFirstPage();
    }

    private void showFirstPage() {
        mViewPager.setCurrentItem(0);
        mRadioGroup.check(R.id.rb_main_first);
        mAdapter.getmDataList().get(0).initData();
    }

    private void showSecondPage() {
        mViewPager.setCurrentItem(1);
        mRadioGroup.check(R.id.rb_main_second);
        mAdapter.getmDataList().get(1).initData();
    }

    private void showWatchMovieCompleteToast() {
        if (SharedPreferencesUtils.getBoolean(getApplicationContext(), "try_over", false)) {
            UIUtils.gotoDialogActivity();
            ShowToast.show("试看结束，开通会员无限制观看！");
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), "try_over", false);
        } else if (SharedPreferencesUtils.getBoolean(getApplicationContext(), "gold_over", false)) {
            UIUtils.gotoDialogActivity();
            ShowToast.show("升级钻石会员无限制观看");
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), "gold_over", false);
        } else if (SharedPreferencesUtils.getBoolean(getApplicationContext(), "diamond_over", false)) {
            UIUtils.gotoDialogActivity();
            ShowToast.show("一键解锁无限制观看");
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), "diamond_over", false);
        } else if (SharedPreferencesUtils.getBoolean(getApplicationContext(), "blackdiamond_over", false)) {
            UIUtils.gotoDialogActivity();
            ShowToast.show("升级黑钻会员无限制观看");
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), "blackdiamond_over", false);
        } else if (SharedPreferencesUtils.getBoolean(getApplicationContext(), "royal_over", false)) {
            UIUtils.gotoDialogActivity();
            ShowToast.show("升级皇冠会员无限制观看");
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), "royal_over", false);
        } else if (SharedPreferencesUtils.getBoolean(getApplicationContext(), "extreme_over", false)) {
            UIUtils.gotoDialogActivity();
            ShowToast.show("升级至尊会员无限制观看");
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), "extreme_over", false);
        } else if (SharedPreferencesUtils.getBoolean(getApplicationContext(), "lifelong_over", false)) {
            UIUtils.gotoDialogActivity();
            ShowToast.show("升级终身会员无限制观看");
            SharedPreferencesUtils.saveBoolean(getApplicationContext(), "lifelong_over", false);
        }
    }

    private void clearAdapter() {
        if (mAdapter != null) {
            mAdapter.clear();
        }
    }
}

