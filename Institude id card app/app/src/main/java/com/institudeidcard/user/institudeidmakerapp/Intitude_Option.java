package com.institudeidcard.user.institudeidmakerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Institude;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.Registration_pojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Intitude_Option extends AppCompatActivity {

String mobile,reg_mob;

ProgressDialog pd;
Button btnintituteDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intitude__option);

        btnintituteDetail=findViewById(R.id.btnintituteDetail);

        mobile=getIntent().getExtras().getString("mobile");
//        reg_mob=getIntent().getExtras().getString("reg_mobile");

        System.out.println("mobile////"+mobile);


        //Send login mobile no to verify that some institute record is there with this mobile no
        //if record exist then set enable false to institude details button.

        if (mobile!=null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api_Institude.BASE_URL1)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Api_Institude Ar = retrofit.create(Api_Institude.class);
            Call<Registration_pojo> call = Ar.checkIntitudeData(mobile);

            pd = new ProgressDialog(Intitude_Option.this);
            pd.setMessage("Please Wait..");
            pd.show();
            call.enqueue(new Callback<Registration_pojo>() {
                @Override
                public void onResponse(Call<Registration_pojo> call, Response<Registration_pojo> response) {
//                //response like 200
//                System.out.println("server response///////////////////" + response.code());
//                //isSuccessful (true/false)
//                System.out.println("server response///////////////////" + response.isSuccessful());

                    Registration_pojo po = response.body();
                    System.out.println("Response from server////////" + po.getResponse());

                    String res=po.getResponse();
                    if(res.equals("Record Present ")){
                        btnintituteDetail.setEnabled(false);
                        btnintituteDetail.setBackgroundResource(R.color.brown);
                    }

                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<Registration_pojo> call, Throwable t) {
                    System.out.println("error :" + t.getMessage());
                    Toast.makeText(Intitude_Option.this, "Error:Server error" , Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

        }



    }

    public void showInstitudeDetail(View v) {
        Intent i = new Intent(Intitude_Option.this, InstitudeDetails.class);
        startActivity(i);
    }

    public void showIDCardTemplate(View v) {
        Intent i = new Intent(Intitude_Option.this, IDCardTemplates.class);
        i.putExtra("mobile",mobile);
        startActivity(i);
    }

    //Logout button on ActionBar Code................................
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View view = inflater.inflate(R.layout.logout_dailog, null);
            builder.setView(view);

            // Add the buttons
            builder.setPositiveButton("Logout Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                    Intent intent = new Intent(Intitude_Option.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}



