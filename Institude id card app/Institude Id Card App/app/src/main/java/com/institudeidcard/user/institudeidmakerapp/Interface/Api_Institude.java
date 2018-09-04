package com.institudeidcard.user.institudeidmakerapp.Interface;

import android.widget.ImageButton;

import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudeDetail_pojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudePojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.MyStatus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api_Institude {
    String BASE_URL = "http://192.168.0.4:8084/Institude_Id_Card_App_webservices/webresources/";

    String BASE_URL1 = "http://192.168.0.4/imageupload/";


    @GET("generic")
    Call<ArrayList<InstitudePojo>> showInstitude();

    @GET("generic/getInstitude")
    Call<InstitudePojo> showInstitudeId(@Query("institude") String institude);

    @GET("generic/getSave")
    Call<MyStatus> saveData(@Query("id") String id,
                            @Query("name") String name,
                            @Query("clasess") String clasess,
                            @Query("division") String division,
                            @Query("dob") String dob,
                            @Query("blood") String blood,
                            @Query("mobile") String mobile,
                            @Query("address") String address);


    @FormUrlEncoded
    @POST("upload.php")
    Call<ImageClass> uploadImage(@Field("image") String image);


    @FormUrlEncoded
    @POST("uploadImage.php")
    Call<ImageClass> upload(@Field("id") String id,
                            @Field("image") String image,
                            @Field("name") String name,
                            @Field("clasess") String clasess,
                            @Field("division") String division,
                            @Field("dob") String dob,
                            @Field("blood") String blood,
                            @Field("mobile") String mobile,
                            @Field("address") String address);


    @FormUrlEncoded
    @POST("upload_institude_detail.php")
    Call<ImageClass> intitudeData(
            @Field("code") String code,
            @Field("name") String name,
            @Field("address") String address,
            @Field("logo") String logo,
            @Field("signature") String signature);


}
