package com.institudeidcard.user.institudeidmakerapp.Interface;

import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.DataOfRegisteredUser;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.IDCardAllDetails;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.InstitudePojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.Registration_pojo;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.Response_class;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api_Student {

    //phpmyadmin device network url
//    String BASE_URL1 = "http://192.168.0.9:8081/imageupload/";
     String BASE_URL1 = "https://pareekrohit95.000webhostapp.com/imageupload/";

    //sms API
    String BASE_URLSMS = "http://www.eazy2sms.in/";

    //registration with OTP
    @FormUrlEncoded
    @POST("stud_register_api.php")
    Call<Registration_pojo> registrationData(
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("password") String password,
            @Field("otp") String otp
    );

    //Check OTP
    @FormUrlEncoded
    @POST("verify_stud_otp_api.php")
    Call<Registration_pojo> checkOTP(
            @Field("mobile") String mobile,
            @Field("otp") String otp
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

    //login
    @FormUrlEncoded
    @POST("stud_login_api.php")
    Call<Registration_pojo> login(
            @Field("mobile") String mobile,
            @Field("password") String password
    );

    //ForgetPassword
    @FormUrlEncoded
    @POST("stud_forget_pass_api.php")
    Call<Registration_pojo> forgetPassword(
            @Field("mobile") String mobile
    );

    //To get institude in spinner
    @GET("institude.php")
    Call<ArrayList<InstitudePojo>> showInstitude();

    //To get selected institude id
    @FormUrlEncoded
    @POST("all_institude_details.php")
    Call<InstitudePojo> showInstitudeId(@Field("institude") String institude);

    //To update all student detail to database
    @FormUrlEncoded
    @POST("uploadImage.php")
    Call<Response_class> upload(
//            @Field("status") String status,
            @Field("SID") String SID,
            @Field("id") String id,
            @Field("image") String image,
            @Field("name") String name,
            @Field("clasess") String clasess,
            @Field("division") String division,
            @Field("dob") String dob,
            @Field("blood") String blood,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("address") String address);

    //To upload all student detail to database
    @FormUrlEncoded
    @POST("insert_stud_api.php")
    Call<ImageClass> upload1(
            @Field("reg_mob") String reg_mob,
            @Field("id") String id,
            @Field("image") String image,
            @Field("name") String name,
            @Field("clasess") String clasess,
            @Field("division") String division,
            @Field("dob") String dob,
            @Field("blood") String blood,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("address") String address);


    //To fetch all student detail from database
    @FormUrlEncoded
    @POST("stud_details_api.php")
    Call<ImageClass> showStudentDetails(
            @Field("SID") String SID
    );

    //To fetch all student and institude details for preparing id card
    @FormUrlEncoded
    @POST("all_stud_insti_detail_api.php")
    Call<IDCardAllDetails> idcard(
            @Field("SID") String SID
    );


    //To fetch how many details of students are filled through this registered user
    @FormUrlEncoded
    @POST("register_stud_data_api.php")
    Call<ArrayList<DataOfRegisteredUser>> showStudentData(
            @Field("reg_mob") String reg_mob
    );

}
