package com.example.android.caferecharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cafedroid.android.caferecharge.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button loginAsStudent;
    private Button loginAsStaff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                if(firebaseUser!=null){
                   String email= firebaseUser.getEmail();
                   if(email.startsWith("1")){
                       startActivity(new Intent(MainActivity.this,SectionActivity.class));
                   }else{
                       startActivity(new Intent(MainActivity.this,StaffMainActivity.class));
                   }

                    finish();
                }else{

                }
            }
        });
        loginAsStaff=findViewById(R.id.loginAsStaff);
        loginAsStudent=findViewById(R.id.loginAsStudent);

        loginAsStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,StudentLogin.class));
            }
        });
        loginAsStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,StaffLogin.class));
            }
        });


    }
}