package com.david.thegadjetfinder.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.david.thegadjetfinder.R;
import com.david.thegadjetfinder.api.RetrofitClient;
import com.david.thegadjetfinder.model.Gadget;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ViewAllActivity extends AppCompatActivity {

    TextView textView;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_viewall);
        generateGadgets();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        gadgetActivity();
        return super.onOptionsItemSelected(item);
    }

    public void generateGadgets(){
        SharedPreferences preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        Long userID = preferences.getLong("userID",0);
        Call<List<Gadget>> allGadgetsCall = RetrofitClient.getInstance().getIGadgetFinderService().getALL(userID);
        Thread thread = new Thread(() -> {
            allGadgetsCall.enqueue(new Callback<List<Gadget>>() {
                @Override
                public void onResponse(Call<List<Gadget>> call, retrofit2.Response<List<Gadget>> response) {

                    if (response.isSuccessful()) {
                        List<Gadget> allGadgets = response.body();
                        for (int i = 0; i < allGadgets.size(); i++) {
                            int id = View.generateViewId();
                            Button button = new Button(ViewAllActivity.this);
                            button.setText(allGadgets.get(i).getGadget());
                            button.setWidth(100);
                            button.setHeight(50);
                            button.setId(id);
                            button.setTextSize(15);
                            Gadget gadget = allGadgets.get(i);
                            button.setOnClickListener(v -> mapActivity(gadget));


                                    GridLayout layout = findViewById(R.id.view_all);
                            layout.addView(button);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Gadget>> call, Throwable t) {

                }
            });
        });

        thread.start();
    }
    private void mapActivity(Gadget gadget) {
        Intent intent = new Intent(this,MapActivity.class);
        intent.putExtra("gadget",gadget.getGadget());
        intent.putExtra("id",gadget.getId());
        startActivity(intent);

    }
    public void gadgetActivity(){
        Intent intent = new Intent(this, GadgetActivity.class);
        startActivity(intent);
    }
}
