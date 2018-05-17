package com.example.android.caferecharge;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cafedroid.android.caferecharge.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {
    ImageView imageview;
    int requestCode=1;
    StorageReference firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        imageview=findViewById(R.id.user_Image_user_profile);
        firebaseStorage=FirebaseStorage.getInstance().getReference();
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,requestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==requestCode&&resultCode==RESULT_OK){
            Toast.makeText(this,data.getData().toString(),Toast.LENGTH_SHORT).show();
            Picasso.with(UserProfile.this).load(data.getData()).into(imageview);
            StorageReference storageReference=firebaseStorage.child("UserImages").child(data.getData().getLastPathSegment());
            storageReference.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UserProfile.this,taskSnapshot.getDownloadUrl().toString(),Toast.LENGTH_SHORT).show();

                    Toast.makeText(UserProfile.this,"Uploaded",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(UserProfile.this,"Upload Failed",Toast.LENGTH_SHORT).show();
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
