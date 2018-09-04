package com.institudeidcard.user.institudeidmakerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static com.institudeidcard.user.institudeidmakerapp.Interface.Api_Student.BASE_URLSMS;

public class Stud_Registration extends AppCompatActivity {
    Button btnRegister;
    EditText edpassword, edMobile, edEmail;

    String mobile;
    String message;
    String type;
    String userid;
    String password;
    String otp;
    String email, pass;
    String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud__registration);

        btnRegister = findViewById(R.id.btn_Stud_Register);

        edpassword = findViewById(R.id.ed_stud_password);
        edEmail = findViewById(R.id.ed_Stud_Email);
        edMobile = findViewById(R.id.ed_stud_mobile);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEmptyField();
                if (isEmptyField() == true) {

                    // registration asyncTask
                    StudRegistrationTask rt = new StudRegistrationTask(Stud_Registration.this);
                    rt.execute(mobile, email, pass, otp);
                    showdialog();

                }
            }
        });


    }

    public boolean isEmptyField() {
        mobile = edMobile.getText().toString();
        email = edEmail.getText().toString();
        pass = edpassword.getText().toString();

        // Using random method to generate OTP
        int randomPin = (int) (Math.random() * 9000) + 1000;
        otp = String.valueOf(randomPin);


        if (email.isEmpty() || mobile.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Fields Are Empty..", Toast.LENGTH_LONG).show();
            return false;

        } else if (isValideEmail(email) == false) {
            edEmail.setError("Please Enter Valid Email");
            return false;

        } else if (isValideMobile(mobile) == false) {
            edMobile.setError("Please Enter Valid mobile no");
            return false;

        }
        return true;
    }

    //Validation of edittext..............................................................................
    private boolean isValideMobile(String mobile) {

        //mobile number validation.....................................................................
        String validmobile = "^[7-9][0-9]{9}$";
        Matcher matcher1 = Pattern.compile(validmobile).matcher(mobile);

        if (matcher1.matches()) {
            return true;
        } else {
            return false;

        }
//..............................................................................................
    }

    private boolean isValideEmail(String email) {
        //Email Validation..................................................................................................
        String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
        Matcher matcher = Pattern.compile(validemail).matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            return false;

        }
    }

    //showing enter otp dialog box...........................................................................
    public void showdialog() {

        final EditText ed_otp;

        mobile = edMobile.getText().toString();
        email = edEmail.getText().toString();

        //message = "Hello user your MyIDCard OTP is:0101. Please Do Not share OTP with anyone";
//        type = "1";
//        password = "sbit2018";
//        userid = "sbit";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.enter_otp_dialog, null);
        builder.setView(view);

        ed_otp = view.findViewById(R.id.ed_otp);

            // Add the buttons
            builder.setPositiveButton("Verify my number", null);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            builder.setNeutralButton("Resend", null);

            // Create the AlertDialog
            final AlertDialog dialog = builder.create();
          //To keep the dialog box open
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialogInterface) {

                    //On click of 'Resend' this block will excecute.........................................................................................
                    Button button1 = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                    button1.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            System.out.println("////In Neutral..");
//                            StudRegistrationTask rt = new StudRegistrationTask(Stud_Registration.this);
//                            rt.execute(mobile, email, pass, otp);

                            message = "Hello user your MyIDCard OTP is:" + otp + ". Please Do Not share OTP with anyone";
                            type = "1";
                            password = "sbit2018";
                            userid = "sbit";

                            StudSMSTask up = new StudSMSTask(Stud_Registration.this);
                            up.execute(mobile, message, type, userid, password);
                            System.out.println("mobile in Resend method :"+mobile+" otp: "+otp);
                        }
                    });

                    //On click of 'verfy my number' this block will excecute.........................................................................................
                    Button button = (dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            System.out.println("-------ed_otp:" + ed_otp.getText().toString());

                            Gson gson = new GsonBuilder()
                                    .setLenient()
                                    .create();
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(Api_Institude.BASE_URL1)
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .build();

                            Api_Student Ar = retrofit.create(Api_Student.class);
                            Call<Registration_pojo> call = Ar.checkOTP(mobile, ed_otp.getText().toString());

                            call.enqueue(new Callback<Registration_pojo>() {
                                @Override
                                public void onResponse(Call<Registration_pojo> call, Response<Registration_pojo> response) {
                                    //response like 200
                                    System.out.println("server response///////////////////" + response.code());
                                    //isSuccessful (true/false)
                                    System.out.println("server response///////////////////" + response.isSuccessful());

                                    Registration_pojo po = response.body();
                                    System.out.println("///////////////////Server Response:-" + po.getResponse());
                                    Toast.makeText(Stud_Registration.this, "" + po.getResponse(), Toast.LENGTH_LONG).show();

                                    res = po.getResponse();
                                    System.out.println("check response--" + res);

                                    if (po.getResponse().equals("Number Verified successfully")) {
                                        dialog.dismiss();
                                        finish();
                                    } else {
                                    }
                                }

                                @Override
                                public void onFailure(Call<Registration_pojo> call, Throwable t) {
                                    System.out.println("Error :" + t.getMessage());
                                    Toast.makeText(Stud_Registration.this, "Error:server error", Toast.LENGTH_SHORT).show();
                                    //pd.dismiss();
                                }
                            });
                        }
                    });
                }
            });


            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

    }
    //.........................................................................................................


    //call student page...........................................
    public void showStudentLogin(View v) {
        Intent i = new Intent(Stud_Registration.this, stud_login.class);
        startActivity(i);
        finish();
    }
}

