package com.example.kreeby.agrarforum;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kreeby.agrarforum.NetworkHandler.NetworkUtils;
import com.example.kreeby.agrarforum.NetworkHandler.OkHttpListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;


public class SplashScreen extends Activity {

        Thread splashTread;
        ImageView imageView;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private OkHttpListener listener;

    boolean logged_in = false;
    //
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splashscreen);

            listener = new OkHttpListener() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e(LOG_TAG, e.toString());
                }

                @Override
                public void onResponse(Response response) throws IOException, JSONException {
                    String responseBody = response.body().string();
                    Log.i(LOG_TAG, responseBody);


                    JSONObject booked = new JSONObject(responseBody);

                    final String okNo = booked.getString("status");

                    if (okNo.equals("error")) {




                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Login Not Successful",Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        JSONObject api = booked.getJSONObject("user");

                        final String token = api.getString("api_token");
                        final String granted = api.getString("granted");
                        final String nickname = api.getString("username");
                        final String ID = api.getString("id");

                        Log.d("splash login: ", ID);
                        runOnUiThread(new Runnable() {
                            public void run() {

                                //display in short period of time
                              //  Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();

                                login(granted, ID, token, nickname);
                                SplashScreen.this.finish();


                            }
                        });
                    }

                }
            };


            loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            loginPrefsEditor = loginPreferences.edit();

            logged_in = loginPreferences.getBoolean("saveLogin",false);
            Log.wtf("preference: ", logged_in + "");

            StartAnimations();


        }



    private void login(String granted, String ID, String token, String nickname) {
            logged_in = true;
        Intent changeToProfile = new Intent(SplashScreen.this, granted.equals("1") ? ProfessionalProfile.class : SimpleuserProfile.class);

        changeToProfile.putExtra("ID", ID);
        changeToProfile.putExtra("GRANTED", granted);
        changeToProfile.putExtra("TOKEN", token);
        changeToProfile.putExtra("USERNAME", nickname);
        startActivity(changeToProfile);
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
                Intent intent = new Intent(SplashScreen.this,
                                    MainActivity.class);
                @Override
                public void run() {
                    try {
                        if(!logged_in) {
                            Log.d("heree","logged_in");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                            int waited = 0;
                            // Splash screen pause time
                            while (waited < 3500) {
                                sleep(100);
                                waited += 100;
                            }
                            startActivity(intent);
                            SplashScreen.this.finish();

                        }else {
                            String url = "login/";

                            RequestBody body = new FormBody.Builder()
                                    .add("email", loginPreferences.getString("username", ""))
                                    .add("password", loginPreferences.getString("password", ""))
                                    .build();

                            int waited = 0;
                            // Splash screen pause time
                            while (waited < 3500) {
                                sleep(100);
                                waited += 100;
                            }


                            NetworkUtils.getData(url, body, listener);



                        }


                    } catch (InterruptedException e) {
                        // do nothing
                    } finally {
                    }

                }
            };
            splashTread.start();
        }

    }

