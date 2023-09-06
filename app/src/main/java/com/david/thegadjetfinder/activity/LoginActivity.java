package com.david.thegadjetfinder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.david.thegadjetfinder.R;
import com.david.thegadjetfinder.api.RetrofitClient;
import com.david.thegadjetfinder.model.Login;
import com.david.thegadjetfinder.model.Response;
import com.david.thegadjetfinder.validation.ValidateEmailPassword;
import com.google.android.material.textfield.TextInputLayout;


import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements ValidateEmailPassword {

    private EditText email;
    private EditText password;
    private Button btnLogin;
    private Button toRegister;

    private TextView txtResponse;

    private TextInputLayout emailInput, passwordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        toRegister = findViewById(R.id.to_register);
        txtResponse = findViewById(R.id.res_login);
        emailInput = findViewById(R.id.email_txt_input_login);
        passwordInput = findViewById(R.id.password_txt_input_login);
        btnLogin.setOnClickListener(v->login());
        toRegister.setOnClickListener(v->registerActivity());
    }
    public void registerActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login() {
        String emailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();
        boolean valid = validateFields(emailTxt);

        if(valid) {
            Login login = new Login(emailTxt, passwordTxt);

            Call<Response> loginCall = RetrofitClient.getInstance().getIGadgetFinderService().login(login);

            Thread thread = new Thread(() -> loginCall.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    if (response.isSuccessful()) {
                        String res = response.body().getResponse();

                        StringBuilder message = new StringBuilder();
                        if (res.equals("User is logged in")) {
                            String name = response.body().getName();
                            message.append("Hi " + name + "!\n" + res);

                            //save response data to android session
                            SharedPreferences preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            txtResponse.setText(message.toString());
                            editor.putLong("userID", response.body().getUserID());
                            editor.commit();

                            //load gadget page
                            gadgetActivity();

                        } else {
                            message.append(res);
                        }
                        txtResponse.setText(message.toString());
                    }
                }


                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    txtResponse.setText(R.string.request_failed);
                }
            }));

            thread.start();
            }
    }
    public void gadgetActivity(){
        Intent intent = new Intent(this, GadgetActivity.class);
        startActivity(intent);
    }

    public boolean validateFields(String email){
        emailInput.setError("");
        if(!validateEmail(email)){
            emailInput.setError("Email not valid");
            return false;
        }
        return true;
    }

}

