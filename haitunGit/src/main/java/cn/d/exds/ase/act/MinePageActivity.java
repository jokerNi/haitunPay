package cn.d.exds.ase.act;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import cn.d.exds.ase.R;
import cn.d.exds.ase.base.BaseActivity;
import cn.d.exds.ase.util.PassEvent;
import cn.d.exds.ase.net.AccessAddresses;
import cn.d.exds.ase.util.PackageUtil;
import cn.d.exds.ase.util.Pass;
import cn.d.exds.ase.util.SharedPreferencesUtils;
import cn.d.exds.ase.util.UIUtils;
import de.greenrobot.event.EventBus;

public class MinePageActivity extends BaseActivity {
    @Override
    public void initView() {
        View rootView = UIUtils.inflate(R.layout.layout_mine_page);
        setContentView(rootView);
        EventBus.getDefault().register(this);
        initViews();
        initButtons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initListener() {
        mMine_tv_open_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.gotoDialogActivity();
            }
        });
    }

    @Override
    public void initData() {
        disPlayDeviceInfo();
        if (!UIUtils.isVIP()) {
            initFreeMember();
        } else if (UIUtils.isGoldVIP()) {
            initGoldMember();
        } else if (UIUtils.isDiamondVIP()) {
            initDiamondMember();
        } else if (UIUtils.isBlackDiamondVIP()) {
            initBlackDiamondMember();
        } else if (UIUtils.isRoyalVIP()) {
            initRoyalMember();
        } else if (UIUtils.isExtremeVIP()) {
            initExtremeMember();
        } else if (UIUtils.isLifeLongVIP()) {
            initLifeLongMember();
        }
    }

    public void onEventMainThread(PassEvent event) {
        int pass = event.getPassRessult();
        switch (pass) {
            case Pass.INITFREEMEMBER:
                initFreeMember();
                break;
            case Pass.INITGOLDMEMBER:
                initGoldMember();
                break;
            case Pass.INITDIAMONDMEMBER:
                initDiamondMember();
                break;
            case Pass.INITBLACKDIAMONDMEMBER:
                initBlackDiamondMember();
                break;
            case Pass.INITROYALMEMBER:
                initRoyalMember();
                break;
            case Pass.INITEXTREMEMEMBER:
                initExtremeMember();
                break;
            case Pass.INITLIFELONGMEMBER:
                initLifeLongMember();
                break;
            default:
                break;
        }
    }

    private void disPlayDeviceInfo() {
        mTv_imei.setText(PackageUtil.getImei(UIUtils.getContext()));
        mMine_tv_version_name.setText(PackageUtil.getVersionName(UIUtils.getContext()));
        mTv_channel.setText(PackageUtil.getChannel(UIUtils.getContext()));
        mMine_tv_version_appid.setText(AccessAddresses.app_Id);
        mService_qq.setText(mQq1);
        mService_worktime.setText(mQq2);
    }

    private void initLifeLongMember() {
        mMine_tv_open_vip.setVisibility(View.INVISIBLE);
        mMine_tv_vip_level.setText("终身会员");
        mMine_tv_tequan.setText("观看终身区视频");
    }

    private void initExtremeMember() {
        mMine_tv_open_vip.setVisibility(View.VISIBLE);
        mMine_tv_open_vip.setText("升级终身会员");
        mMine_tv_vip_level.setText("至尊会员");
        mMine_tv_tequan.setText("观看至尊区视频");
    }

    private void initRoyalMember() {
        mMine_tv_open_vip.setVisibility(View.VISIBLE);
        mMine_tv_open_vip.setText("升级至尊会员");
        mMine_tv_vip_level.setText("皇冠会员");
        mMine_tv_tequan.setText("观看皇冠区视频");
    }

    private void initBlackDiamondMember() {
        mMine_tv_open_vip.setVisibility(View.VISIBLE);
        mMine_tv_open_vip.setText("升级皇冠会员");
        mMine_tv_vip_level.setText("黑钻会员");
        mMine_tv_tequan.setText("观看黑钻区视频");
    }

    private void initDiamondMember() {
        mMine_tv_open_vip.setVisibility(View.VISIBLE);
        mMine_tv_open_vip.setText("升级黑钻会员");
        mMine_tv_vip_level.setText("钻石会员");
        mMine_tv_tequan.setText("观看钻石区视频");
    }

    private void initGoldMember() {
        mMine_tv_open_vip.setVisibility(View.VISIBLE);
        mMine_tv_open_vip.setText("升级钻石会员");
        mMine_tv_vip_level.setText("黄金会员");
        mMine_tv_tequan.setText("观看黄金区视频");
    }

    private void initFreeMember() {
        mMine_tv_open_vip.setVisibility(View.VISIBLE);
        mMine_tv_open_vip.setText("开通VIP会员");
        mMine_tv_vip_level.setText("游客");
        mMine_tv_tequan.setText("观看体验视频");
    }

    private TextView mTv_imei;
    private TextView mTv_channel;
    private TextView mService_qq;
    private TextView mService_worktime;
    private TextView mMine_tv_open_vip;
    private String mQq1;
    private String mQq2;
    private TextView mMine_tv_vip_level;
    private TextView mMine_tv_tequan;
    private TextView mMine_tv_version_name;
    private TextView mMine_tv_version_appid;

    private void initViews() {
        //初始化控件
        mTv_imei = (TextView) findViewById(R.id.mine_tv_imei);
        mTv_channel = (TextView) findViewById(R.id.mine_tv_channel);
        mService_qq = (TextView) findViewById(R.id.mine_tv_customer_service_qq);
        mService_worktime = (TextView) findViewById(R.id.mine_tv_customer_service_worktime);
        mMine_tv_open_vip = (TextView) findViewById(R.id.mine_tv_open_vip);
        mMine_tv_vip_level = (TextView) findViewById(R.id.mine_tv_vip_level);
        mMine_tv_tequan = (TextView) findViewById(R.id.mine_tv_tequan);
        mMine_tv_version_name = (TextView) findViewById(R.id.mine_tv_version_name);
        mMine_tv_version_appid = (TextView) findViewById(R.id.mine_tv_version_appid);
        mQq1 = SharedPreferencesUtils.getString(UIUtils.getContext(), "qq1", "");
        mQq2 = SharedPreferencesUtils.getString(UIUtils.getContext(), "qq2", "");
        //决定是否显示
        if (TextUtils.isEmpty(mQq1)) {
            mService_qq.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(mQq2)) {
            mService_worktime.setVisibility(View.GONE);
        }

    }

    private void initButtons() {

    }


}
