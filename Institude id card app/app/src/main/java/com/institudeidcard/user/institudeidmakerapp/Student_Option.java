package com.institudeidcard.user.institudeidmakerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Institude;
import com.institudeidcard.user.institudeidmakerapp.Interface.Api_Student;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.DataOfRegisteredUser;
import com.institudeidcard.user.institudeidmakerapp.Pojo_clasess.ImageClass;
import com.institudeidcard.user.institudeidmakerapp.SharedPreference_class.StudentSavePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.institudeidcard.user.institudeidmakerapp.SharedPreference_class.StudentSavePref.PREFS_NAME;
import static com.institudeidcard.user.institudeidmakerapp.SharedPreference_class.StudentSavePref.getStudList;

public class Student_Option extends AppCompatActivity {


    //Retrofit variables..
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api_Student.BASE_URL1)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    Api_Student Ar = retrofit.create(Api_Student.class);

    String reg_mob;
    ProgressDialog pd;

    ArrayList<String> listItems = new ArrayList<String>();

    // ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__option);
        //get the login mobile number through shared preference(session)
        reg_mob = StudentSavePref.getStr("mobile");


        //Loged in user all student data
        Call<ArrayList<DataOfRegisteredUser>> call1 = Ar.showStudentData(reg_mob);
        pd = new ProgressDialog(Student_Option.this);
        pd.setTitle("Checking Your Data!");
        pd.setMessage("Loding...");
        pd.show();

        call1.enqueue(new Callback<ArrayList<DataOfRegisteredUser>>() {
            @Override
            public void onResponse(Call<ArrayList<DataOfRegisteredUser>> call1, Response<ArrayList<DataOfRegisteredUser>> response) {
                pd.dismiss();
                ArrayList<DataOfRegisteredUser> dataList = response.body();

                for (DataOfRegisteredUser po : dataList) {

                    System.out.println("Name/////" + po.getName());
                    System.out.println("SID/////" + po.getSID());
                    System.out.println("Image/////" + po.getPicture());
                    // System.out.println("Response/////" + po.getResponse());
                }


//Alert dialog box to show all student details which are saved with this loged in user
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(Student_Option.this);
                builderSingle.setIcon(R.drawable.person_icon);
                builderSingle.setTitle("Please select student");


                ArrayList<String> names = new ArrayList<>();
                ArrayList<String> picture = new ArrayList<>();

                for (DataOfRegisteredUser po : dataList) {

                    names.add(po.getName());
                    picture.add(po.getPicture());

                    StudentSavePref sp = new StudentSavePref(Student_Option.this);
                    sp.setStudList(po.getName(), po.getSID());

                }

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Student_Option.this, android.R.layout.select_dialog_singlechoice, names);
//                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Student_Option.this, android.R.layout.simple_list_item_1, names);
                //  listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove("status");
                        editor.remove("SID");
                        editor.remove("SID");
                        editor.commit();

                        dialog.dismiss();

                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String strName = arrayAdapter.getItem(which);

                        //we will get the SID from the shared pref when user click on the dialog item
                        String id = StudentSavePref.getStudList(strName);
                        System.out.println("ID is////////" + id);

                        //save SID Pref for session
                        StudentSavePref sp = new StudentSavePref(Student_Option.this);
                        sp.setSID("SID", id);

                        AlertDialog.Builder builderInner = new AlertDialog.Builder(Student_Option.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Student is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                // builderSingle.setView(row);
                AlertDialog dialog = builderSingle.create();
                dialog.show();
                builderSingle.setCancelable(false);

            }

            @Override
            public void onFailure(Call<ArrayList<DataOfRegisteredUser>> call1, Throwable t) {
                pd.dismiss();
                System.out.println("error :" + t.getMessage());
                Toast.makeText(Student_Option.this, "Error:server error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void showStudentDetail(View v) {
        Intent i = new Intent(Student_Option.this, UserDetail.class);
        startActivity(i);
    }

    public void showStudentIDCard(View v) {
        Intent i = new Intent(Student_Option.this, Student_IDcard_view.class);
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
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.clear();
                    editor.commit();

                    finish();
                    Intent intent = new Intent(Student_Option.this, HomeActivity.class);
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
