package com.example.kreeby.agrarforum;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kreeby.agrarforum.NetworkHandler.NetworkUtils;
import com.example.kreeby.agrarforum.NetworkHandler.OkHttpListener;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Application application;
    private static final String TAG = "dsd";
    EditText mail;
    EditText password;
    String valuePass;
    String valueMail;
    Button login;
    TextView myText;
    Button proceed;
    boolean checked;
    private CheckBox saveLoginCheckBox;
    static String myID = "";
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private OkHttpListener listener;
    //    final String ID = "";
    JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

                    Log.d("LOGIN", ID);
                    runOnUiThread(new Runnable() {
                        public void run() {

                            if (saveLoginCheckBox.isChecked() && valueMail != null) {
                                loginPrefsEditor.putBoolean("saveLogin", true);
                                loginPrefsEditor.putString("username", valueMail);
                                loginPrefsEditor.putString("password", valuePass);
                                loginPrefsEditor.commit();
                            } else if(!saveLoginCheckBox.isChecked()){
                                loginPrefsEditor.clear();
                                loginPrefsEditor.commit();
                            }

                            //display in short period of time
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();

                            login(granted, ID, token, nickname);

                        }
                    });
                }

            }
        };

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        OneSignal.startInit(this).inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationReceivedHandler(new NotificationHandler())
                .init();
        //myText = findViewById(R.id.myText);
        login = findViewById(R.id.goregister);
        login.setOnClickListener(this);


        mail = findViewById(R.id.login);
        password = findViewById(R.id.password);
        saveLoginCheckBox = findViewById(R.id.saveLoginCheckBox);

        proceed = findViewById(R.id.loginbutton);
        proceed.setOnClickListener(this);


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin == true) {
            mail.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);

            String url = "login/";

            RequestBody body = new FormBody.Builder()
                    .add("email", loginPreferences.getString("username", ""))
                    .add("password", loginPreferences.getString("password", ""))
                    .build();

            NetworkUtils.getData(url, body, listener);

        }

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                valueMail = mail.getText().toString();
                valuePass = password.getText().toString();


                if (!isEmailValid(valueMail)) {
                    mail.setError("Not valid email format");
                    mail.requestFocus();
                    return;
                }

                if (valuePass.isEmpty()) {
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }


                runInBackround(valueMail, valuePass);


            }
        });


    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void runInBackround(final String str1, final String str2) {

                String url = "login/";

                RequestBody body = new FormBody.Builder()
                        .add("email", str1)
                        .add("password", str2)
                        .build();

                NetworkUtils.getData(url, body, listener);

    }

    private void login(String granted, String ID, String token, String nickname) {
        Intent changeToProfile = new Intent(MainActivity.this, granted.equals("1") ? ProfessionalProfile.class : SimpleuserProfile.class);

        changeToProfile.putExtra("ID", ID);
        changeToProfile.putExtra("GRANTED", granted);
        changeToProfile.putExtra("TOKEN", token);
        changeToProfile.putExtra("USERNAME", nickname);
        startActivity(changeToProfile);
    }


    @Override
    public void onClick(View v) {
        if (v == login) {
            Intent changeToRegister = new Intent(MainActivity.this, RegisterActivity.class);

            startActivity(changeToRegister);
        }

    }

}





