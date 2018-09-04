package com.institudeidcard.user.institudeidmakerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }

    public void showStudentDetail(View v){
        Intent i=new Intent(HomeActivity.this,UserDetail.class);
        startActivity(i);
    }

    public  void showInstitudeDetail(View v){
        Intent i=new Intent(HomeActivity.this,InstitudeDetails.class);
        startActivity(i);
    }
}