//OTP SMS AsyncTask..................................................................................
class StudSMSTask extends AsyncTask<String, Void, Void> {
    ProgressDialog pd;
    Context mctx;
    String mobile, message, type, userid, password;

    public StudSMSTask(Context mctx) {
        this.mctx = mctx;
    }

    @Override
    protected Void doInBackground(String... data) {
        mobile = data[0];
        message = data[1];
        type = data[2];
        userid = data[3];
        password = data[4];

        Log.d("mobile no ////", data[0]);
        Log.d("message ////", data[1]);
        Log.d("Type ////", data[2]);

        System.out.println("SMS class  mobile:"+mobile +"Message :"+message +"type :"+type +" userid :"+userid +" Password :"+password);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URLSMS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api_Student Ar = retrofit.create(Api_Student.class);
        Call<Void> call = Ar.otpSMS(mobile, message, type, userid, password);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.message());
                Toast.makeText(mctx, "You will Receive OTP Soon ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("error :" + t.getMessage());
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

    @Override
    protected void onPostExecute(Void result) {
        pd.dismiss();


    }
}

//registration class..............................................................
class StudRegistrationTask extends AsyncTask<String, Void, String> {
    ProgressDialog pd;
    Context mctx;
    String mobile, email, pass, res;

    String message;
    String type = "1";
    String password = "sbit2018";
    String userid = "sbit";
    String otp;

    public StudRegistrationTask(Context mctx) {
        this.mctx = mctx;
    }

    @Override
    protected String doInBackground(String... data) {
        mobile = data[0];
        email = data[1];
        pass = data[2];
        otp = data[3];


        Log.d("mobile ////", data[0]);
        Log.d("email ////", data[1]);
        Log.d("pass ////", data[2]);
        Log.d("otp ////", data[3]);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api_Institude.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api_Student Ar = retrofit.create(Api_Student.class);
        Call<Registration_pojo> call = Ar.registrationData(mobile, email, pass, otp);


        call.enqueue(new Callback<Registration_pojo>() {
            @Override
            public void onResponse(Call<Registration_pojo> call, Response<Registration_pojo> response) {
                //response like 200
                System.out.println("server response///////////////////" + response.code());
                //isSuccessful (true/false)
                System.out.println("server response///////////////////" + response.isSuccessful());

                Registration_pojo po = response.body();
                System.out.println("///////////////////" + po.getResponse());
                Toast.makeText(mctx, "" + po.getResponse(), Toast.LENGTH_LONG).show();

                res = po.getResponse();
                if (res.equals("user registered successfully") || res.equals("Data Updated Successfully")) {
                    System.out.println("////Your are in sms");
                    message = "Hello user your MyIDCard OTP is:" + otp + ". Please Do Not share OTP with anyone";
                    //otp through sms
                    StudSMSTask up = new StudSMSTask(mctx);
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

        return res;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(mctx);
        pd.setMessage("Please Wait..");
        pd.show();
    }

    @Override
    protected void onPostExecute(String result) {
        pd.dismiss();

    }
}


//Verify OTP class..............................................................
class StudCheckOTP extends AsyncTask<String, Void, String> {
    ProgressDialog pd;
    Context mctx;
    String mobile, otp;

    String response;
    String res;


    public StudCheckOTP(Context mctx) {
        this.mctx = mctx;
    }

    @Override
    protected String doInBackground(String... data) {
        mobile = data[0];
        otp = data[1];

        Log.d("mobile ////", data[0]);
        Log.d("otp ////", data[1]);


//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Api_Institude.BASE_URL1)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        Api_Student Ar = retrofit.create(Api_Student.class);
//        Call<Registration_pojo> call = Ar.checkOTP(mobile, otp);
//
//        call.enqueue(new Callback<Registration_pojo>() {
//            @Override
//            public void onResponse(Call<Registration_pojo> call, Response<Registration_pojo> response) {
//                //response like 200
//                System.out.println("server response///////////////////" + response.code());
//                //isSuccessful (true/false)
//                System.out.println("server response///////////////////" + response.isSuccessful());
//
//                Registration_pojo po = response.body();
//                System.out.println("///////////////////Server Response:-" + po.getResponse());
//                Toast.makeText(mctx, "" + po.getResponse(), Toast.LENGTH_LONG).show();
//
//                res = po.getResponse();
//                System.out.println("check response--" + res);
//
//                StudentSavePref savepref = new StudentSavePref(mctx);
//                savepref.setRes("res", res);
//
//                System.out.println("check response:" + savepref.getRes("res"));
////                Number Verified successfully
//
//            }
//
//            @Override
//            public void onFailure(Call<Registration_pojo> call, Throwable t) {
//                System.out.println("Error :" + t.getMessage());
//                Toast.makeText(mctx, "Error:server error", Toast.LENGTH_SHORT).show();
//                pd.dismiss();
//            }
//        });

        System.out.println("res out of Onresponse--------" + res);
        return res;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(mctx);
        pd.setMessage("Verifying the OTP..");
        pd.show();
    }

    @Override
    protected void onPostExecute(String result) {
        pd.dismiss();
    }
}

