package com.example.fayfl;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fayfl.ui.PhoneAuthActivity;


public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                {
                    try {
                        sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(WelcomeActivity.this, PhoneAuthActivity.class);
                        startActivity(intent);
                    }

                }

            }
        };
        thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}