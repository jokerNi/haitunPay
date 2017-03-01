package cn.d.fesa.wuf.page;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import cn.d.fesa.wuf.R;
import cn.d.fesa.wuf.base.BasePage;
import cn.d.fesa.wuf.net.AccessAddresses;
import cn.d.fesa.wuf.pay.DiaLogActvivity;
import cn.d.fesa.wuf.util.PackageUtil;
import cn.d.fesa.wuf.util.SharedPreferencesUtils;
import cn.d.fesa.wuf.util.UIUtils;

/**
 * Created by schwager on 2016/6/23.
 */
public class MinePage extends BasePage {

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

    public MinePage(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View rootView = UIUtils.inflate(R.layout.layout_mine_page);
        //初始化控件
        mTv_imei = (TextView) rootView.findViewById(R.id.mine_tv_imei);
        mTv_channel = (TextView) rootView.findViewById(R.id.mine_tv_channel);
        mService_qq = (TextView) rootView.findViewById(R.id.mine_tv_customer_service_qq);
        mService_worktime = (TextView) rootView.findViewById(R.id.mine_tv_customer_service_worktime);
        mMine_tv_open_vip = (TextView) rootView.findViewById(R.id.mine_tv_open_vip);
        mMine_tv_vip_level = (TextView) rootView.findViewById(R.id.mine_tv_vip_level);
        mMine_tv_tequan = (TextView) rootView.findViewById(R.id.mine_tv_tequan);
        mMine_tv_version_name = (TextView) rootView.findViewById(R.id.mine_tv_version_name);
        mMine_tv_version_appid = (TextView) rootView.findViewById(R.id.mine_tv_version_appid);
        mQq1 = SharedPreferencesUtils.getString(mContext, "qq1", "");
        mQq2 = SharedPreferencesUtils.getString(mContext, "qq2", "");
        return rootView;
    }

    @Override
    public void initListener() {

        //决定是否显示
        if (TextUtils.isEmpty(mQq1)) {
            mService_qq.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(mQq2)) {
            mService_worktime.setVisibility(View.GONE);
        }

        mMine_tv_open_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoDialog();
            }
        });
    }

    private void gotoDialog() {
        Intent intent = new Intent(mContext, DiaLogActvivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
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

    @Override
    public void onDestroy() {

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
}
