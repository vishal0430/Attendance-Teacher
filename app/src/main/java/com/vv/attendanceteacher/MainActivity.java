package com.vv.attendanceteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.vv.attendanceteacher.ui.HomeActivity;
import com.vv.attendanceteacher.ui.LoginActivity;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sharedPreferences = getSharedPreferences(DataStore.PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean(DataStore.LOGIN_STATUS,false)){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        finish();
    }
}