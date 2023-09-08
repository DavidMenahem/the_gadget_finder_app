package com.david.thegadjetfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.david.thegadjetfinder.R;
import com.david.thegadjetfinder.api.RetrofitClient;
import com.david.thegadjetfinder.model.Response;
import com.david.thegadjetfinder.model.Register;
import com.david.thegadjetfinder.validation.ValidateEmailPassword;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity implements ValidateEmailPassword {

    private EditText email,password;

    private Button btnRegister;
    private TextView txtResponse,toLogin;
    
    private TextInputLayout emailInput,passwordInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        emailInput = findViewById(R.id.email_txt_input_register);
        passwordInput = findViewById(R.id.password_txt_input_register);
        btnRegister = findViewById(R.id.button_register);
        btnRegister.setOnClickListener(v -> register());
        txtResponse = findViewById((R.id.res_register));
        toLogin = findViewById(R.id.to_login);
        toLogin.setOnClickListener(v->loginActivity());
    }
    public void register(){
        String emailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();
        if (validateFields(emailTxt,passwordTxt)) {

            txtResponse.setText("");
            Register register = new Register(email.getText().toString(),
                    password.getText().toString());
            Call<Response> registerCall = RetrofitClient.getInstance().getIGadgetFinderService().register(register);
            Thread thread = new Thread(() -> registerCall.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    String res = response.body().getResponse();
                    StringBuilder message = new StringBuilder();
                    if (response.isSuccessful() && res.equals("User created")) {
                        String name = response.body().getName();
                        message.append("Hi " + name + "! \n" + res);

                        //save response data to android session
                        SharedPreferences preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putLong("userID", response.body().getUserID());
                        editor.apply();

                        //load gadget page
                        gadgetActivity();
                    } else {
                        message.append(res);
                    }
                    txtResponse.setText(message.toString());
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    txtResponse.setText(R.string.request_failed);
                }
            }));

            thread.start();
        }
    }

    //validate field functions
    public boolean validateFields(String email,String password){
        boolean valid = true;

       if(!validateEmailLocal(email)) {
           valid = false;
       }
       if(!validatePasswordLocal(password)){
           valid = false;
       }
       return valid;
    }

    private boolean validatePasswordLocal(String password) {
        passwordInput.setError("");
        if(!validatePassword(password)){
            passwordInput.setError("Password must be 6 to 12 characters");
            return false;
        }else{
            return true;
        }
    }

    private boolean validateEmailLocal(String email) {
        emailInput.setError("");
        if(!validateEmail(email)){
            emailInput.setError("Email is not valid");
            return false;
        }else{
            return true;
        }
    }
    public void loginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void gadgetActivity(){
        Intent intent = new Intent(this, GadgetActivity.class);
        startActivity(intent);
    }
}
