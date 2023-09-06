package com.david.thegadjetfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.david.thegadjetfinder.activity.LoginActivity;
import com.david.thegadjetfinder.activity.RegisterActivity;
public class MainActivity extends AppCompatActivity {

    private Button mainLoginButton;

    private Button mainRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        mainLoginButton = findViewById(R.id.main_login_button);
        mainRegisterButton = findViewById(R.id.main_register_button);

        mainLoginButton.setOnClickListener(v -> loginActivity());
        mainRegisterButton.setOnClickListener(v->registerActivity());
    }

    public void loginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void registerActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}

