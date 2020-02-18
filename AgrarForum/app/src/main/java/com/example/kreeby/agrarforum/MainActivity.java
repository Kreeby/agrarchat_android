package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.squareup.okhttp.MultipartBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = "dsd";
    EditText mail;
    EditText password;
    String valuePass;
    String valueMail;
    Button login;
    TextView myText;
    Button proceed;
    boolean checked;
    static String myID = "";


//    final String ID = "";
    JSONObject obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myText = findViewById(R.id.myText);
        login = findViewById(R.id.goregister);
        login.setOnClickListener(this);


        mail = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);


        proceed = findViewById(R.id.loginbutton);
        proceed.setOnClickListener(this);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                valueMail = mail.getText().toString();
                valuePass = password.getText().toString();



//                if(!isEmailValid(valueMail)) {
//                    mail.setError("Not valid email format");
//                    mail.requestFocus();
//                    return;
//                }

//                if(valuePass.isEmpty()) {
//                    password.setError("Password is required");
//                    password.requestFocus();
//                    return;
//                }


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





    private void runInBackround(final String str1, final String str2){

        new Thread(new Runnable() {
            @Override
            public void run() {
                //me    thod containing process logic.
                makeNetworkRequest("http://192.168.99.64:8000/login/", str1, str2);
            }
        }).start();
    }

    private void makeNetworkRequest(String reqUrl, String str1, String str2) {
//        Log.d(TAG, "Booking started: ");
        OkHttpClient httpClient = new OkHttpClient();
        String responseString = "";
        Log.d("LOGIN", str1);
        Log.d("LOGIN", str2);
        try{
            RequestBody body = new FormBody.Builder()
                    .add("email", str1)
                    .add("password", str2)
                    .build();

            Request request = new Request.Builder()
                    .url(reqUrl)
                    .post(body)
                    .build();

            Response response = httpClient
                    .newCall(request)
                    .execute();
            responseString =  response.body().string();
            response.body().close();
//            Log.d(TAG, "Booking done: " + responseString);

            // Response node is JSON Object
            JSONObject booked = new JSONObject(responseString);
            JSONObject api = booked.getJSONObject("user");

            final String token = api.getString("api_token");
            final String granted = api.getString("granted");
            final String okNo = booked.getString("status");

            final String ID = api.getString("id");
            Log.d("LOGIN", ID);
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if(okNo.equals("success")){
                        //display in short period of time
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                        if(granted.equals("1") || granted.equals("15")) {
                            Intent changeToProfile = new Intent(MainActivity.this, ProfessionalProfile.class);
                            changeToProfile.putExtra("ID", ID);
                            changeToProfile.putExtra("GRANTED", granted);
                            changeToProfile.putExtra("TOKEN", token);
                            startActivity(changeToProfile);


                        }

                        else if (granted.equals("0")) {
                            Intent changeToUser = new Intent(MainActivity.this, SimpleuserProfile.class);
                            changeToUser.putExtra("id", ID);
                            changeToUser.putExtra("GRANTED", granted);
                            changeToUser.putExtra("TOKEN", token);
                            startActivity(changeToUser);
                        }
                    }else{
                        //display in short period of time
                        Toast.makeText(getApplicationContext(), "Login Not Successful", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());

        }

    }




    @Override
    public void onClick(View v) {
        if(v == login) {
                Intent changeToRegister = new Intent(MainActivity.this, RegisterActivity.class);

                startActivity(changeToRegister);
        }

    }


    static public String returnId() {
        Log.d("PROFILE", "is it here?" + myID);
        return myID;
    }
}
