package com.example.my_notes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.databinding.ActivitySplashBinding;

public class Splash_Activity extends AppCompatActivity {
ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Handler handler = new Handler();
        Runnable r=new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_Activity.this, Login_Activity
                        .class);
                startActivity(intent);

                finish();
            }
        };
        handler.postDelayed(r, 2000);
    }

}