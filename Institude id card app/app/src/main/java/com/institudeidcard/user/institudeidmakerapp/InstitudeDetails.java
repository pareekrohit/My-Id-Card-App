package com.institudeidcard.user.institudeidmakerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Institude;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudeDetail_pojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.Mobile_no_pojo;
import com.institudeidcard.user.institudeidmakerapp.SharedPreference_class.StudentSavePref;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InstitudeDetails extends AppCompatActivity {
    Bitmap logoBitmap, signatureBitmap;
    ImageView imgLogo, imgSignature;

    String Institudename;
    String code;
    String Institudeaddress;
    String ImageLogo;
    String signature;
    String email, mobile;

    String reg_email;

    EditText edInstitudeName, edCode, edInstitudeAddress, edemail, edmobile;
    ProgressDialog pd;

    //image comprssion variable
    int newWidth = 400;
    int newHeight = 400;

    int signatureWidth = 500;
    int signatureHeight = 150;

    Matrix matrix;
    Bitmap resizedBitmap, signResizedBitmap;

    float scaleWidth;
    float scaleHeight;
    ByteArrayOutputStream outputStream;
    Button btnintituteDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institude_details);


        btnintituteDetail = findViewById(R.id.btnintituteDetail);
        imgLogo = findViewById(R.id.img_logo);
        imgSignature = findViewById(R.id.ImgSinatureUpload);

        edInstitudeName = findViewById(R.id.edInstitudeName);
        edInstitudeAddress = findViewById(R.id.edInstitudeAddress);
        edCode = findViewById(R.id.edCode);
        edemail = findViewById(R.id.edemail);
        edmobile = findViewById(R.id.edInstitudeMobile);

        //get loged in user email id from registration and show in email textbox
        reg_email=StudentSavePref.getStr("email");
        edemail.setText(reg_email);

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

