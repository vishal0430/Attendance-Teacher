package com.vv.attendanceteacher.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vv.attendanceteacher.DataStore;
import com.vv.attendanceteacher.MainActivity;
import com.vv.attendanceteacher.R;
import com.vv.attendanceteacher.RetrofitClient;
import com.vv.attendanceteacher.models.LoginModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email,pass;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        Call<LoginModel> call = RetrofitClient.getInstance().getMyApi().loginTeacher(email.getText().toString(),pass.getText().toString());
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                LoginModel loginModel = response.body();
                if(loginModel.getStatus().equals("1")){
                    MainActivity.sharedPreferences.edit().putBoolean(DataStore.LOGIN_STATUS,true).apply();
                    MainActivity.sharedPreferences.edit().putString(DataStore.TEACHER_ID,loginModel.getId()).apply();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Enter correct Email / Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Something Went Wrong !!"+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}