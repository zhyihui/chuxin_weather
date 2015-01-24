package com.zyh.chuxin.app.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.zyh.chuxin.app.MainActivity;
import com.zyh.chuxin.app.R;

/**
 * Created by zhyh on 2015/1/24.
 */
public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView infoTextView = (TextView) findViewById(R.id.splash_show_info_text);
        infoTextView.setText("正在初始化...");

        Animator animator = ObjectAnimator.ofFloat(infoTextView, "rotationY", 0.0f, 360.0f);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                toMainActivity();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(2000).start();
    }

    private void toMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
