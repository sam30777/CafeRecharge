package com.example.android.caferecharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cafedroid.android.caferecharge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StaffLogin extends AppCompatActivity {
    private EditText staffId;
    private EditText password;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);
        staffId=findViewById(R.id.staffId);
        password=findViewById(R.id.password_staff);

        login=findViewById(R.id.loginStf);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StaffLogin.this,"Clicked", Toast.LENGTH_SHORT).show();
                String id=staffId.getText().toString();
                String em=id+"@gmail.com";
                String pass=password.getText().toString();
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(StaffLogin.this,"Signed In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StaffLogin.this,StaffMainActivity.class));
                        }else{
                            Toast.makeText(StaffLogin.this,"Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}
