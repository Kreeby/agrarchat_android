package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ProfessionalProfileFromSimple extends AppCompatActivity implements View.OnClickListener {

    String hisId = "";
    String myToken = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professional_profile_from_simple);

        String token = getIntent().getStringExtra("TOKEN");

        myToken+= token;
        String id = getIntent().getStringExtra("ID");

        Log.d("VALUES", id);
        hisId += id;
    }






    @Override
    public void onClick(View v) {

        Intent changeToProfile = new Intent(ProfessionalProfileFromSimple.this, QuestionsOfProfessionalFromSimple.class);
//                                changeToProfile.putExtra("ID", myID);
//                                changeToProfile.putExtra("GRANTED", myGranted);
        changeToProfile.putExtra("ID", hisId);
        changeToProfile.putExtra("TOKEN", myToken);
        startActivity(changeToProfile);

    }
}
