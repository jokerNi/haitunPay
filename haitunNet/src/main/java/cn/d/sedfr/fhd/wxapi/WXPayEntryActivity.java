package cn.d.sedfr.fhd.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.d.sedfr.fhd.util.PassEvent;
import cn.d.sedfr.fhd.util.Logger;
import cn.d.sedfr.fhd.util.Pass;

import com.longyou.haitunsdk.HaiTunPay;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

/**
 * 微信支付回调类
 * Created by CharryLi on 16/7/25.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, HaiTunPay.getInstance().getAppId_wechat());//appid需换成商户自己开放平台appid
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int pay = Pass.PAY_IDEL;
            if (resp.errCode == 0) {
                pay = Pass.PAY_SUCCESS;
                Logger.getInstance().e("qw", "WXPayEntryActivity.onResp.支付成功");
            } else if (resp.errCode == -2) {
                pay = Pass.PAY_CANCEL;
                Logger.getInstance().e("qw", "WXPayEntryActivity.onResp.支付取消");
            } else {
                pay = Pass.PAY_FAIL;
                Logger.getInstance().e("qw", "WXPayEntryActivity.onResp.支付失败");
            }
            EventBus.getDefault().post(new PassEvent(pay));
            WXPayEntryActivity.this.finish();
        }
    }


}
