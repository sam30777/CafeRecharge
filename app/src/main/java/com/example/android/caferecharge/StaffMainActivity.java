package com.example.android.caferecharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cafedroid.android.caferecharge.R;

public class StaffMainActivity extends AppCompatActivity {
    FloatingActionButton orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);
        orders=findViewById(R.id.currentOrders);
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StaffMainActivity.this,OrderDates.class);
                intent.putExtra(getString(R.string.satffBool),getString(R.string.staffIntent));
                startActivity(intent);
            }
        });
    }
}
