package com.android_project.workassist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ALL")
public class SigninActivity extends AppCompatActivity {

    Button b_signin, b_signup, b_c_signin, b_m_signin;
    EditText et_username, et_password;
    String signin_type = "c";
    TextView tv_signin;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        db = openOrCreateDatabase("workassist", Context.MODE_PRIVATE, null);

        tv_signin = findViewById(R.id.tv_signin);
        int id_bms=R.id.b_member_signin, id_bcs=R.id.b_customer_signin;

        b_m_signin = findViewById(id_bms);
        b_c_signin = findViewById(id_bcs);

        b_m_signin.setOnClickListener(view -> {
            changeSignin(b_m_signin, b_c_signin);
            signin_type = "m";
        });

        b_c_signin.setOnClickListener(view -> {
            changeSignin(b_c_signin, b_m_signin);
            signin_type = "c";
        });

        b_signup = findViewById(R.id.b_signup);
        b_signup.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),SignupActivity.class);
            startActivity(i);
        });

        //signin button listener
        b_signin = findViewById(R.id.b_signin);
        b_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_username = findViewById(R.id.et_username);
                et_password = findViewById(R.id.et_password);
                String username = et_username.getText().toString(),
                        password = et_password.getText().toString(),
                        retrieved_username, retrieved_password;
                Log.d("tag", "Username: "+username+" Password: "+password);

                Cursor resultSet;
                if(signin_type.equals("c"))
                    resultSet = db.rawQuery("select * from customers",null);
                else
                    resultSet = db.rawQuery("select * from members",null);
                resultSet.moveToFirst();
                while(resultSet.isAfterLast() == false) {
                    retrieved_username = resultSet.getString(resultSet.getColumnIndex("email"));
                    retrieved_password = resultSet.getString(resultSet.getColumnIndex("password"));
                    if(retrieved_username.equals(username) && retrieved_password.equals(password)) {
                        Intent i;
                        if(signin_type.equals("c"))
                            i = new Intent(getApplicationContext(), CustomerHomeActivity.class);
                        else
                            i = new Intent(getApplicationContext(), MemberHomeActivity.class);
                        i.putExtra("email", username);
                        startActivity(i);
                        return;
                    }
                    resultSet.moveToNext();
                }
                Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    public void changeSignin(Button a, Button b){
        if(a==findViewById(R.id.b_customer_signin))
            tv_signin.setText("Customer's SignIn");
        else
            tv_signin.setText("Member's SignIn");

        a.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.teal_200));
        a.setTextColor(getResources().getColor(R.color.black));
        b.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.teal_700));
        b.setTextColor(getResources().getColor(R.color.white));
    }
}