package com.android_project.workassist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MemberListActivity extends AppCompatActivity {

    ListView lv_members;
    String work, pincode;
    ArrayList<String> names = new ArrayList<>(), emails = new ArrayList<>(), mobiles = new ArrayList<>();
    SQLiteDatabase db;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        Bundle bundle = getIntent().getExtras();
        work = bundle.getString("work");
        pincode = bundle.getString("pincode");

//        Toast.makeText(getApplicationContext(), "work: "+work+" pincode:"+pincode, Toast.LENGTH_SHORT).show();

        db = openOrCreateDatabase("workassist", Context.MODE_PRIVATE, null);

        @SuppressLint("Recycle") Cursor resultSet = db.rawQuery("select * from members where availability='yes' and pincode='"+pincode+"';", null);
        resultSet.moveToFirst();
        while(!resultSet.isAfterLast()){
            String retrieved_work = resultSet.getString(resultSet.getColumnIndex("work"));
//            Log.d("tag", retrieved_work);
            String[] works = retrieved_work.split(",");
            for(String i : works){
//                Log.d("tag", i);
                if(i.equals(work)) {
//                    Log.d("tag", "added");
                    names.add(resultSet.getString(resultSet.getColumnIndex("name")));
                    emails.add(resultSet.getString(resultSet.getColumnIndex("email")));
                    mobiles.add(resultSet.getString(resultSet.getColumnIndex("mobile")));
                }
            }
            resultSet.moveToNext();
        }
        String[] res = new String[names.size()];
        for(int i=0;i<names.size();i++){
            res[i]= "Name: "+names.get(i)+" Mobile: "+mobiles.get(i);
        }

        lv_members = findViewById(R.id.lv_members);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, res);
        lv_members.setAdapter(adapter);

        lv_members.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
        });
    }
}