package com.institudeidcard.user.institudeidmakerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Institude;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudeDetail_pojo;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InstitudeDetails extends AppCompatActivity {
    Bitmap logoBitmap, signatureBitmap;
    ImageView imgLogo, imgSignature;

    //static final int RESULT_LOAD_IMG = 0;

    String Institudename;
    String code;
    String Institudeaddress;
    String ImageLogo;
    String signature;


    EditText edInstitudeName, edCode, edInstitudeAddress;


    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institude_details);


        imgLogo = findViewById(R.id.img_logo);
        imgSignature = findViewById(R.id.ImgSinatureUpload);

        edInstitudeName = findViewById(R.id.edInstitudeName);
        edInstitudeAddress = findViewById(R.id.edInstitudeAddress);
        edCode = findViewById(R.id.edCode);


        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);

            }
        });

        imgSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);

            }
        });
    }


    //Save button on ActionBar Code................................
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ImageLogo = logoimageToString();
        signature = SignatureimageToString();

        Institudename = edInstitudeName.getText().toString();
        code = edCode.getText().toString();
        Institudeaddress = edInstitudeAddress.getText().toString();

        int id = item.getItemId();

        if (id == R.id.menu_save) {

            if (imgLogo.equals(R.drawable.upload_img) || imgSignature.equals(R.drawable.upload_img) || Institudename.isEmpty() || Institudeaddress.isEmpty() || code.isEmpty()) {
                Toast.makeText(this, "Fields Are Empty..", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View view = inflater.inflate(R.layout.save_dailog, null);
                builder.setView(view);

                // Add the buttons
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {


                        Gson gson = new GsonBuilder()
                                .setLenient()
                                .create();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Api_Institude.BASE_URL1)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();

                        Api_Institude Ar = retrofit.create(Api_Institude.class);
                        Call<ImageClass> call = Ar.intitudeData(code, Institudename, Institudeaddress, ImageLogo, signature);

                        final ProgressDialog progressDoalog;
                        progressDoalog = new ProgressDialog(InstitudeDetails.this);
                        progressDoalog.setMax(100);
                        progressDoalog.setMessage("Wait....");
                        progressDoalog.setTitle("Uploading Data!");
                        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        // show it
                        progressDoalog.show();

                        call.enqueue(new Callback<ImageClass>() {
                            @Override
                            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                                progressDoalog.dismiss();

                                //response like 200
                                System.out.println("server response///////////////////" + response.code());
                                //isSuccessful (true/false)
                                System.out.println("server response///////////////////" + response.isSuccessful());


                                ImageClass imageClass = response.body();
                                Toast.makeText(InstitudeDetails.this, "" + imageClass.getResponse(), Toast.LENGTH_LONG).show();
                                System.out.println("///" + imageClass.getResponse());
                            }

                            @Override
                            public void onFailure(Call<ImageClass> call, Throwable t) {
                                progressDoalog.dismiss();
                                System.out.println("error :" + t.getMessage());
                                Toast.makeText(InstitudeDetails.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();


                            }
                        });

                        //Empty all textview
                        edInstitudeName.setText("");
                        edCode.setText("");
                        edInstitudeAddress.setText("");

                        //Empty all imageView
                        Bitmap bitmap = BitmapFactory.decodeResource(InstitudeDetails.this.getResources(), R.drawable.upload_img);
                        imgLogo.setImageBitmap(bitmap);
                        imgSignature.setImageBitmap(bitmap);
                        edInstitudeName.getFocusable();
//.....................................................................................................
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
        return true;
    }

    //Load Image From Gallery..........................................................................................
// public void loadImagefromGallery() {
//     // Create intent to Open Image applications like Gallery, Google Photos
//     Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//     // Start the Intent
//     startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
// }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // When an Image is picked for logo
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                logoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                imgLogo.setImageBitmap(logoBitmap);


            }
            //image pick for signature
            else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                signatureBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                imgSignature.setImageBitmap(signatureBitmap);

            } else {

                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {

            Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
//....................................................................................................................


//....................................................................................................................


    //Base64 Image Conversion.............................................................
    private String logoimageToString() {
        ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();
        if (logoBitmap != null) {
            logoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);

        }
        byte[] imgByte = btyeArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
//.......................................................................................

    //Base64 Image Conversion.............................................................
    private String SignatureimageToString() {
        ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();
        if (signatureBitmap != null) {
            signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);

        }
        byte[] imgByte = btyeArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
//.......................................................................................


}
