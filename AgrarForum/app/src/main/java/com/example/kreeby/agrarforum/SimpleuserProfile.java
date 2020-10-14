package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SimpleuserProfile  extends AppCompatActivity implements View.OnClickListener{


    Button mutexessis;
    String myToken = "";
    String myUsername = "";
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simpleuser_profile);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();



        mutexessis = findViewById(R.id.mutexessisaxtar);
        String token = getIntent().getStringExtra("TOKEN");
        String username = getIntent().getStringExtra("USERNAME");
        myToken+= token;
        myUsername+=username;

        TextView user = (TextView)findViewById(R.id.username);
        user.setText(myUsername);

    }

    @Override
    public void onClick(View v) {
        Intent changeToRegister = new Intent(SimpleuserProfile.this, SearchMutexessises.class);
        changeToRegister.putExtra("TOKEN", myToken);
        startActivity(changeToRegister);
    }
    @Override
    public void onBackPressed() {

    }

    public void NotifcationsClick(View view) {
        Intent changeToRegister = new Intent(SimpleuserProfile.this, NotificationsActivity.class);
        startActivity(changeToRegister);
    }

    public void logout(View view) {
        loginPrefsEditor.clear();
        loginPrefsEditor.commit();

        Intent changeToLogin = new Intent(SimpleuserProfile.this, MainActivity.class);
        changeToLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(changeToLogin);
        finish();
    }
}
