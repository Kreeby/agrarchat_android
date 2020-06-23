package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SimpleuserProfile  extends AppCompatActivity implements View.OnClickListener{


    Button mutexessis;
    String myToken = "";
    String myUsername = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simpleuser_profile);


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
}
