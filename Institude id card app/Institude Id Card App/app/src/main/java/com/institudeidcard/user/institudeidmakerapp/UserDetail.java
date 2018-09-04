package com.institudeidcard.user.institudeidmakerapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Institude;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudePojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.MyStatus;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDetail extends AppCompatActivity {
    ImageButton imgpic;

    EditText edDOB, edName, edMobile, edAddress;
    final Calendar myCalendar = Calendar.getInstance();

    //image from gallery
    private static int RESULT_LOAD_IMG;
    String imgDecodableString;
    ImageView imgView;
    Bitmap bitmap;
    Spinner spinnerInstitude, spinnerClass, spinnerDivision, spinnerBlood;

    ArrayAdapter<String> adapterInstitude;
    ArrayList<String> institudeList = new ArrayList<>();

    public String value;
    public String institude;
    public String division;
    public String clasess;
    public String blood;
    public String name;
    public String mobile;
    public String address;
    public String dob;
    public String selectedId;

    //Retrofit variables..
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api_Institude.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    Api_Institude Ar = retrofit.create(Api_Institude.class);


    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        imgpic = findViewById(R.id.btnSelectPic);
        spinnerInstitude = findViewById(R.id.spinnerIntitudeName);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerBlood = findViewById(R.id.spinnerBlood);
        spinnerDivision = findViewById(R.id.spinnerDivision);

        edName = findViewById(R.id.edName);
        edMobile = findViewById(R.id.edMobile);
        edAddress = findViewById(R.id.edAddress);

        edDOB = findViewById(R.id.edDOB);
        imgView = findViewById(R.id.img_profile);

        adapterInstitude = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, institudeList);
        spinnerInstitude.setAdapter(adapterInstitude);


//Calender code...................................................................................................................
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateedDOB();
            }
        };

        edDOB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(UserDetail.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
//................................................................................................


        imgpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  selectImage();
                loadImagefromGallery();
            }
        });

