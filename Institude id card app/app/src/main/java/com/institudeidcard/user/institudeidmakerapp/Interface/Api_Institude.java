package com.institudeidcard.user.institudeidmakerapp.Interface;

import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudeDetail_pojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudePojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudeResponse_pojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.Registration_pojo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api_Institude {
    //String BASE_URL = "http://192.168.0.6:8084/Institude_Id_Card_App_webservices/webresources/";

    //phpmyadmin device network url
  // String BASE_URL1 = "http://192.168.0.9:8081/imageupload/";
    String BASE_URL1 = "https://pareekrohit95.000webhostapp.com/imageupload/";

    //sms API
    String BASE_URLSMS = "http://www.eazy2sms.in/";


    @GET("institude.php")
    Call<ArrayList<InstitudePojo>> showInstitude();

    @FormUrlEncoded
    @POST("all_institude_details.php")
    Call<InstitudePojo> showInstitudeId(@Field("institude") String institude);


    @FormUrlEncoded
    @POST("uploadImage.php")
    Call<ImageClass> upload(@Field("id") String id,
                            @Field("image") String image,
                            @Field("name") String name,
                            @Field("clasess") String clasess,
                            @Field("division") String division,
                            @Field("dob") String dob,
                            @Field("blood") String blood,
                            @Field("email") String email,
                            @Field("mobile") String mobile,
                            @Field("address") String address);

    @FormUrlEncoded
    @POST("upload_institude_detail.php")
    Call<ImageClass> intitudeData(
            @Field("reg_mob") String reg_mob,
            @Field("code") String code,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("address") String address,
            @Field("logo") String logo,
            @Field("signature") String signature

    );


    //send otp via sms
    @POST("SMS.aspx")
    Call<Void> otpSMS(
            @Query("Mobile") String mobile,
            @Query("Message") String message,
            @Query("Type") String type,
            @Query("Userid") String userid,
            @Query("Password") String password
    );


    //registration with OTP
    @FormUrlEncoded
    @POST("register_api.php")
    Call<Registration_pojo> registrationData(
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("password") String password,
            @Field("otp") String otp
    );

    //Check OTP
    @FormUrlEncoded
    @POST("verify_otp_api.php")
    Call<Registration_pojo> checkOTP(
            @Field("mobile") String mobile,
            @Field("otp") String otp
    );

    //login
    @FormUrlEncoded
    @POST("login_api.php")
    Call<Registration_pojo> login(
            @Field("mobile") String mobile,
            @Field("password") String password
    );

    //ForgetPassword
    @FormUrlEncoded
    @POST("forget_pass_api.php")
    Call<Registration_pojo> forgetPassword(
            @Field("mobile") String mobile
    );


    //IDCardTemplate
    @FormUrlEncoded
    @POST("institude_detail_api.php")
    Call<InstitudeResponse_pojo> fetchIntitudeDetail(
            @Field("mobile") String mobile
    );

    //Send login mobile no to verify that some institute record is there with this mobile no
    @FormUrlEncoded
    @POST("mob_reg_api.php")
    Call<Registration_pojo> checkIntitudeData(
            @Field("mobile") String mobile
    );


    //Save Institute selected template to database
    @FormUrlEncoded
    @POST("templet_upload_api.php")
    Call<Registration_pojo> saveTemplate(
            @Field("mobile") String mobile,
            @Field("image") String image
    );
}
