package com.institudeidcard.user.institudeidmakerapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Institude;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Student;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.IDCardAllDetails;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;
import com.institudeidcard.user.institudeidmakerapp.SharedPreference_class.StudentSavePref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Student_IDcard_view extends AppCompatActivity {

    ImageView img_school_logo, img_signature, img_pic;
    TextView txtInstitudeName, txtInstitudeAddress, txtStudentName, txt_class, txt_division, txtDob, txtbloodgrp, txtMobileno, txtStudentAddress;
    ScrollView scrollView;
    RelativeLayout relativelayout;
    //Retrofit variables..
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api_Institude.BASE_URL1)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    Api_Student Ar = retrofit.create(Api_Student.class);


    String reg_mob,SID;
    CardView cardview;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__idcard_view);


        relativelayout = findViewById(R.id.relativelayout);
        cardview = findViewById(R.id.cardView);
        img_school_logo = findViewById(R.id.img_school_logo);
        img_signature = findViewById(R.id.img_signature);
        img_pic = findViewById(R.id.img_pic);

        txtInstitudeName = findViewById(R.id.txtInstitudeName);
        txtInstitudeAddress = findViewById(R.id.txtInstitudeAddress);
        txtStudentName = findViewById(R.id.txtStudentName);
        txt_class = findViewById(R.id.txt_class);
        txt_division = findViewById(R.id.txt_division);
        txtDob = findViewById(R.id.txtDob);
        txtbloodgrp = findViewById(R.id.txtbloodgrp);
        txtMobileno = findViewById(R.id.txtMobileno);
        txtStudentAddress = findViewById(R.id.txtStudentAddress);

        reg_mob = StudentSavePref.getStr("mobile");
        System.out.println("Reg_mobile:-" + reg_mob);

        SID=StudentSavePref.getSID("SID");


        //Student Data Fetch code
        Call<IDCardAllDetails> call1 = Ar.idcard(SID);

        pd = new ProgressDialog(Student_IDcard_view.this);
        pd.setTitle("Getting Your Information!");
        pd.setMessage("Please Wait..");
        pd.show();

        call1.enqueue(new Callback<IDCardAllDetails>() {
            @Override
            public void onResponse(Call<IDCardAllDetails> call1, Response<IDCardAllDetails> response) {
                pd.dismiss();

                IDCardAllDetails imageClass = response.body();

                Toast.makeText(Student_IDcard_view.this, "" + imageClass.getResponse(), Toast.LENGTH_LONG).show();
                System.out.println("response:-" + imageClass.getResponse());

                System.out.println("Stud_Name/////" + imageClass.getStud_name());
                System.out.println("class/////" + imageClass.getStud_class());
                System.out.println("Stud_division/////" + imageClass.getStud_division());
                System.out.println("Stud_dob/////" + imageClass.getStud_dob());
                System.out.println("Stud_blood/////" + imageClass.getStud_blood());
                System.out.println("Stud_mobile/////" + imageClass.getStud_mobile());
                System.out.println("Stud_address/////" + imageClass.getStud_address());
                System.out.println("institude_name/////" + imageClass.getName_of_institute());
                System.out.println("institute_address/////" + imageClass.getInstitute_address());
                System.out.println("logo/////" + imageClass.getLogo());
                System.out.println("signature/////" + imageClass.getSignature());
                System.out.println("Stud_image/////" + imageClass.getStud_image());
                System.out.println("Institute_template/////" + imageClass.getTemplate());

                //if response is equal to 'Data Found' then set the editext value as per server response.

                String res = imageClass.getResponse();
                if (res.equals("Data Found")) {

                    System.out.println("res:-" + res);

                    //BASE64 String decode and display in logo
                    String base64String = "data:image/jpg;base64," + imageClass.getLogo();
                    final String pureBase64Encoded = base64String.split(",")[1];
                    final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    img_school_logo.setImageBitmap(decodedBitmap);

                    //BASE64 String decode and display in signature
                    String base64Strin = "data:image/jpg;base64," + imageClass.getSignature();
                    final String pureBase64Encode = base64Strin.split(",")[1];
                    final byte[] decodedByte = Base64.decode(pureBase64Encode, Base64.DEFAULT);
                    Bitmap decodedBitma = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                    img_signature.setImageBitmap(decodedBitma);

                    //BASE64 String decode and display in student image
                    String base64Stri = "data:image/jpg;base64," + imageClass.getStud_image();
                    final String pureBase64Encod = base64Stri.split(",")[1];
                    final byte[] decodedByt = Base64.decode(pureBase64Encod, Base64.DEFAULT);
                    Bitmap decodedBitm = BitmapFactory.decodeByteArray(decodedByt, 0, decodedByt.length);
                    img_pic.setImageBitmap(decodedBitm);

//                    //BASE64 String decode and display in student image
                    String base64Str = "data:image/jpg;base64," + imageClass.getTemplate();
                    final String pureBase64Enco = base64Str.split(",")[1];
                    final byte[] decodedBy = Base64.decode(pureBase64Enco, Base64.DEFAULT);
                    Bitmap decodedBit = BitmapFactory.decodeByteArray(decodedBy, 0, decodedBy.length);
                    //code use to set the bitmap to relative layout
                    BitmapDrawable background = new BitmapDrawable(decodedBit);
                    relativelayout.setBackgroundDrawable(background);


                    txtInstitudeName.setText(imageClass.getName_of_institute());
                    txtInstitudeAddress.setText(imageClass.getInstitute_address());
                    txtStudentName.setText(imageClass.getStud_name());
                    txt_class.setText(imageClass.getStud_class());
                    txt_division.setText(imageClass.getStud_division());
                    txtDob.setText(imageClass.getStud_dob());
                    txtbloodgrp.setText(imageClass.getStud_blood());
                    txtMobileno.setText(imageClass.getStud_mobile());
                    txtStudentAddress.setText(imageClass.getStud_address());

                } else {
                    System.out.println("You are in else");
                    cardview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<IDCardAllDetails> call1, Throwable t) {
                pd.dismiss();
                System.out.println("error :" + t.getMessage());
                Toast.makeText(Student_IDcard_view.this, "Error:server error", Toast.LENGTH_SHORT).show();

            }
        });

    }
}

