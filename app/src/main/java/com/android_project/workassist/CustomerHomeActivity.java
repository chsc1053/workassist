package com.android_project.workassist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerHomeActivity extends AppCompatActivity {

    Button b_submit;
    EditText et_pincode;
    RadioGroup rg_work;
    String work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");

        //work radiogroup listener
        rg_work = findViewById(R.id.rg_work);
        rg_work.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb_checked = findViewById(checkedId);
            work  = rb_checked.getText().toString();
        });

        et_pincode = findViewById(R.id.et_pincode);

        //submit button listener
        b_submit = findViewById(R.id.b_submit);
        b_submit.setOnClickListener(v -> {
            String pincode = et_pincode.getText().toString();

            Intent i = new Intent(getApplicationContext(), MemberListActivity.class);
            i.putExtra("work", work);
            i.putExtra("pincode", pincode);
            startActivity(i);
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Intent i = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}