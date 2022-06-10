package com.vv.attendanceteacher.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.vv.attendanceteacher.DataStore;
import com.vv.attendanceteacher.MainActivity;
import com.vv.attendanceteacher.R;
import com.vv.attendanceteacher.RetrofitClient;
import com.vv.attendanceteacher.databinding.ActivityHomeBinding;
import com.vv.attendanceteacher.models.TeacherDetailsModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    public static String teacherId;
    TextView name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        teacherId = MainActivity.sharedPreferences.getString(DataStore.TEACHER_ID,"0");
        if(teacherId.equals("0")){
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            finish();
        }

        setSupportActionBar(binding.appBarHome.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        View view = binding.navView.inflateHeaderView(R.layout.nav_header_home);

        name = view.findViewById(R.id.nav_name);
        email = view.findViewById(R.id.nav_email);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Call<TeacherDetailsModel> call = RetrofitClient.getInstance().getMyApi().getTeacherDetails(teacherId);
        call.enqueue(new Callback<TeacherDetailsModel>() {
            @Override
            public void onResponse(Call<TeacherDetailsModel> call, Response<TeacherDetailsModel> response) {
                name.setText(response.body().getFirstName() + " " + response.body().getLastName());
                email.setText(response.body().getEmailAddress());
            }

            @Override
            public void onFailure(Call<TeacherDetailsModel> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}