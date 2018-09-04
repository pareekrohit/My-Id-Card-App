package com.institudeidcard.user.institudeidmakerapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Student;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudePojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.Response_class;
import com.institudeidcard.user.institudeidmakerapp.SharedPreference_class.StudentSavePref;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDetail extends AppCompatActivity {
    ImageButton imgpic;

    EditText edDOB, edName, edMobile, edAddress, edemail;
    final Calendar myCalendar = Calendar.getInstance();

    //image from gallery
    private static int RESULT_LOAD_IMG;

    ImageView imgView;
    //ImageView img_resize;
    Bitmap bitmap, bitmap1;
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
    public String email;

    //Retrofit variables..
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api_Institude.BASE_URL1)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    Api_Student Ar = retrofit.create(Api_Student.class);

    //image comprssion variable
    int newWidth = 413;
    int newHeight = 531;
    Matrix matrix;
    Bitmap resizedBitmap;
    float scaleWidth;
    float scaleHeight;
    ByteArrayOutputStream outputStream;

    String reg_mob, status, SID;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        //get the login mobile number through shared preference(session)
        reg_mob = StudentSavePref.getStr("mobile");
        System.out.println("on load reg_mob"+reg_mob);
        SID = StudentSavePref.getSID("SID");
        // System.out.println("UserDetail SID////"+SID);
//        StudFetchTask st=new StudFetchTask(UserDetail.this, edDOB, edName, edMobile, edAddress, edemail,spinnerClass, spinnerDivision, spinnerBlood,imgView);
//        st.execute(reg_mob);

        imgpic = findViewById(R.id.btnSelectPic);
        spinnerInstitude = findViewById(R.id.spinnerIntitudeName);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerBlood = findViewById(R.id.spinnerBlood);
        spinnerDivision = findViewById(R.id.spinnerDivision);

        edName = findViewById(R.id.edName);
        edMobile = findViewById(R.id.edMobile);
        edAddress = findViewById(R.id.edAddress);
        edemail = findViewById(R.id.edEmail);
        edDOB = findViewById(R.id.edDOB);
        imgView = findViewById(R.id.img_profile);

        adapterInstitude = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, institudeList);
        spinnerInstitude.setAdapter(adapterInstitude);


        //Student Data Fetch code
        Call<ImageClass> call1 = Ar.showStudentDetails(SID);
        pd = new ProgressDialog(UserDetail.this);
        pd.setTitle("Checking Loged Student Data!");
        pd.setMessage("Please Wait..");
        pd.show();

        call1.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call1, Response<ImageClass> response) {
                pd.dismiss();
                ImageClass imageClass = response.body();
                Toast.makeText(UserDetail.this, "" + imageClass.getResponse(), Toast.LENGTH_LONG).show();
                System.out.println("response:-" + imageClass.getResponse());

                System.out.println("Name/////" + imageClass.getName());
                System.out.println("Institude Name/////" + imageClass.getName_of_institude());
                System.out.println("Address/////" + imageClass.getAddress());
                System.out.println("Mobile/////" + imageClass.getMobile());
                System.out.println("Division/////" + imageClass.getDivision());
                System.out.println("Email/////" + imageClass.getEmail());
                System.out.println("Blood/////" + imageClass.getBlood());
                System.out.println("DOB/////" + imageClass.getDOB());
                System.out.println("image/////" + imageClass.getImage());


                //if response is equal to 'Data Found' then set the editext value as per server response.
//                if (imageClass.getResponse() == "Data Found") {

                String res = imageClass.getResponse();
                if (res.equals("Data Found")) {
                    System.out.println("res:-" + res);
                    new StudentSavePref(UserDetail.this).setStatus("status", "1");
//                    System.out.println("status:-" + StudentSavePref.getStatus("status"));

                    //BASE64 String decode and display in student image
                    String base64String = "data:image/jpg;base64," + imageClass.getImage();
                    final String pureBase64Encoded = base64String.split(",")[1];
                    final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    imgView.setImageBitmap(decodedBitmap);

                    edName.setText(imageClass.getName());
                    edDOB.setText(imageClass.getDOB());
                    edemail.setText(imageClass.getEmail());
                    edMobile.setText(imageClass.getMobile());
                    edAddress.setText(imageClass.getAddress());

                    for (int i = 0; i < spinnerClass.getCount(); i++) {
                        if (spinnerClass.getItemAtPosition(i).equals(imageClass.getClasess())) {
                            System.out.println("//spinerValue:-" + i);
                            spinnerClass.setSelection(i);
                        }
                    }

                    for (int i = 0; i < spinnerBlood.getCount(); i++) {
                        if (spinnerBlood.getItemAtPosition(i).equals(imageClass.getBlood())) {
                            System.out.println("//spinerValue:-" + i);
                            spinnerBlood.setSelection(i);
                        }
                    }
                    for (int i = 0; i < spinnerDivision.getCount(); i++) {
                        if (spinnerDivision.getItemAtPosition(i).equals(imageClass.getDivision())) {
                            System.out.println("//spinerValue:-" + i);
                            spinnerDivision.setSelection(i);
                        }
                    }

                    for (int i = 0; i < spinnerInstitude.getCount(); i++) {
                        if (spinnerInstitude.getItemAtPosition(i).equals(imageClass.getName_of_institude())) {
                            System.out.println("//spinerValue:-" + i);
                            spinnerInstitude.setSelection(i);
                        }
                    }
                } else {
                    //Please Insert Data
                    new StudentSavePref(UserDetail.this).setStatus("status", "0");
                }
            }


            @Override
            public void onFailure(Call<ImageClass> call1, Throwable t) {
                pd.dismiss();
                System.out.println("error :" + t.getMessage());
                Toast.makeText(UserDetail.this, "Error:server error", Toast.LENGTH_SHORT).show();
                //  pd.dismiss();
            }
        });

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
                new DatePickerDialog(UserDetail.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
//................................................................................................................................


        imgpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                //loadImagefromGallery();
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

                        System.out.println("Institude Name :" + po.getName_of_intitude());
                        //System.out.println("Institude id :"+po.getInstitude_id());
                        institudeList.add(po.getName_of_intitude());

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
                Toast.makeText(UserDetail.this, "error :server error", Toast.LENGTH_SHORT).show();
            }
        });

