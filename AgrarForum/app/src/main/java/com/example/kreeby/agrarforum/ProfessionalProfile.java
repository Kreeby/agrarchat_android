package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ProfessionalProfile extends AppCompatActivity implements View.OnClickListener{


    String myId = "";
    String myGranted = "";
    String myToken = "";
    String myUsername = "";

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professional_profile);


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();


        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false);
        }


        String id = getIntent().getStringExtra("ID");
        String granted = getIntent().getStringExtra("GRANTED");
        String token = getIntent().getStringExtra("TOKEN");
        String username = getIntent().getStringExtra("USERNAME");
        myId += id;
        myToken+=token;
        myGranted += granted;
        myUsername+=username;

        Log.d("PROFILE", "MYID" + myId);
        TextView user = (TextView)findViewById(R.id.profile_name);
        user.setText(myUsername);

    }

    @Override
    public void onClick(View v) {
        Intent changeToRegister = new Intent(ProfessionalProfile.this, QuestionsOfProfessional.class);
        changeToRegister.putExtra("ID", myId);
        changeToRegister.putExtra("GRANTED", myGranted);
        changeToRegister.putExtra("TOKEN", myToken);
        startActivity(changeToRegister);
    }

    @Override
    public void onBackPressed() {

    }

    public void NotifcationsClick(View view) {
        Intent changeToRegister = new Intent(ProfessionalProfile.this, NotificationsActivity.class);
        startActivity(changeToRegister);
    }

    public void logout(View view) {
        loginPrefsEditor.clear();
        loginPrefsEditor.commit();

        Intent changeToLogin = new Intent(ProfessionalProfile.this, MainActivity.class);
        startActivity(changeToLogin);
        finish();
    }
}
