package com.institudeidcard.user.institudeidmakerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.bumptech.glide.load.engine.Resource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Institude;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudePojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudeResponse_pojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.Registration_pojo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IDCardTemplates extends AppCompatActivity {
//    Spinner spinnerInstitude;

    //    ArrayAdapter<String> adapterInstitude;
//    ArrayList<String> institudeList = new ArrayList<>();
    LinearLayout linear, linearbutton;

    MenuItem menu_save;

    ScrollView scrollView;
    RelativeLayout relative_layout;

    int[] arr = {R.drawable.cc, R.drawable.a, R.drawable.bb, R.drawable.dd, R.drawable.ee};

    Button btnPrev, btnNext;
    int i = 0;

    //    String spinnerItemName;
    TextView txtInstitudeName, txtInstitudeAddress;
    ImageView img_school_logo, img_signature;

    String reg_mobile;
    CardView cardview;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_templates);

        reg_mobile = getIntent().getExtras().getString("mobile");

        cardview = findViewById(R.id.cardview);
        txtInstitudeName = findViewById(R.id.txtInstitudeName);
        txtInstitudeAddress = findViewById(R.id.txtInstitudeAddress);
        img_school_logo = findViewById(R.id.img_school_logo);
        img_signature = findViewById(R.id.img_signature);

        scrollView = findViewById(R.id.scrollView);
        relative_layout=findViewById(R.id.relative_layout);

        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);


//for displaying next template.......................
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i >= 4) {
                    Toast.makeText(IDCardTemplates.this, "Click Prev", Toast.LENGTH_SHORT).show();
                } else {
                    i++;
                    changeBackground();
                }
            }
        });
//for displaying previous template....................
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i <= 0) {
                    Toast.makeText(IDCardTemplates.this, "Click Next", Toast.LENGTH_SHORT).show();
                } else {
                    i--;
                    changeBackground();
                }
            }
        });

//        UploadInstitudeName up = new UploadInstitudeName(IDCardTemplates.this, spinnerInstitude, adapterInstitude, institudeList);
//        up.execute();


        //fetch all loged in user institude data to template
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api_Institude.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api_Institude Ar = retrofit.create(Api_Institude.class);
        Call<InstitudeResponse_pojo> call = Ar.fetchIntitudeDetail(reg_mobile);

        pd = new ProgressDialog(IDCardTemplates.this);
        pd.setTitle("Getting Your Information!");
        pd.setMessage("Please Wait..");
        pd.show();

        call.enqueue(new Callback<InstitudeResponse_pojo>() {
            @Override
            public void onResponse(Call<InstitudeResponse_pojo> call, Response<InstitudeResponse_pojo> response) {
                pd.dismiss();

                InstitudeResponse_pojo idp = response.body();
                String res = idp.getResponse();
                Toast.makeText(IDCardTemplates.this, "" + idp.getResponse(), Toast.LENGTH_SHORT).show();

                System.out.println("//response " + res);

                if (res.equals("Please Add Your Institude Data")) {
                    btnNext.setEnabled(false);
                    btnPrev.setEnabled(false);
                    cardview.setVisibility(View.GONE);

                    //menu item save button false
                    menu_save.setEnabled(false);

                } else if (res.equals("Data Found")){

                    System.out.println("Institude Name--////////" + idp.getName_of_intitude());
                    System.out.println("Address--////////" + idp.getAddress());
                    System.out.println("Logo////////" + idp.getLogo());
                    System.out.println("Signature////////" + idp.getSignature());
                    System.out.println("Template ////////" + idp.getTemplate());

                    txtInstitudeName.setText(idp.getName_of_intitude());
                    txtInstitudeAddress.setText(idp.getAddress());

                    //BASE64 decode string logo
                    String base64String = "data:image/jpg;base64," + idp.getLogo();
                    final String pureBase64Encoded = base64String.split(",")[1];
                    final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    img_school_logo.setImageBitmap(decodedBitmap);

//                        //BASE64 decode string signature
                    String base64Strin = "data:image/jpg;base64," + idp.getSignature();
                    final String pureBase64Encode = base64Strin.split(",")[1];
                    final byte[] decodedByte = Base64.decode(pureBase64Encode, Base64.DEFAULT);
                    Bitmap decodedBitma = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                    img_signature.setImageBitmap(decodedBitma);


//                    //BASE64 decode string Template
                    String base64Stri = "data:image/jpg;base64," + idp.getTemplate();
                    final String pureBase64Encod = base64Stri.split(",")[1];
                    final byte[] decodedByt = Base64.decode(pureBase64Encod, Base64.DEFAULT);
                    Bitmap decodedBitm = BitmapFactory.decodeByteArray(decodedByt, 0, decodedByt.length);
                    Drawable dr = new BitmapDrawable(decodedBitm);
                    scrollView.setBackground(dr);
                }
                else {

                    System.out.println("Institude Name--////////" + idp.getName_of_intitude());
                    System.out.println("Address--////////" + idp.getAddress());
                    System.out.println("Logo////////" + idp.getLogo());
                    System.out.println("Signature////////" + idp.getSignature());


                    txtInstitudeName.setText(idp.getName_of_intitude());
                    txtInstitudeAddress.setText(idp.getAddress());

                    //BASE64 decode string logo
                    String base64String = "data:image/jpg;base64," + idp.getLogo();
                    final String pureBase64Encoded = base64String.split(",")[1];
                    final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    img_school_logo.setImageBitmap(decodedBitmap);

//                        //BASE64 decode string signature
                    String base64Strin = "data:image/jpg;base64," + idp.getSignature();
                    final String pureBase64Encode = base64Strin.split(",")[1];
                    final byte[] decodedByte = Base64.decode(pureBase64Encode, Base64.DEFAULT);
                    Bitmap decodedBitma = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                    img_signature.setImageBitmap(decodedBitma);

                }


            }

            @Override
            public void onFailure(Call<InstitudeResponse_pojo> call, Throwable t) {
                pd.dismiss();
                System.out.println("error :" + t.getMessage());
                Toast.makeText(IDCardTemplates.this, "Error:Server error", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void changeBackground() {
        scrollView.setBackgroundResource(arr[i]);
    }

    //Save button on ActionBar Code................................
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);

        menu_save = menu.findItem(R.id.menu_save);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_save) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View view = inflater.inflate(R.layout.save_dailog, null);
            builder.setView(view);

            // Add the buttons
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            arr[i]);

                    ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();
                    icon.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);
                    byte[] imgByte = btyeArrayOutputStream.toByteArray();
                    System.out.println("base64" + Base64.encodeToString(imgByte, Base64.DEFAULT));
                    String convertedImage = Base64.encodeToString(imgByte, Base64.DEFAULT);


                    SaveTemplate st = new SaveTemplate(IDCardTemplates.this);
                    st.execute(reg_mobile, convertedImage);

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }

}