//        Drawable logo=imgLogo.getDrawable();
//        Drawable img_signature=imgSignature.getDrawable();
        Institudename = edInstitudeName.getText().toString();
        code = edCode.getText().toString();
        Institudeaddress = edInstitudeAddress.getText().toString();
        email = edemail.getText().toString();
        mobile = edmobile.getText().toString();

        int id = item.getItemId();
        isValideEmail(email);
        isValideMobile(mobile);
        System.out.println("logo///" + ImageLogo + " signature/////" + signature);
        if (id == R.id.menu_save) {

            if (ImageLogo.equals("") || signature.equals("") || Institudename.isEmpty() || Institudeaddress.isEmpty() || code.isEmpty() || mobile.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Fields Are Empty..", Toast.LENGTH_SHORT).show();
            } else if (isValideEmail(email) == false) {
                edemail.setError("Please Enter Valid Email");
                Toast.makeText(this, "Email is Not Valid..", Toast.LENGTH_SHORT).show();
            } else if (isValideMobile(mobile) == false) {
                edmobile.setError("Please Enter Valid mobile no");
                Toast.makeText(this, "Mobile is not Valid..", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View view = inflater.inflate(R.layout.save_dailog, null);
                builder.setView(view);

                // Add the buttons
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {

                        UploadTask up = new UploadTask(InstitudeDetails.this);
                        up.execute(code, Institudename, mobile, email, Institudeaddress, ImageLogo, signature);

                        //Empty all textview
                        edInstitudeName.setText("");
                        edCode.setText("");
                        edInstitudeAddress.setText("");
                        edemail.setText("");
                        edmobile.setText("");

                        //Empty all imageView
                        Bitmap bitmap = BitmapFactory.decodeResource(InstitudeDetails.this.getResources(), R.drawable.upload_img);
                        imgLogo.setImageBitmap(bitmap);
                        imgSignature.setImageBitmap(bitmap);


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

    public void imageLogoquality(Bitmap pic) {
        int imgWidth = pic.getWidth();
        int imgHeight = pic.getHeight();

        matrix = new Matrix();
        scaleWidth = ((float) newWidth) / imgWidth;
        scaleHeight = ((float) newHeight) / imgHeight;
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(0);

        resizedBitmap = Bitmap.createBitmap(pic, 0, 0, imgWidth, imgHeight, matrix, true);
        outputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        imgLogo.setImageBitmap(resizedBitmap);

    }

    public void imageSignaturequality(Bitmap pic) {
        int imgWidth = pic.getWidth();
        int imgHeight = pic.getHeight();

        matrix = new Matrix();
        scaleWidth = ((float) signatureWidth) / imgWidth;
        scaleHeight = ((float) signatureHeight) / imgHeight;
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(0);

        signResizedBitmap = Bitmap.createBitmap(pic, 0, 0, imgWidth, imgHeight, matrix, true);
        outputStream = new ByteArrayOutputStream();
        signResizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        imgSignature.setImageBitmap(signResizedBitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                logoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                // imgLogo.setImageBitmap(logoBitmap);
                imageLogoquality(logoBitmap);
            }

            //image pick for signature
            else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                signatureBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                //   imgSignature.setImageBitmap(signatureBitmap);
                imageSignaturequality(signatureBitmap);

            } else {

                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {

            Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
//....................................................................................................................


    //Base64 Image Conversion.............................................................
    private String logoimageToString() {
        ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();

        if (resizedBitmap != null) {
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);
        }
        byte[] imgByte = btyeArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
//.......................................................................................

    //Base64 Image Conversion.............................................................
    private String SignatureimageToString() {
        ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();
        if (signResizedBitmap != null) {
            signResizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);
        }
        byte[] imgByte = btyeArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
//.......................................................................................

}

//AyncTask Code..........................................................................
class UploadTask extends AsyncTask<String, Void, Void> {
    ProgressDialog pd;
    Context mctx;
    String code, Institudename, Institudeaddress, ImageLogo, signature, mobile, email, reg_mob;

    public UploadTask(Context mctx) {
        this.mctx = mctx;
    }

    @Override
    protected Void doInBackground(String... data) {
        code = data[0];
        Institudename = data[1];
        mobile = data[2];
        email = data[3];
        Institudeaddress = data[4];
        ImageLogo = data[5];
        signature = data[6];

        final String reg_mob = Mobile_no_pojo.mobile;
//        System.out.println("/////mobile----"+reg_mob);

//        Log.d("code ////", data[0]);
//        Log.d("mobile////", data[2]);
//        Log.d("email ////", data[3]);
//        System.out.println("mobile++"+data[2]);
//        System.out.println("email++"+data[3]);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api_Institude.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api_Institude Ar = retrofit.create(Api_Institude.class);
        Call<ImageClass> call = Ar.intitudeData(reg_mob, code, Institudename, mobile, email, Institudeaddress, ImageLogo, signature);

        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                pd.dismiss();
                //response like 200
                System.out.println("server response///////////////////" + response.code());
                //isSuccessful (true/false)
                System.out.println("server response///////////////////" + response.isSuccessful());

                ImageClass imageClass = response.body();
                Toast.makeText(mctx, "" + imageClass.getResponse(), Toast.LENGTH_LONG).show();
                System.out.println("///" + imageClass.getResponse());
                String res = imageClass.getResponse();
                if (res.equals("Data uploaded successfully...")) {

                    Intent i = new Intent(mctx, Intitude_Option.class);
                    i.putExtra("mobile", reg_mob);
                    mctx.startActivity(i);
                    ((InstitudeDetails) mctx).finish();
                }


            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {
                pd.dismiss();
                System.out.println("error :" + t.getMessage());
                Toast.makeText(mctx, "Error:Server Error", Toast.LENGTH_SHORT).show();
            }
        });

        return null;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(mctx);
        pd.setMessage("It May Take Some Time! Please Wait..");
        pd.show();
    }

    @Override
    protected void onPostExecute(Void result) {
        pd.dismiss();
    }
}


