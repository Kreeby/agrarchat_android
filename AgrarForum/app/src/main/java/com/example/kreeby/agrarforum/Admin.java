package com.example.kreeby.agrarforum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Admin extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        defaultFragment(new SendFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.NavigationBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        Fragment frag = null;

        switch (item.getItemId()) {
            case R.id.item1:
                frag = new ProfileFragment();
                Toast toast = Toast.makeText(getApplicationContext(), "Admin Profile page", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.item2:
                frag = new SendFragment();
                Toast toast1 = Toast.makeText(getApplicationContext(), "Send Materials page", Toast.LENGTH_SHORT);
                toast1.show();
                break;
            case R.id.item3:
                frag = new SettingsFragment();
                Toast toast3 = Toast.makeText(getApplicationContext(), "Send Materials page", Toast.LENGTH_SHORT);
                toast3.show();
                break;
        }
        return defaultFragment(frag);
    }

    private boolean defaultFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_bar, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void onClick(View view) {
        if(view.getId() == R.id.send_notification) {
            Intent changeToSend = new Intent(this, SendNotification.class);
            startActivity(changeToSend);
        }
    }

}
