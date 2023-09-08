package com.david.thegadjetfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.david.thegadjetfinder.activity.LoginActivity;
import com.david.thegadjetfinder.activity.RegisterActivity;
import com.david.thegadjetfinder.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private Button mainLoginButton;

    private Button mainRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dummyConnection("dummy");
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

    private void dummyConnection(String dummy) {
        Call<String> dummyCall = RetrofitClient.getInstance().getIGadgetFinderService().dummy(dummy);
        Thread thread = new Thread(() -> dummyCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        }));

        thread.start();
    }
}

