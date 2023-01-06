package com.android_project.workassist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ALL")
public class SignupActivity extends AppCompatActivity {

    Button b_signin, b_signup, b_c_signup, b_m_signup;
    EditText et_name, et_email, et_mobile, et_password, et_confirm_password, et_pincode;
    RadioGroup rg_gender;
    Spinner sp_work;
    String signup_type = "c", gender, work, works[]={"Barber","Carpenter","Cleaner","Electrician","Gardener","Painter","Plumber"};
    TextView tv_signup;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = openOrCreateDatabase("workassist", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists customers(name varchar,email varchar,mobile varchar,gender varchar,password varchar);");
        db.execSQL("create table if not exists members(name varchar,email varchar,mobile varchar,gender varchar,password varchar,pincode varchar,work varchar,availability varchar);");

        int id_bms=R.id.b_member_signup, id_bcs=R.id.b_customer_signup;
        b_m_signup = findViewById(id_bms);
        b_c_signup = findViewById(id_bcs);
        et_pincode = findViewById(R.id.et_pincode); et_pincode.setVisibility(View.GONE);
        sp_work = findViewById(R.id.sp_work); sp_work.setVisibility(View.GONE);
        tv_signup = findViewById(R.id.tv_signup);

        b_m_signup.setOnClickListener(view -> {
            changeSignup(b_m_signup, b_c_signup);
            et_pincode.setVisibility(View.VISIBLE);
            sp_work.setVisibility(View.VISIBLE);
            signup_type = "m";
        });

        b_c_signup.setOnClickListener(view -> {
            changeSignup(b_c_signup, b_m_signup);
            et_pincode.setVisibility(View.GONE);
            sp_work.setVisibility(View.GONE);
            signup_type = "c";
        });

        //switching to signin activity
        b_signin = findViewById(R.id.b_signin);
        b_signin.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),SigninActivity.class);
            startActivity(i);
        });

        //work spinner listener
        sp_work.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                work = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //setting dropdown resource for work spinnner
        ArrayAdapter aa_work = new ArrayAdapter(this,android.R.layout.simple_spinner_item,works);
        aa_work.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work.setAdapter(aa_work);

        //gender radiogroup listener
        rg_gender = findViewById(R.id.rg_gender);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb_checked = findViewById(checkedId);
                gender = rb_checked.getText().toString();
            }
        });

        //signup button listener
        b_signup = (Button) findViewById(R.id.b_signup);
        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_name = findViewById(R.id.et_name);
                et_email = findViewById(R.id.et_email);
                et_mobile = findViewById(R.id.et_mobile);
                et_password = findViewById(R.id.et_password);
                et_confirm_password = findViewById(R.id.et_confirm_password);
                String name = et_name.getText().toString(),
                        email = et_email.getText().toString(),
                        mobile = et_mobile.getText().toString(),
                        password = et_password.getText().toString(),
                        confirm_password = et_confirm_password.getText().toString(),
                        pincode = et_pincode.getText().toString();
                Log.d("tag", "Name: "+name+" e-mail: "+email+" Mobile: "+mobile+" Gender: "+gender+" Password: "+password+" Confirm Password: "+confirm_password+" Pincode: "+pincode+" Work: "+work);

                if(!(password.equals(confirm_password))){
                    Toast.makeText(getApplicationContext(), "Passwords does not match", Toast.LENGTH_LONG).show();
                    Log.d("tag", "passwords unequal");
                    return;
                }
                Log.d("tag", "passwords equal");
                if(signup_type.equals("c")){
                    db.execSQL("insert into customers values('"+name+"','"+email+"','"+mobile+"','"+gender+"','"+password+"');");
                }
                else{
                    db.execSQL("insert into members values('"+name+"','"+email+"','"+mobile+"','"+gender+"','"+password+"','"+pincode+"','"+work+"','no');");
                }
                Toast.makeText(getApplicationContext(),"Signup Successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),SigninActivity.class);
                startActivity(i);
            }
        });
    }

    // switch between customer & member signup
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    public void changeSignup(Button a, Button b){
        if(a==findViewById(R.id.b_customer_signup))
            tv_signup.setText("Customer's SignUp");
        else
            tv_signup.setText("Member's SignUp");

        a.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.teal_200));
        a.setTextColor(getResources().getColor(R.color.black));
        b.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.teal_700));
        b.setTextColor(getResources().getColor(R.color.white));
    }
}