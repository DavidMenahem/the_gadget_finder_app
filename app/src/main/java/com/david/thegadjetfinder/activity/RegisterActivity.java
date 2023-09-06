package com.david.thegadjetfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private EditText email,password,mobile,fName,lName, streetAddress,streetNumber,city,zipcode;

    private Button btnRegister,toLogin;
    private TextView txtResponse;
    
    private TextInputLayout fNameInput,lNameInput,emailInput,passwordInput,mobileInput,streetNumberInput,
    streetAddressInput,cityInput,zipcodeInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        mobile = findViewById(R.id.mobile);
        fName = findViewById(R.id.first_name);
        lName = findViewById(R.id.last_name);
        streetAddress = findViewById(R.id.street_address);
        streetNumber = findViewById(R.id.street_number);
        city = findViewById(R.id.city);
        zipcode = findViewById(R.id.zipcode);
        btnRegister = findViewById(R.id.button_register);
        txtResponse = findViewById((R.id.res_register));
        toLogin = findViewById(R.id.to_login);
        fNameInput = findViewById(R.id.first_name_txt_input);
        lNameInput = findViewById(R.id.last_name_txt_input);
        emailInput = findViewById(R.id.email_txt_input_register);
        passwordInput = findViewById(R.id.password_txt_input_register);
        mobileInput = findViewById(R.id.mobile_txt_input);
        streetNumberInput = findViewById(R.id.street_number_txt_input);
        streetAddressInput = findViewById(R.id.street_address_txt_input);
        cityInput = findViewById(R.id.city_txt_input);
        zipcodeInput = findViewById(R.id.zipcode_txt_input);

        btnRegister.setOnClickListener(v -> register());
        toLogin.setOnClickListener(v->loginActivity());
    }
    public void register(){
        String fNameTxt = fName.getText().toString();
        String lNameTxt = lName.getText().toString();
        String emailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();
        String mobileTxt = mobile.getText().toString();
        String streetNumberTxt = streetNumber.getText().toString();
        String streetAddressTxt = streetAddress.getText().toString();
        String cityTxt = city.getText().toString();
        String zipcodeTxt = zipcode.getText().toString();
        if (validateFields(emailTxt,passwordTxt,mobileTxt,fNameTxt,lNameTxt,
                streetAddressTxt,streetNumberTxt,cityTxt,zipcodeTxt)) {

            txtResponse.setText("");
            Register register = new Register(fName.getText().toString(),
                    lName.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    mobile.getText().toString(),
                    streetAddress.getText().toString(),
                    streetNumber.getText().toString(),
                    city.getText().toString(),
                    zipcode.getText().toString());
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
    public boolean validateFields(String email,String password,String mobile,String fName,String lName,
                                  String streetAddress,String streetNumber,String city,String zipcode){
        boolean valid = true;
       if(!validateFirstName(fName)){
           valid = false;
       }
       if(!validateLastName(lName)){
           valid = false;
       }
       if(!validateEmailLocal(email)) {
           valid = false;
       }
       if(!validatePasswordLocal(password)){
           valid = false;
       }
       if(!validateMobile(mobile)){
           valid = false;
       }
       if(!validateStreetNumber(streetNumber)){
           valid = false;
       }
       if(!validateStreetAddress(streetAddress)){
           valid = false;
       }
       if(!validateCity(city)){
           valid = false;
       }
       if(!validateZipcode(zipcode)){
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

    private boolean validateZipcode(String zipcode) {
        zipcodeInput.setError("");
        for (int i = 0; i < zipcode.length(); i++) {
            if (!Character.isLetterOrDigit(zipcode.charAt(i))) {
                zipcodeInput.setError("Zipcode must be all numbers or letters");
                return false;
            }
        }
        return true;
    }

    private boolean validateCity(String city) {
        cityInput.setError("");
        boolean valid = true;
        int length = city.length();
        if(length > 2) {
            for (int i = 0; i < length; i++) {
                if (!(Character.isLetter(city.charAt(i)) || (city.charAt(i) == 44))) {
                    valid = false;
                }
            }
        }
        if(!valid) {
            cityInput.setError("City must be at least 3 letters or '\''");
            return false;
        }

        return true;
    }

    private boolean validateStreetAddress(String streetAddress) {
        streetAddressInput.setError("");
        for (int i = 0; i < streetAddress.length(); i++) {
            if (!Character.isLetterOrDigit(streetAddress.charAt(i))) {
                streetNumberInput.setError("Street address must be digits or letters");
                return false;
            }
        }

        return true;
    }

    private boolean validateStreetNumber(String streetNumber) {
        streetNumberInput.setError("");
        for (int i = 0; i < streetNumber.length(); i++) {
            if (!Character.isLetterOrDigit(streetNumber.charAt(i))) {
                streetNumberInput.setError("Street number must be digits or letters");
                return false;
            }
        }
        return true;
    }

    private boolean validateMobile(String mobile) {
        mobileInput.setError("");
        int length = mobile.length();
        for (int i = 0; i < length; i++) {
            if (!(Character.isDigit(mobile.charAt(i))
                    || mobile.charAt(i) == 45 || (mobile.charAt(i) == 43))) {
                mobileInput.setError("Mobile must be all numbers or '+/-");
                return false;
            }
        }
            return true;
    }

    private boolean validateLastName(String lName) {
        lNameInput.setError("");
        if(lName.length() < 3){
            lNameInput.setError("Last Name must be at least 3 letters");
            return false;
        }
        return true;
    }

    public boolean validateFirstName(String fName){
        fNameInput.setError("");
        if(fName.length() < 3){
            fNameInput.setError("First name must be at least 3 letters");
            return false;
        }
        return true;
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
