package com.example.android.caferecharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cafedroid.android.caferecharge.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class favorite extends AppCompatActivity implements MenuAdapter.ListItemInterface {
    private RecyclerView recyclerView;
    private ArrayList<FoodItem> foodItems=new ArrayList<>();
    private MenuAdapter menuAdapter;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        recyclerView=findViewById(R.id.favorite_recycler_view);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.favorites_list));
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                foodItems.add(0,dataSnapshot.getValue(FoodItem.class));
                menuAdapter=new MenuAdapter(favorite.this,foodItems,favorite.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(favorite.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(menuAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClicked(int listitemindex) {
        FoodItem foodItem=foodItems.get(listitemindex);
        Intent intent=new Intent(favorite.this,ItemDetaills.class);
        intent.putExtra(getString(R.string.section_name),foodItem.getSection_name());
        intent.putExtra(getString(R.string.item_name),foodItem.getName());
    }
}
