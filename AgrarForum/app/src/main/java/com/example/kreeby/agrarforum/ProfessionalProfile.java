package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ProfessionalProfile extends AppCompatActivity implements View.OnClickListener{


    String myId = "";
    String myGranted = "";
    String myToken = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professional_profile);




        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false);
        }


        String id = getIntent().getStringExtra("ID");
        String granted = getIntent().getStringExtra("GRANTED");
        String token = getIntent().getStringExtra("TOKEN");
        myId += id;
        myToken+=token;
        myGranted += granted;


        Log.d("PROFILE", "MYID" + myId);


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
}
