package com.institudeidcard.user.institudeidmakerapp.Pojo_clasess;

import com.google.gson.annotations.SerializedName;

public class ImageClass {

    public ImageClass(String id, String image, String name, String clasess, String division, String DOB, String blood, String mobile, String address) {

        Image = image;
        Name = name;
        Clasess = clasess;
        Division = division;
        this.DOB = DOB;
        Blood = blood;
        Mobile = mobile;
        Address = address;

    }

    @SerializedName("id")
    public String Id;

    @SerializedName("image")
    public String Image;

    @SerializedName("name")
    public String Name;

    @SerializedName("clasess")
    public String Clasess;

    @SerializedName("division")
    public String Division;

    @SerializedName("dob")
    public String DOB;

    @SerializedName("blood")
    public String Blood;

    @SerializedName("mobile")
    public String Mobile;

    @SerializedName("address")
    public String Address;

    @SerializedName("response")
    public  String Response;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getClasess() {
        return Clasess;
    }

    public void setClasess(String clasess) {
        Clasess = clasess;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getBlood() {
        return Blood;
    }

    public void setBlood(String blood) {
        Blood = blood;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
