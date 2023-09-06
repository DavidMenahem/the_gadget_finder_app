package com.david.thegadjetfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.david.thegadjetfinder.R;
import com.david.thegadjetfinder.api.RetrofitClient;
import com.david.thegadjetfinder.model.Response;
import com.david.thegadjetfinder.shape.CircleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;


public class MapActivity extends AppCompatActivity {
    private TextView gadgetName;
    private Button deleteGadget;
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_map);
        gadgetName = findViewById(R.id.gadget_name);
        deleteGadget = findViewById(R.id.delete_gadget);
        deleteGadget.setOnClickListener(v->deleteGadget(getGadgetId()));
        showCircle();
    }

    private void deleteGadget(Long gadgetId) {
        Call<Void> gadgetCall = RetrofitClient.getInstance().getIGadgetFinderService().delete(gadgetId);
        Thread thread = new Thread(() -> gadgetCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {

                if (response.isSuccessful()) {
                    viewAllActivity();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        }));

        thread.start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        viewAllActivity();
        return super.onOptionsItemSelected(item);
    }

    public void showCircle(){
        //
        List<Integer> randomNumbers = getRandomPositions();
        int x = randomNumbers.get(0);
        int y = randomNumbers.get(1);
        int halfScreenWidth = randomNumbers.get(2);
        int halfScreenHeight = randomNumbers.get(3);
        // pass the new x and y and add the circle to the layout
        CircleView locationCircle = new CircleView(this,x,y);
        CircleView refCircle = new CircleView(this,0,0);

        // Create an AlphaAnimation object
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(500); //
        alphaAnimation.setRepeatCount(Animation.INFINITE); //
        // Start the animation
        locationCircle.startAnimation(alphaAnimation);
        String gadget = getGadgetName();
        gadgetName.setX(x + halfScreenWidth);
        gadgetName.setY(y + halfScreenHeight + 20);
        RelativeLayout layout = findViewById(R.id.map);
        layout.addView(locationCircle);
        layout.addView(refCircle);
        gadgetName.setText(gadget);
    }
    public void viewAllActivity(){
        Intent intent = new Intent(this,ViewAllActivity.class);
        startActivity(intent);
    }
    public List<Integer> getRandomPositions(){
        List<Integer> randomNumbers = new ArrayList<>();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float screenWidth = metrics.widthPixels;
        float screenHeight = metrics.heightPixels;
        int buttonHeight = (int)(40*metrics.density);
        Random random = new Random();
        //subtract half of the width and height from a random number
        //since the circle starts in the middle of the screen
        float xUpperBound = screenWidth - screenWidth/2;
        float yUpperBound = screenHeight - screenHeight/2;
        int x = random.nextInt((int)screenWidth - 100) - (int)screenWidth/2;
        int y = random.nextInt((int)screenHeight - 250) - (int)screenHeight/2;
        randomNumbers.add(x);
        randomNumbers.add(y);
        randomNumbers.add((int)screenWidth/2);
        randomNumbers.add((int)screenHeight/2);
        return randomNumbers;
    }

    public String getGadgetName(){
        Intent intent = getIntent();
        return intent.getExtras().getString("gadget");
    }
    public Long getGadgetId(){
        Intent intent = getIntent();
        return intent.getExtras().getLong("id");
    }
}
