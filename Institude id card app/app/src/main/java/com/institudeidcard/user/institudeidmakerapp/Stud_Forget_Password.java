package com.institudeidcard.user.institudeidmakerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Institude;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Student;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.Registration_pojo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Stud_Forget_Password extends AppCompatActivity {
    Button btnSendSMS;
    EditText ed_mobile;
    RelativeLayout linear2;

    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud__forget__password);

        btnSendSMS = findViewById(R.id.btnSendSMS);
        ed_mobile = findViewById(R.id.ed_mobile);
        linear2 = findViewById(R.id.linear2);

        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEmptyField();
                if (isEmptyField() == true) {
                    //forgetPassword asyncTask
                    StudForgetPasswordTask rt = new StudForgetPasswordTask(Stud_Forget_Password.this);
                    rt.execute(mobile);
                }
            }
        });
    }

    //Validation..........................................................................................
    public boolean isEmptyField() {
        mobile = ed_mobile.getText().toString();

        if (mobile.isEmpty()) {
            Snackbar snackbar = Snackbar.make(linear2, "Fields Are Empty..", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;

        } else if (isValideMobile(mobile) == false) {
            ed_mobile.setError("Please Enter Valid mobile no");
            return false;
        }
        return true;
    }

    //mobile number validation.....................................................................
    private boolean isValideMobile(String mobile) {

        String validmobile = "^[7-9][0-9]{9}$";
        Matcher matcher1 = Pattern.compile(validmobile).matcher(mobile);

        if (matcher1.matches()) {
            return true;
        } else {
            return false;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


//OTP SMS AsyncTask..................................................................................
class StudSMS1Task extends AsyncTask<String, Void, Void> {
    ProgressDialog pd;
    Context mctx;
    String mobile, message, type, userid, password;

    public StudSMS1Task(Context mctx) {
        this.mctx = mctx;
    }

    @Override
    protected Void doInBackground(String... data) {
        mobile = data[0];
        message = data[1];
        type = data[2];
        userid = data[3];
        password = data[4];

//        Log.d("mobile no ////", data[0]);
//        Log.d("message ////", data[1]);
//        Log.d("Type ////", data[2]);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api_Institude.BASE_URLSMS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api_Student Ar = retrofit.create(Api_Student.class);
        Call<Void> call = Ar.otpSMS(mobile, message, type, userid, password);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//                //response like 200
                System.out.println("server response///////////////////" + response.code());
                //isSuccessful (true/false)
                System.out.println("server response///////////////////" + response.isSuccessful());

                Toast.makeText(mctx, "Check Your sms box ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("error :" + t.getMessage());
                Toast.makeText(mctx, "Error:server error" , Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

        return null;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(mctx);
        pd.setMessage("Please Wait..");
        pd.show();
    }

    @Override
    protected void onPostExecute(Void result) {
        pd.dismiss();
    }
}


//forgetPassword class..............................................................
class StudForgetPasswordTask extends AsyncTask<String, Void, String> {
    ProgressDialog pd;
    Context mctx;
    String mobile;

    String message, type, userid, password;

    public StudForgetPasswordTask(Context mctx) {
        this.mctx = mctx;
    }

    @Override
    protected String doInBackground(String... data) {
        mobile = data[0];

        Log.d("mobile ////", data[0]);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api_Institude.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api_Student Ar = retrofit.create(Api_Student.class);
        Call<Registration_pojo> call = Ar.forgetPassword(mobile);
        call.enqueue(new Callback<Registration_pojo>() {
            @Override
            public void onResponse(Call<Registration_pojo> call, Response<Registration_pojo> response) {
                //response like 200
                System.out.println("server response///////////////////" + response.code());
                //isSuccessful (true/false)
                System.out.println("server response///////////////////" + response.isSuccessful());

                Registration_pojo po = response.body();
                System.out.println("///////////////////password:" + po.getPassword());
                Toast.makeText(mctx, po.getResponse(), Toast.LENGTH_SHORT).show();

                String res = po.getResponse();
                if (res.equals("Mobile Number Found")) {

                    message = "Hello user your MyIDCard Password is:" + po.getPassword() + ". Please Do Not share Password with anyone";
                    type = "1";
                    password = "sbit2018";
                    userid = "sbit";

                    //  Send password through sms
                    StudSMS1Task up = new StudSMS1Task(mctx);
                    up.execute(mobile, message, type, userid, password);

                }


            }

            @Override
            public void onFailure(Call<Registration_pojo> call, Throwable t) {
                System.out.println("Error :" + t.getMessage());
                Toast.makeText(mctx, "Error:server error", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

        return null;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(mctx);
        pd.setMessage("Please Wait few seconds..");
        pd.show();
    }

    @Override
    protected void onPostExecute(String result) {
        pd.dismiss();
    }
}


