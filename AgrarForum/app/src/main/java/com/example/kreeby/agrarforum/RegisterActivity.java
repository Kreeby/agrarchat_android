package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "ds";
    Spinner sp;
    boolean pressed = false;
    String record = "";
    ArrayAdapter<CharSequence> adapter;


    TextView type;
    EditText username;
    EditText email;
    EditText password;


    Button registerButton;
    String valUN;
    String varEmail;
    String varPass;
    boolean checked;
    int granted = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        registerButton = (Button) findViewById(R.id.registerbutton);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password_reg);
        email = (EditText) findViewById(R.id.email);


        sp = findViewById(R.id.random);
        sp.setVisibility(View.INVISIBLE);

        adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);


        if (pressed == true) {
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    ((TextView) adapterView.getChildAt(0)).setTextSize(12);
                    if (position == 0) {
                    }

                    if (position == 1) {
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valUN = username.getText().toString();
                varEmail = email.getText().toString();
                varPass = password.getText().toString();

                if(pressed == true) {
                    granted = 1;
                }
                else {
                    granted = 0;
                }
                runInBackround(valUN, varEmail, varPass, granted, sp.getSelectedItem().toString());
            }
        });
    }


    private void runInBackround(final String str1, final String str2, final String str3, final int granted, final String str4) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //me    thod containing process logic.
                makeNetworkRequest("http://192.168.99.64:8000/register/", str1, str2, str3, granted, str4);
            }
        }).start();
    }

    private void makeNetworkRequest(String reqUrl, String str1, String str2, String str3, int granted, String category) {
//        Log.d(TAG, "Booking started: ");
        OkHttpClient httpClient = new OkHttpClient();
        String responseString = "";
        Log.d("LOGIN", str1);
        Log.d("LOGIN", str2);
        try {
            RequestBody body = new FormBody.Builder()
                    .add("username", str1)
                    .add("email", str2)
                    .add("password", str3)
                    .add("granted", String.valueOf(granted))
                    .add("category", category)
                    .build();

            Request request = new Request.Builder()
                    .url(reqUrl)
                    .post(body)
                    .build();

            Response response = httpClient
                    .newCall(request)
                    .execute();
            responseString = response.body().string();
            response.body().close();
//            Log.d(TAG, "Booking done: " + responseString);

            // Response node is JSON Object
            JSONObject booked = new JSONObject(responseString);
            JSONObject api = booked.getJSONObject("user");

            final String token = api.getString("api_token");
            final String okNo = booked.getString("status");
            Log.d("LOGIN", token);
            Log.d("LOGIN", okNo);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (okNo.equals("success")) {
                        //display in short period of time
                        Intent login = new Intent(RegisterActivity.this, MainActivity.class);
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                    } else {
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

    public void selectItem(View view) {
        checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checkBox:
                pressed = !pressed;
                if (pressed == true) {
                    sp.setVisibility(View.VISIBLE);
                    granted = 1;
                } else {
                    sp.setVisibility(View.INVISIBLE);
                    granted = 0;
                }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
