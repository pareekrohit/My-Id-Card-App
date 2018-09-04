package com.institudeidcard.user.institudeidmakerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Stud_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud__screen);
    }
    public void showStudentRegistration(View v){
        Intent i=new Intent(Stud_screen.this,Stud_Registration.class);
        startActivity(i);
    }

    public void showstudentLogin(View v){
        Intent i=new Intent(Stud_screen.this,stud_login.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
