package cn.d.fesa.wuf.act;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import cn.d.fesa.wuf.R;
import cn.d.fesa.wuf.base.BaseActivity;
import cn.d.fesa.wuf.base.BaseApplication;

public class SplashActivity extends BaseActivity {
    public RelativeLayout mSplash;

    @Override
    public void initView() {
        setContentView(R.layout.activity_splash);
        mSplash = (RelativeLayout) findViewById(R.id.splash);
        //加载启动的动画效果
        Animation animation = AnimationUtils.loadAnimation(BaseApplication.getContext(), R.anim.alpha);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setFillAfter(true);
        mSplash.setAnimation(animation);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
       /* BaseApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 2500);*/
    }
}
