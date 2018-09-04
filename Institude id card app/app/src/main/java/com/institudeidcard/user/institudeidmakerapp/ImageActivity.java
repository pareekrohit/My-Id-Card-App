package com.institudeidcard.user.institudeidmakerapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Institude;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageActivity extends AppCompatActivity {

//    Context mContext;
//    ImageView image;
//    Button choose, upload;
//    final int PICK_IMAGE_REQUEST = 111;
//    String imgDecodableString;
//    Bitmap bitmap;
//    //Retrofit variables..
//
//    Gson gson = new GsonBuilder()
//            .setLenient()
//            .create();
//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(Api_Institude.BASE_URL1)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build();
//
//    Api_Institude Ar = retrofit.create(Api_Institude.class);
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image);
//
//
//        image = findViewById(R.id.image);
//        upload = findViewById(R.id.upload);
//        choose = findViewById(R.id.choose);
//
//
//        choose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadImagefromGallery();
//            }
//        });
//
//
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadImage();
//            }
//        });
//
//    }
//
//    //Load Image From Gallery..........................................................................................
//    public void loadImagefromGallery() {
//        // Create intent to Open Image applications like Gallery, Google Photos
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        // Start the Intent
//        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        try {
//            // When an Image is picked
//            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
//                System.out.println("/////" + requestCode);
//                // Get the Image from data
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                // Get the cursor
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//
//                // Move to first row
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imgDecodableString = cursor.getString(columnIndex);
//                cursor.close();
//
//                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
//
//                // Set the Image in ImageView after decoding the String
//                //image.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
//
//                image.setImageBitmap(bitmap);
//
//            } else {
//
//                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
//            }
//
//        } catch (Exception e) {
//
//            Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    public void uploadImage() {
//        String Image = imageToString();
//
//
//        Call<ImageClass> call = Ar.uploadImage(Image);
//
//
//        call.enqueue(new Callback<ImageClass>() {
//            @Override
//            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
//
//                //response like 200
//                System.out.println("server response///////////////////" + response.code());
//                //isSuccessful (true/false)
//                System.out.println("server response///////////////////" + response.isSuccessful());
//
//
//                    ImageClass imageClass = response.body();
//                    System.out.println("Server Response//// :" + imageClass.getResponse());
//                    Toast.makeText(ImageActivity.this, "" + imageClass.getResponse(), Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onFailure(Call<ImageClass> call, Throwable t) {
////
//                System.out.println("error :" +t.getCause());
//                Toast.makeText(ImageActivity.this, "error :" + call, Toast.LENGTH_SHORT).show();
//            }
//        });
//
////.....................................................................................................
//
//    }
//
//    private String imageToString() {
//        ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);
//        byte[] imgByte = btyeArrayOutputStream.toByteArray();
//        return Base64.encodeToString(imgByte, Base64.DEFAULT);
//    }


}
