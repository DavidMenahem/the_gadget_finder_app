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
import com.david.thegadjetfinder.model.Gadget;
import com.david.thegadjetfinder.model.Response;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;

public class GadgetActivity extends AppCompatActivity {

    private EditText editTxtGadget;

    private TextView txtGadgetResponse;
    private Button addGadget,viewAll;
    private TextInputLayout gadgetInput;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gadget);
        editTxtGadget = findViewById(R.id.add_gadget);
        txtGadgetResponse = findViewById(R.id.gadgetResponse);
        addGadget = findViewById(R.id.btn_add_gadget);
        viewAll = findViewById(R.id.btn_view_gadgets);
        gadgetInput = findViewById(R.id.gadget_txt_input);
        addGadget.setOnClickListener(v->addGadget());
        viewAll.setOnClickListener(v-> viewAllActivity());
    }

    public void addGadget(){
        //get logged user id from session

        SharedPreferences preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        Long userID = preferences.getLong("userID",0);
        //create new gadget
        String name = editTxtGadget.getText().toString();
        if(!name.isEmpty()) {
            gadgetInput.setError("");
            Gadget gadget = new Gadget(userID, name);

            //make a call via retrofit to add gadget to te database
            Call<Response> gadgetCall = RetrofitClient.getInstance().getIGadgetFinderService().addGadget(gadget);
            Thread thread = new Thread(() -> gadgetCall.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                    if (response.isSuccessful()) {
                        String res = response.body().getResponse();
                        txtGadgetResponse.setText(res);
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    txtGadgetResponse.setText(R.string.request_failed);
                }
            }));

            thread.start();
        }else{
           gadgetInput.setError("Gadget Name is required");
        }
    }

    public void viewAllActivity(){
        Intent intent = new Intent(this,ViewAllActivity.class);
        startActivity(intent);
    }
}