//Web Services code to fetch the institude in spinner............................................................
        //Login from server


        Call<ArrayList<InstitudePojo>> call = Ar.showInstitude();


        call.enqueue(new Callback<ArrayList<InstitudePojo>>() {
            @Override
            public void onResponse(Call<ArrayList<InstitudePojo>> call, Response<ArrayList<InstitudePojo>> response) {
                System.out.println("1");

                //response like 200
                System.out.println("server response///////////////////" + response.code());
                //isSuccessful (true/false)
                System.out.println("server response///////////////////" + response.isSuccessful());


                //response body respond data from server
                Log.d("error", "" + response.body());

                try {

                    //reponse-URL
                    System.out.println("///////////////////" + response);
                    System.out.println("+++" + response.body());

                    ArrayList<InstitudePojo> institudePojo = response.body();
                    for (InstitudePojo po : institudePojo) {

                        System.out.println("Institude Name :" + po.getInstitude_id() + po.getInstitude_name());
                        //System.out.println("Institude id :"+po.getInstitude_id());
                        institudeList.add(po.getInstitude_name());

                    }
                    adapterInstitude.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(UserDetail.this, "Exception :" + e, Toast.LENGTH_LONG).show();
                    System.out.println("Exception :" + e);
                    Log.d("Exception :", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InstitudePojo>> call, Throwable t) {
                Log.d("error", "" + t.getMessage());
                System.out.println("error :" + t.getMessage());
                Toast.makeText(UserDetail.this, "error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//.....................................................................................................

//spinner listner to get the select item id from server database......................................................................

        spinnerInstitude.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                value = spinnerInstitude.getSelectedItem().toString();

                Call<InstitudePojo> call = Ar.showInstitudeId(value);
                call.enqueue(new Callback<InstitudePojo>() {
                    @Override
                    public void onResponse(Call<InstitudePojo> call, Response<InstitudePojo> response) {
                        //response like 200
                        System.out.println("server response///////////////////" + response.code());
                        //isSuccessful (true/false)
                        System.out.println("server response///////////////////" + response.isSuccessful());


                        //response body respond data from server
                        Log.d("error", "" + response.body());

                        try {

                            //reponse-URL
                            System.out.println("///////////////////" + response);


                            InstitudePojo institudePojo = response.body();
                            System.out.println("Institude Name :" + institudePojo.getInstitude_id());
                            // Toast.makeText(UserDetail.this, "Select institude id :" + institudePojo.getInstitude_id(), Toast.LENGTH_SHORT).show();
                            selectedId = String.valueOf(institudePojo.getInstitude_id());

                        } catch (Exception e) {
                            Toast.makeText(UserDetail.this, "Exception :" + e, Toast.LENGTH_LONG).show();
                            System.out.println("Exception :" + e);
                            Log.d("Exception :", "" + e);
                        }
                    }

                    @Override
                    public void onFailure(Call<InstitudePojo> call, Throwable t) {
                        Log.d("error", "" + t.getMessage());
                        System.out.println("error :" + t.getMessage());
                        Toast.makeText(UserDetail.this, "error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

//.....................................................................................................


            }

            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UserDetail.this, "Please Select Institude Name", Toast.LENGTH_SHORT).show();
            }
        });

//....................................................................
    }

    //select image method.................................................................................................
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UserDetail.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UserDetail.this);

                if (items[item].equals("Take Photo")) {
                    //  userChoosenTask="Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    //  userChoosenTask="Choose from Library";
                    if (result)
                        loadImagefromGallery();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
//......................................................................................................................

    private void updateedDOB() {
        String myFormat = "yyyy/mm/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        edDOB.setText(sdf.format(myCalendar.getTime()));
    }

    //Save button on ActionBar Code................................
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final String Image = imageToString();

        institude = spinnerInstitude.getSelectedItem().toString();
        clasess = spinnerClass.getSelectedItem().toString();
        blood = spinnerBlood.getSelectedItem().toString();
        division = spinnerDivision.getSelectedItem().toString();

        name = edName.getText().toString();
        dob = edDOB.getText().toString();
        mobile = edMobile.getText().toString();
        address = edAddress.getText().toString();


        int id = item.getItemId();


        if (id == R.id.menu_save) {


            if (Image.isEmpty() || name.isEmpty() || address.isEmpty() || clasess.equals("--Select--") || blood.equals("--Select--") || division.equals("--Select--") || dob.isEmpty()) {
                Toast.makeText(this, "Fields Are Empty..", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View view = inflater.inflate(R.layout.save_dailog, null);
                builder.setView(view);

                // Add the buttons
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


//                        UploadTask up = new UploadTask(UserDetail.this,selectedId, Image, name, clasess,division, dob, blood, mobile, address);
//                        up.execute();

                        UploadTask up = new UploadTask();
                        up.execute();


                        //showing progress dialog
//                        pd = new ProgressDialog(UserDetail.this);
//                        pd.setMessage("Wait while uploding the data to server..");
//                        pd.show();

//                        Gson gson = new GsonBuilder()
//                                .setLenient()
//                                .create();
//                        Retrofit retrofit = new Retrofit.Builder()
//                                .baseUrl(Api_Institude.BASE_URL1)
//                                .addConverterFactory(GsonConverterFactory.create(gson))
//                                .build();
//
//                        Api_Institude Ar = retrofit.create(Api_Institude.class);
//
//                        Call<ImageClass> call = Ar.upload(selectedId, Image, name, clasess,division, dob, blood, mobile, address);
//                        call.enqueue(new Callback<ImageClass>() {
//                            @Override
//                            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
//                                //response like 200
//                                System.out.println("server response///////////////////" + response.code());
//                                //isSuccessful (true/false)
//                                System.out.println("server response///////////////////" + response.isSuccessful());
//
//                                try {
//                                    ImageClass imageClass = response.body();
//                                    Toast.makeText(UserDetail.this, "Status:" + imageClass.getResponse(), Toast.LENGTH_LONG).show();
//                                    pd.dismiss();
//
//                                } catch (Exception e) {
//                                    pd.dismiss();
//                                    Toast.makeText(UserDetail.this, "Exception :" + e, Toast.LENGTH_LONG).show();
//                                    System.out.println("Exception :" + e);
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<ImageClass> call, Throwable t) {
//                                System.out.println("error :" + t.getMessage());
//                                Toast.makeText(UserDetail.this, "Rohit...... :" +t.getMessage(), Toast.LENGTH_SHORT).show();
//                                pd.dismiss();
//
//                            }
//                        });
                        edName.setText("");
                        edAddress.setText("");
                        edMobile.setText("");
                        edDOB.setText("");
                        imgView.setImageBitmap(null);
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
//..................................................................................................................


    //Load Image From Gallery..........................................................................................
    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG = 0);
    }

    //load image from camera.......................................................................................
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_LOAD_IMG = 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                imgView.setImageBitmap(bitmap);

            } else {

                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {

            Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
//....................................................................................................................

    //Base64 Image Conversion.............................................................
    private String imageToString() {
        ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);

        }
        byte[] imgByte = btyeArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
//.......................................................................................


    //check masmallow permission other app will crash................................................
    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }
//.............................................................................................................

}


class UploadTask extends AsyncTask<String, String, String> {
    ProgressDialog pd;
    Context mctx;
    String selectedId, Image, name, clasess, division, dob, blood, mobile, address;

    String res;


    @Override
    protected String doInBackground(String... data) {


        //Log.d("DoInbackground ////", data[0]);

        //Toast.makeText(mctx, "DoInbackground data[0]////"+data[0], Toast.LENGTH_SHORT).show();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api_Institude.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api_Institude Ar = retrofit.create(Api_Institude.class);

        Call<ImageClass> call = Ar.upload(selectedId, Image, name, clasess, division, dob, blood, mobile, address);
        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                //response like 200
                System.out.println("server response///////////////////" + response.code());
                //isSuccessful (true/false)
                System.out.println("server response///////////////////" + response.isSuccessful());

                try {
                    ImageClass imageClass = response.body();
                    Toast.makeText(mctx, "Status:" + imageClass.getResponse(), Toast.LENGTH_LONG).show();
                    pd.dismiss();

                    res = "Saved Successfully...";

                } catch (Exception e) {
                    pd.dismiss();
                    Toast.makeText(mctx, "Exception :" + e, Toast.LENGTH_LONG).show();
                    System.out.println("Exception :" + e);
                }
            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {
              try {
                  System.out.println("error :" + t.getMessage());
                  Toast.makeText(mctx, "Rohit...... :" + t.getMessage(), Toast.LENGTH_SHORT).show();
              }catch (Exception e){
                  System.out.println("////ee"+e.getMessage());
              }

                // pd.dismiss();

            }
        });

        return res;
    }

//    @Override
//    protected void onPreExecute() {
//
//        pd = new ProgressDialog(mctx);
//        pd.setMessage("Wait while uploding the data to server..");
//        pd.show();
//    }

    @Override
    protected void onPostExecute(String res) {
//        pd.dismiss();


    }


}

