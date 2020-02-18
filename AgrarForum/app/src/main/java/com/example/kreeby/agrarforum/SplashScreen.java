package com.example.kreeby.agrarforum;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class SplashScreen extends Activity {

        Thread splashTread;
        ImageView imageView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splashscreen);
            StartAnimations();

        }

        private void StartAnimations() {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
            anim.reset();

            LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
            l.clearAnimation();
            l.startAnimation(anim);

            anim = AnimationUtils.loadAnimation(this, R.anim.translate);
            anim.reset();

            ImageView iv = (ImageView) findViewById(R.id.splash);
            iv.clearAnimation();
            iv.startAnimation(anim);



            splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        int waited = 0;
                        // Splash screen pause time
                        while (waited < 3500) {
                            sleep(100);
                            waited += 100;
                        }
                        Intent intent = new Intent(SplashScreen.this,
                                MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        SplashScreen.this.finish();
                    } catch (InterruptedException e) {
                        // do nothing
                    } finally {
                        SplashScreen.this.finish();
                    }

                }
            };
            splashTread.start();
        }

    }

