package com.example.android.caferecharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cafedroid.android.caferecharge.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDates extends AppCompatActivity implements OrderHistoryAdapter.ListItemInterface {
    private DatabaseReference orderHIstRef;
    private FirebaseUser firebaseUser;
    private ArrayList<HistoryData> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderHistoryAdapter orderHistoryAdapter;
    Boolean isStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_dates);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.recycler_order_history);
        orderHIstRef = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.newOrder));
        Intent intent = getIntent();
        String getKey=intent.getStringExtra(getString(R.string.userVerifty));

        if (getKey==null) {
            isStaff=false;
            readHistory();
          /*  DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.notification));
            notificationRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Toast.makeText(OrderDates.this, dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
        } else {
            readForStaff();
        }
             getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    private void readForStaff() {
        isStaff=true;
        arrayList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.current));
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HistoryData historyData = dataSnapshot.getValue(HistoryData.class);
                arrayList.add(0, historyData);
                orderHistoryAdapter = new OrderHistoryAdapter(OrderDates.this, arrayList, OrderDates.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrderDates.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(orderHistoryAdapter);
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

    private void readHistory() {
        orderHIstRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HistoryData historyData = dataSnapshot.getValue(HistoryData.class);
                arrayList.add(0, historyData);
                orderHistoryAdapter = new OrderHistoryAdapter(OrderDates.this, arrayList, OrderDates.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrderDates.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(orderHistoryAdapter);

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
    public void onclick(int position) {
        HistoryData historyData = arrayList.get(position);
        Intent intent = new Intent(OrderDates.this, order_details.class);
        intent.putExtra(getString(R.string.order_key), historyData.getKey());
        if(isStaff){
            intent.putExtra(getString(R.string.satffBool), getString(R.string.staffIntent));
        }

        startActivity(intent);

    }
}
