package com.example.android.caferecharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class order_details extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    TextView orderNumber;
    TextView orderStatus;
    ArrayList<FoodItem> arrayList=new ArrayList<>();
    TextView orderTime;
    String uid;
    String order_num_string;
    TextView order_total;
        RecyclerView recyclerView;
        OrderHistoryDetailAdapter orderHistoryDetailAdapter;
        Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        button=findViewById(R.id.orderDone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        String s =intent.getStringExtra(getString(R.string.order_key));
        Toast.makeText(order_details.this,s, Toast.LENGTH_SHORT).show();
        if(intent.getStringExtra(getString(R.string.satffBool)).equals(getString(R.string.staffIntent))){
            databaseReference=FirebaseDatabase.getInstance().getReference().child(getString(R.string.current)).child(s);
            button.setVisibility(View.VISIBLE);
            setButton();
        }else{

            firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            databaseReference= FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.newOrder)).child(s);
        }

         orderNumber=findViewById(R.id.order_number_details);
        orderTime=findViewById(R.id.order_time_details);
        orderStatus=findViewById(R.id.order_status_details);
        recyclerView=findViewById(R.id.order_detail_recycler);

        order_total=findViewById(R.id.order_total_detail);
        getStatusData();
        getOrderDetails();
    }
    private void setButton(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dab=FirebaseDatabase.getInstance().getReference().child(uid).child(getString(R.string.notification));
                dab.child(getString(R.string.orderNum)).setValue(order_num_string);
                removeOrder();


            }
        });
    }
    private void removeOrder(){
        databaseReference.removeValue();
        startActivity(new Intent(order_details.this,OrderDates.class));
    }
    private void getOrderDetails(){
        DatabaseReference orderHOst=databaseReference.child(getString(R.string.order_history));
        orderHOst.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FoodItem foodItem=dataSnapshot.getValue(FoodItem.class);
                arrayList.add(0,foodItem);
                orderHistoryDetailAdapter=new OrderHistoryDetailAdapter(arrayList,order_details.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(order_details.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(orderHistoryDetailAdapter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getStatusData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HistoryData historyData=dataSnapshot.getValue(HistoryData.class);
                orderNumber.setText(historyData.getOrderNumber());
                orderTime.setText(historyData.getOrderTime());
                order_num_string=historyData.getOrderNumber();
                order_total.setText(historyData.getOrderTotal());
                uid=historyData.getUid();
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
