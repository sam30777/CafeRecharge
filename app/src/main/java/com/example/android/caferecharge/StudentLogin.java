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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentLogin extends AppCompatActivity {
    private Button studentLogin;
    private EditText rollNumber;
    private EditText password;
    private EditText cardNumber;
    private EditText name;
    private FirebaseAuth firebaseAuth;
    private String roll;
    private String pass;
    private String card;
    private String na;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);


        studentLogin=findViewById(R.id.loginStu);
        rollNumber=findViewById(R.id.rollNumber);
        password=findViewById(R.id.password_student);
        cardNumber=findViewById(R.id.card_Number);
        name=findViewById(R.id.user_name);
        studentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StudentLogin.this,"Logged In", Toast.LENGTH_SHORT).show();
               roll=rollNumber.getText().toString();
                String completeRoll=roll+"@gmail.com";
                 pass=password.getText().toString();
                 na=name.getText().toString();
                 card=cardNumber.getText().toString();
                firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(completeRoll,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            checkUser();
                            Toast.makeText(StudentLogin.this,"Signed In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StudentLogin.this,SectionActivity.class));
                            finish();
                        }else{
                            Toast.makeText(StudentLogin.this,"Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void checkUser(){
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(firebaseUser.getUid())){
                    databaseReference.removeEventListener(this);
                }else{
                    writeData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void writeData(){

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        User user=new User(roll,na,card,"0");
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child(firebaseUser.getUid()).setValue(user);
    }
}