//class UploadInstitudeName extends AsyncTask<String, Void, ArrayList<String>> {
//    ProgressDialog pd;
//    Context mctx;
//
//    Spinner spinnerInstitude;
//    ArrayAdapter<String> adapterInstitude;
//    ArrayList<String> institudeList = new ArrayList<>();
//
//    public UploadInstitudeName(Context mctx, Spinner spinnerInstitude, ArrayAdapter<String> adapterInstitude, ArrayList<String> institudeList) {
//        this.mctx = mctx;
//        this.spinnerInstitude = spinnerInstitude;
//        this.adapterInstitude = adapterInstitude;
//        this.institudeList = institudeList;
//    }
//
//    @Override
//    protected ArrayList<String> doInBackground(String... data) {
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Api_Institude.BASE_URL1)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        Api_Institude Ar = retrofit.create(Api_Institude.class);
//
//        Call<ArrayList<InstitudePojo>> call = Ar.showInstitude();
//
//        call.enqueue(new Callback<ArrayList<InstitudePojo>>() {
//            @Override
//            public void onResponse(Call<ArrayList<InstitudePojo>> call, Response<ArrayList<InstitudePojo>> response) {
//
//
//                //response like 200
//                System.out.println("server response///////////////////" + response.code());
//                //isSuccessful (true/false)
//                System.out.println("server response///////////////////" + response.isSuccessful());
//
//                try {
//
//                    //reponse-URL
//                    System.out.println("///////////////////" + response);
//                    System.out.println("+++" + response.body());
//
//                    ArrayList<InstitudePojo> institudePojo = response.body();
//                    for (InstitudePojo po : institudePojo) {
//
//                        //   System.out.println("Institude Name :" + po.getIID() + po.getName_of_intitude());
//                        institudeList.add(po.getName_of_intitude());
//
//                    }
//                    adapterInstitude.notifyDataSetChanged();
//
//                } catch (Exception e) {
//                    Toast.makeText(mctx, "Exception :" + e, Toast.LENGTH_LONG).show();
//                    System.out.println("Exception :" + e);
//                    Log.d("Exception :", "" + e);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<InstitudePojo>> call, Throwable t) {
//                Log.d("error", "" + t.getMessage());
//                System.out.println("error :" + t.getMessage());
//                Toast.makeText(mctx, "error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        return institudeList;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        pd = new ProgressDialog(mctx);
//        pd.setMessage("Please Wait..");
//        pd.show();
//    }
//
//    @Override
//    protected void onPostExecute(ArrayList<String> InstitudeList) {
//        pd.dismiss();
//        adapterInstitude = new ArrayAdapter<String>(mctx, android.R.layout.simple_spinner_dropdown_item, institudeList);
//        spinnerInstitude.setAdapter(adapterInstitude);
//    }
//}

//fetch template
class SaveTemplate extends AsyncTask<String, Void, Integer> {
    ProgressDialog pd;
    Context mctx;
    String mobile, image, institute;

    public SaveTemplate(Context mctx) {
        this.mctx = mctx;
    }

    @Override
    protected Integer doInBackground(String... data) {
        mobile = data[0];
        image = data[1];

        Log.d("mobile:- ////", data[0]);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api_Institude.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api_Institude Ar = retrofit.create(Api_Institude.class);
        Call<Registration_pojo> call = Ar.saveTemplate(mobile, image);

        call.enqueue(new Callback<Registration_pojo>() {
            @Override
            public void onResponse(Call<Registration_pojo> call, Response<Registration_pojo> response) {
                //response like 200
                System.out.println("server response///////////////////save" + response.code());
                //isSuccessful (true/false)
                System.out.println("server response///////////////////save" + response.isSuccessful());

                Registration_pojo po = response.body();
                Toast.makeText(mctx, "" + po.getResponse(), Toast.LENGTH_SHORT).show();
                System.out.println("Response////" + po.getResponse());
            }

            @Override
            public void onFailure(Call<Registration_pojo> call, Throwable t) {
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
        pd.setTitle("Saving Template!");
        pd.setMessage("Please Wait..");
        pd.show();
    }

    @Override
    protected void onPostExecute(Integer result) {
        pd.dismiss();

    }
}


