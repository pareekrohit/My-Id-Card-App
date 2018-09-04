package com.institudeidcard.user.institudeidmakerapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Institude;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Student;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.Registration_pojo;
import com.institudeidcard.user.institudeidmakerapp.SharedPreference_class.StudentSavePref;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class stud_login extends AppCompatActivity {
    Button btnlogin;
    EditText edMobile, edPassword;
    LinearLayout ln;

    String mobile, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_login);

        btnlogin = findViewById(R.id.buttonLogin);
        edMobile = findViewById(R.id.edUserMobile);
        edPassword = findViewById(R.id.editTextPassword);

        ln = findViewById(R.id.linearlayout1);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEmptyField();
                if (isEmptyField() == true) {
                    //login asyncTask
                    StudLoginTask rt = new StudLoginTask(stud_login.this,btnlogin);
                    rt.execute(mobile, pass);
//                    btnlogin.setEnabled(false);
//                    btnlogin.setBackgroundColor(getResources().getColor(R.color.brown));
                }
            }
        });

    }

    //Validation.........................................................................................
    public boolean isEmptyField() {
        mobile = edMobile.getText().toString();
        pass = edPassword.getText().toString();


        if (mobile.isEmpty() || pass.isEmpty()) {
            //Toast.makeText(this, "Fields Are Empty..", Toast.LENGTH_LONG).show();

            Snackbar snackbar = Snackbar.make(ln, "Fields Are Empty..", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else if (isValideMobile(mobile) == false) {
            edMobile.setError("Please Enter Valid mobile no");
            return false;
        }
        return true;
    }

    private boolean isValideMobile(String mobile) {

        //mobile number validation.....................................................................
        String validmobile = "^[7-9][0-9]{9}$";
        Matcher matcher1 = Pattern.compile(validmobile).matcher(mobile);

        if (matcher1.matches()) {
            return true;
        } else {
            return false;

        }
    }


    public void showStudRegistration(View v) {
        Intent i = new Intent(stud_login.this, Stud_Registration.class);
        startActivity(i);
        finish();
    }

    public void showStudentForgetPassword(View v) {
        Intent i = new Intent(stud_login.this, Stud_Forget_Password.class);
        startActivity(i);
        finish();
    }
}

//Login class..............................................................
class StudLoginTask extends AsyncTask<String, Void, String> {
    ProgressDialog pd;
    Context mctx;
    String mobile, pass, res;
    Button btnlogin;

    public StudLoginTask(Context mctx, Button btnlogin) {
        this.mctx = mctx;
        this.btnlogin = btnlogin;
    }

    @Override
    protected String doInBackground(String... data) {
        mobile = data[0];
        pass = data[1];

//        Log.d("mobile ////", data[0]);
//        Log.d("pass ////", data[1]);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api_Institude.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api_Student Ar = retrofit.create(Api_Student.class);
        Call<Registration_pojo> call = Ar.login(mobile, pass);
        call.enqueue(new Callback<Registration_pojo>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<Registration_pojo> call, Response<Registration_pojo> response) {

//                btnlogin.setEnabled(true);
//                btnlogin.setBackgroundColor(R.color.colorPrimary);

                //response like 200
                System.out.println("server response///////////////////" + response.code());
                //isSuccessful (true/false)
                System.out.println("server response///////////////////" + response.isSuccessful());

                Registration_pojo po = response.body();
                System.out.println("///////////////////" + po.getResponse());
                Toast.makeText(mctx, "" + po.getResponse(), Toast.LENGTH_SHORT).show();

                res = po.getResponse();

                if (res.equals("User Login Successfully")) {
                    //new Mobile_no_pojo(mobile);

                    StudentSavePref savepref = new StudentSavePref(mctx);
                    savepref.setStr("mobile", mobile);

//                    String reg_mob = StudentSavePref.getStr("mobile");
//                    System.out.println("reg_mob"+reg_mob);

                    Intent i = new Intent(mctx, Student_Option.class);
                    i.putExtra("mobile", mobile);
                    mctx.startActivity(i);

                    ((stud_login) mctx).finish();

                }
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFailure(Call<Registration_pojo> call, Throwable t) {
                btnlogin.setEnabled(true);
                btnlogin.setBackgroundColor(R.color.colorPrimary);

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
        pd.setMessage("Please Wait..");
        pd.show();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onPostExecute(String result) {
        pd.dismiss();
        super.onPostExecute(result);

    }
}
