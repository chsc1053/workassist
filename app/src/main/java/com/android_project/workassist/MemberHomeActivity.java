package com.android_project.workassist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MemberHomeActivity extends AppCompatActivity {

    Button b_submit;
    RadioGroup rg_member_availability;
    String availability = "no", email;
    TextView tv_availability;

    SQLiteDatabase db;

    @SuppressLint({"Range", "SetTextI18n", "Recycle"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_home);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
//        Log.d("tag", "Username: "+email);

        tv_availability = findViewById(R.id.tv_availability);

        db = openOrCreateDatabase("workassist", Context.MODE_PRIVATE, null);
        Cursor resultSet1;
        resultSet1 = db.rawQuery("select * from members",null);
        resultSet1.moveToFirst();
        while(!resultSet1.isAfterLast()) {
            String retrieved_email1 = resultSet1.getString(resultSet1.getColumnIndex("email"));
            if(retrieved_email1.equals(email)) {
                if(resultSet1.getString(resultSet1.getColumnIndex("availability")).equals("yes")){
                    tv_availability.setText("Available");
                    tv_availability.setTextColor(Color.GREEN);
                }
                else{
                    tv_availability.setText("Not-Available");
                    tv_availability.setTextColor(Color.RED);
                }
                break;
            }
            resultSet1.moveToNext();
        }

        rg_member_availability = findViewById(R.id.rg_member_availability);
        rg_member_availability.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb_checked = findViewById(checkedId);
            if(rb_checked.getText().toString().equals("Available")) availability = "yes";
            else availability = "no";
        });

        b_submit = findViewById(R.id.b_submit);
        b_submit.setOnClickListener(v -> {
            Cursor resultSet = db.rawQuery("select * from members",null);
            resultSet.moveToFirst();
            while(!resultSet.isAfterLast()) {
                String retrieved_email = resultSet.getString(resultSet.getColumnIndex("email"));
                if(retrieved_email.equals(email)) {
                    db.execSQL("update members set availability='"+availability+"' where email='"+email+"';");
                    if(availability.equals("yes")){
                        tv_availability.setText("Available");
                        tv_availability.setTextColor(Color.GREEN);
                    }
                    else{
                        tv_availability.setText("Not-Available");
                        tv_availability.setTextColor(Color.RED);
                    }
//                    Log.d("tag", "set");
                    break;
                }
                resultSet.moveToNext();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_member, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Intent i = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(i);
            return true;
        }
        else if(item.getItemId() == R.id.edit){
            Intent i = new Intent(getApplicationContext(),MemberUpdateActivity.class);
            i.putExtra("email",email);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}