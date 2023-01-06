package com.android_project.workassist;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MemberUpdateActivity extends AppCompatActivity {

    Button b_submit;
    EditText et_pincode;
    CheckBox cb_barber, cb_carpenter, cb_cleaner, cb_electrician,cb_gardener, cb_painter, cb_plumber;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_update);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
//        Log.d("tag", "Username: "+email);

        cb_barber=findViewById(R.id.cb_barber);
        cb_carpenter=findViewById(R.id.cb_carpenter);
        cb_cleaner=findViewById(R.id.cb_cleaner);
        cb_electrician=findViewById(R.id.cb_electrician);
        cb_gardener=findViewById(R.id.cb_gardener);
        cb_painter=findViewById(R.id.cb_painter);
        cb_plumber=findViewById(R.id.cb_plumber);
        et_pincode=findViewById(R.id.et_pincode);
        b_submit=findViewById(R.id.b_submit);

        db = openOrCreateDatabase("workassist", Context.MODE_PRIVATE, null);

        b_submit.setOnClickListener(v -> {
            String work="", pincode;
            if(cb_barber.isChecked()){ work+="Barber";}
            if(cb_carpenter.isChecked()){ if(!work.equals("")) work+=","; work+="Carpenter";}
            if(cb_cleaner.isChecked()){ if(!work.equals("")) work+=","; work+="Cleaner";}
            if(cb_electrician.isChecked()){ if(!work.equals("")) work+=","; work+="Electrician";}
            if(cb_gardener.isChecked()){ if(!work.equals("")) work+=","; work+="Gardener";}
            if(cb_painter.isChecked()){ if(!work.equals("")) work+=","; work+="Painter";}
            if(cb_plumber.isChecked()){ if(!work.equals("")) work+=","; work+="Plumber";}

            pincode = et_pincode.getText().toString();

            db.execSQL("update members set work='"+work+"' where email='"+email+"';");
            db.execSQL("update members set pincode='"+pincode+"' where email='"+email+"';");
//                Log.d("tag", "set");
            Toast.makeText(getApplicationContext(), "Details Updated", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), MemberHomeActivity.class);
            i.putExtra("email",email);
            startActivity(i);
        });
    }
}