//.....................................................................................................

//spinner listner to get the select item id from server database......................................................................

        spinnerInstitude.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                value = spinnerInstitude.getSelectedItem().toString();

                System.out.println("values" + value);

                Call<InstitudePojo> call = Ar.showInstitudeId(value);
                call.enqueue(new Callback<InstitudePojo>() {
                    @Override
                    public void onResponse(Call<InstitudePojo> call, Response<InstitudePojo> response) {
                        //response like 200
                        System.out.println("server response///////////////////" + response.code());
                        //isSuccessful (true/false)
                        System.out.println("server response///////////////////" + response.isSuccessful());

                        //reponse-URL
                        System.out.println("///////////////////" + response);
                        InstitudePojo institudePojo = response.body();
                        System.out.println("Institude Id :" + institudePojo.getIID());

                        // Toast.makeText(UserDetail.this, "Select institude id :" + institudePojo.getIID(), Toast.LENGTH_SHORT).show();
                        selectedId = String.valueOf(institudePojo.getIID());

                    }

                    @Override
                    public void onFailure(Call<InstitudePojo> call, Throwable t) {
                        Log.d("error", "" + t.getMessage());
                        System.out.println("error :" + t.getCause());
                        Toast.makeText(UserDetail.this, "error :server error", Toast.LENGTH_SHORT).show();
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

    public void imagequality(Bitmap pic) {

        int imgWidth = pic.getWidth();
        int imgHeight = pic.getHeight();

        System.out.println("image width:" + imgWidth + " image Height :" + imgHeight);
        matrix = new Matrix();
        scaleWidth = ((float) newWidth) / imgWidth;
        scaleHeight = ((float) newHeight) / imgHeight;
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(0);

        resizedBitmap = Bitmap.createBitmap(pic, 0, 0, imgWidth, imgHeight, matrix, true);
        outputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        imgView.setImageBitmap(resizedBitmap);
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
        //String myFormat = "yyyy/mm/dd"; //In which you need put here
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
        email = edemail.getText().toString();


        int id = item.getItemId();
        if (id == R.id.menu_save) {

            isValideEmail(email);
            isValideMobile(mobile);

            if (email.isEmpty() || Image.equals(R.drawable.b) || name.isEmpty() || address.isEmpty() || clasess.equals("--Select--") || blood.equals("--Select--") || division.equals("--Select--") || dob.isEmpty()) {

                Toast.makeText(this, "Fields Are Empty..", Toast.LENGTH_LONG).show();

            } else if (isValideEmail(email) == false) {
                edemail.setError("Please Enter Valid Email");
                Toast.makeText(this, "Email is Not Valid..", Toast.LENGTH_SHORT).show();
            } else if (isValideMobile(mobile) == false) {
                edMobile.setError("Please Enter Valid mobile no");
                Toast.makeText(this, "Mobile is not Valid..", Toast.LENGTH_SHORT).show();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View view = inflater.inflate(R.layout.save_dailog, null);
                builder.setView(view);

                // Add the buttons
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        status = StudentSavePref.getStatus("status");
                        System.out.println("Status now is :////" + status);
                        System.out.println("status///" + status);

                        if (status.equals("1")) {
                            UpdatedTask up = new UpdatedTask(UserDetail.this);
                            up.execute(SID, selectedId, Image, name, clasess, division, dob, blood, email, mobile, address);
                        } else if (status.equals("0")) {
                            System.out.println("If condition reg_mob"+reg_mob);
                            UploadTask1 up = new UploadTask1(UserDetail.this);
                            up.execute(reg_mob, selectedId, Image, name, clasess, division, dob, blood, email, mobile, address);
                        }

                        //   System.out.println("status/////:" + status);


                        edName.setText("");
                        edAddress.setText("");
                        edMobile.setText("");
                        edDOB.setText("");
                        edemail.setText("");
                        Bitmap bitmap = BitmapFactory.decodeResource(UserDetail.this.getResources(), R.drawable.b);
                        imgView.setImageBitmap(bitmap);

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

    private boolean isValideMobile(String mobile) {

        //mobile number validation.....................................................................
        String validmobile = "^[7-9][0-9]{9}$";
        Matcher matcher1 = Pattern.compile(validmobile).matcher(mobile);

        if (matcher1.matches()) {
            return true;
        } else {
            return false;
            //edMobile.setError("Please Enter Valid mobile no");
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
            //edemail.setError("Please Enter Correct Email");
        }
//.............................................................................................................................

    }
//..................................................................................................................


    //Load Image From Gallery..........................................................................................
    public void loadImagefromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 0);
    }

    //load image from camera.......................................................................................
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("request code :" + requestCode + " resultCode:" + resultCode + " data:" + data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                System.out.println("inside the camera if condition");

                Uri selectedImage = data.getData();
                System.out.println("URI of image" + selectedImage);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                // imgView.setImageBitmap(bitmap);
                imagequality(bitmap);


            } else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

                System.out.println("inside the camera if condition");
                //Uri selectedImage = data.getData();
                bitmap1 = (Bitmap) data.getExtras().get("data");
                // imgView.setImageBitmap(bitmap1);
                imagequality(bitmap1);


            } else {

                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {

            Toast.makeText(this, "Something went wrong:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
//....................................................................................................................

    //Base64 Image Conversion.............................................................
    private String imageToString() {

        BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);
        }


        byte[] imgByte = btyeArrayOutputStream.toByteArray();
        System.out.println("base64" + Base64.encodeToString(imgByte, Base64.DEFAULT));
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


    class UpdatedTask extends AsyncTask<String, Void, Void> {
        ProgressDialog pd;
        Context mctx;
        String selectedId, Image, name, clasess, division, dob, blood, email, mobile, address, SID, status;

        public UpdatedTask(Context mctx) {
            this.mctx = mctx;
        }

        @Override
        protected Void doInBackground(String... data) {
//            status = data[0];
            SID = data[0];
            selectedId = data[1];
            Image = data[2];
            name = data[3];
            clasess = data[4];
            division = data[5];
            dob = data[6];
            blood = data[7];
            email = data[8];
            mobile = data[9];
            address = data[10];

            Log.d("SID in upload task////", data[0]);
            Log.d("selected id////", data[1]);
            Log.d("name ////", data[4]);

//            System.out.println("status ////--" + data[0]);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api_Student.BASE_URL1)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Api_Student Ar = retrofit.create(Api_Student.class);
            Call<Response_class> call = Ar.upload(SID, selectedId, Image, name, clasess, division, dob, blood, email, mobile, address);
            call.enqueue(new Callback<Response_class>() {
                @Override
                public void onResponse(Call<Response_class> call, Response<Response_class> response) {
//                //response like 200
                    System.out.println("server response///////////////////" + response.code());
                    //isSuccessful (true/false)
                    System.out.println("server response///////////////////" + response.isSuccessful());
                    System.out.println("API Response :"+response);

                    Response_class imageClass = response.body();
                    Toast.makeText(mctx, "" + imageClass.getResponse(), Toast.LENGTH_LONG).show();
                    System.out.println("Response from server////////" + imageClass.getResponse());
                    pd.dismiss();

                }

                @Override
                public void onFailure(Call<Response_class> call, Throwable t) {
                    System.out.println("error :" + t.getMessage());
                    Toast.makeText(mctx, "Error: server error", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(mctx);
            pd.setMessage("Updating Data!");
            pd.setMessage("Wait while updating data..");
            pd.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();
        }
    }
}

class UploadTask1 extends AsyncTask<String, Void, Void> {
    ProgressDialog pd;
    Context mctx;

    String selectedId, Image, name, clasess, division, dob, blood, email, mobile, address, SID, status, reg_mob;

    public UploadTask1(Context mctx) {
        this.mctx = mctx;
    }

    @Override
    protected Void doInBackground(String... data) {
        reg_mob = data[0];
        selectedId = data[1];
        Image = data[2];
        name = data[3];
        clasess = data[4];
        division = data[5];
        dob = data[6];
        blood = data[7];
        email = data[8];
        mobile = data[9];
        address = data[10];

        Log.d("reg_mob ////", data[0]);
        Log.d("SelectedID////", data[1]);
        Log.d("name ////", data[4]);

        System.out.println("reg_mob ////--" + data[0]);
        System.out.println("reg_mob ////--" + data[1]);
        System.out.println("reg_mob ////--" + data[2]);
        System.out.println("reg_mob ////--" + data[3]);
        System.out.println("reg_mob ////--" + data[4]);
        System.out.println("reg_mob ////--" + data[5]);
        System.out.println("reg_mob ////--" + data[6]);
        System.out.println("reg_mob ////--" + data[7]);
        System.out.println("reg_mob ////--" + data[8]);
        System.out.println("reg_mob ////--" + data[9]);
        System.out.println("reg_mob ////--" + data[10]);


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api_Student.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api_Student Ar = retrofit.create(Api_Student.class);
        Call<ImageClass> call = Ar.upload1(reg_mob, selectedId, Image, name, clasess, division, dob, blood, email, mobile, address);
        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
//                //response like 200
                System.out.println("server response///////////////////" + response.code());
                //isSuccessful (true/false)
                System.out.println("server response///////////////////" + response.isSuccessful());

                // System.out.println("API hit : "+response.);

                ImageClass imageClass = response.body();
                Toast.makeText(mctx, "" + imageClass.getResponse(), Toast.LENGTH_LONG).show();
                System.out.println("Response from server////////" + imageClass.getResponse());
                pd.dismiss();


            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {
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
        pd.setMessage("Wait while uploding the data to server..");
        pd.show();
    }

    @Override
    protected void onPostExecute(Void result) {
        pd.dismiss();
    }
}






