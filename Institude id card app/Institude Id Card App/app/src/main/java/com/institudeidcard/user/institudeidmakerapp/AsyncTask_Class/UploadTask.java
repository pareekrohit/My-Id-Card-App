package com.institudeidcard.user.institudeidmakerapp.AsyncTask_Class;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Institude;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//public class UploadTask extends AsyncTask<String ,String,String> {
//    ProgressDialog pd;
//    Context mctx;
//    String selectedId, Image, name, clasess,division, dob, blood, mobile, address;
//
//    String res;
//
//    public UploadTask( Context mctx,String selectedId, String image, String name, String clasess, String division, String dob, String blood, String mobile, String address) {
//        this.mctx=mctx;
//        this.selectedId = selectedId;
//        Image = image;
//        this.name = name;
//        this.clasess = clasess;
//        this.division = division;
//        this.dob = dob;
//        this.blood = blood;
//        this.mobile = mobile;
//        this.address = address;
//
//    }
//
//    @Override
//    protected String doInBackground(String...data) {
//
//        Log.d("DoInbackground ////",data[0]);
//
//        //Toast.makeText(mctx, "DoInbackground data[0]////"+data[0], Toast.LENGTH_SHORT).show();
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Api_Institude.BASE_URL1)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        Api_Institude Ar = retrofit.create(Api_Institude.class);
//
//        Call<ImageClass> call = Ar.upload(selectedId, Image, name, clasess,division, dob, blood, mobile, address);
//        call.enqueue(new Callback<ImageClass>() {
//            @Override
//            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
//                //response like 200
//                System.out.println("server response///////////////////" + response.code());
//                //isSuccessful (true/false)
//                System.out.println("server response///////////////////" + response.isSuccessful());
//
//                try {
//                    ImageClass imageClass = response.body();
//                    Toast.makeText(mctx, "Status:" + imageClass.getResponse(), Toast.LENGTH_LONG).show();
//                    pd.dismiss();
//
//                    res="Saved Successfully...";
//
//                } catch (Exception e) {
//                    pd.dismiss();
//                    Toast.makeText(mctx, "Exception :" + e, Toast.LENGTH_LONG).show();
//                    System.out.println("Exception :" + e);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ImageClass> call, Throwable t) {
//                System.out.println("error :" + t.getMessage());
//                Toast.makeText(mctx, "Rohit...... :" +t.getMessage(), Toast.LENGTH_SHORT).show();
//                pd.dismiss();
//
//            }
//        });
//
//        return res;
//    }
//
////    @Override
////    protected void onPreExecute() {
////
////        pd = new ProgressDialog(mctx);
////        pd.setMessage("Wait while uploding the data to server..");
////        pd.show();
////    }
//
//    @Override
//    protected void onPostExecute(String res) {
//        pd.dismiss();
//
//
//    }
//
//
//}
