package com.example.app_sepiem;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    private TextView nome, designacao;
    private ImageView logo;
    private View topView1, topView2, topView3;
    private View bottomView1, bottomView2, bottomView3;

    private int count = 0;

    Timer timer;


    @Override
    public void onBackPressed() {


        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else {
            backToast = Toast.makeText(getBaseContext(),"Pressione 'Back' novamente para sair !!!" +
                    "", Toast.LENGTH_LONG);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.nome);
        designacao = findViewById(R.id.designacao);

        logo = findViewById(R.id.logo);

        topView1 = findViewById(R.id.topView1);
        topView2 = findViewById(R.id.topView2);
        topView3 = findViewById(R.id.topView3);

        bottomView1 =findViewById(R.id.bottomView1);
        bottomView2 =findViewById(R.id.bottomView2);
        bottomView3 =findViewById(R.id.bottomView3);

        Animation logoAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_animation);
        Animation nomeAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_animation);

        Animation topView1Animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.top_views_animation);
        Animation topView2Animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.top_views_animation);
        Animation topView3Animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.top_views_animation);

        Animation bottomView1Animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_views_animation);
        Animation bottomView2Animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_views_animation);
        Animation bottomView3Animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_views_animation);

        topView1.startAnimation(topView1Animation);
        bottomView1.startAnimation(bottomView1Animation);

        topView1Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                topView2.setVisibility(View.VISIBLE);
                bottomView2.setVisibility(View.VISIBLE);

                topView2.startAnimation(topView2Animation);
                bottomView2.startAnimation(bottomView2Animation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        topView2Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                topView3.setVisibility(View.VISIBLE);
                bottomView3.setVisibility(View.VISIBLE);

                topView3.startAnimation(topView3Animation);
                bottomView3.startAnimation(bottomView3Animation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        topView3Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                logo.setVisibility(View.VISIBLE);
                logo.startAnimation(logoAnimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        logoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                nome.setVisibility(View.VISIBLE);
                nome.startAnimation(nomeAnimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        nomeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                designacao.setVisibility(View.VISIBLE);
                final String animateTxt = designacao.getText().toString();
                designacao.setText("");
                count = 0;

                new CountDownTimer(animateTxt.length() * 100, 100){
                    @Override
                    public void onTick(long l) {

                        designacao.setText(designacao.getText().toString()+animateTxt.charAt(count));
                        count++;

                    }

                    @Override
                    public void onFinish() {
                    }

                }.start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }
        );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, pesquisarInscricao.class));
            }
        }, 7500);






    }

}