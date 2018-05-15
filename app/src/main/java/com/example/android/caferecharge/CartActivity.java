package com.example.android.caferecharge;




import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CartActivity extends AppCompatActivity {
    private ArrayList<FoodItem> foodItems=new ArrayList<>();
    private static TextView textView;
    private static String order_Total;
    private String currentBalance;
    private CartAdapter cartAdapter;
    private Button order_botton;
    private String pushId;
    private DatabaseReference recent;
    private static Integer orderNumber=0;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getBalance();

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        textView=findViewById(R.id.cart_total);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid())
                .child(getString(R.string.student_Cart))
                .child(getString(R.string.item_list));
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FoodItem foodItem=dataSnapshot.getValue(FoodItem.class);
                foodItems.add(foodItem);
                RecyclerView recyclerView=findViewById(R.id.cart_recycler);
                cartAdapter=new CartAdapter(getApplicationContext(),foodItems);
                recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(cartAdapter);
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

        readTotal(this);

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
    public static void readTotal(final Context context){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference cart_tot=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(context.getString(R.string.student_Cart))
                .child(context.getString(R.string.cart_total));

        cart_tot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order_Total=dataSnapshot.getValue(String.class);
                textView.setText(order_Total);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void getBalance(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference  cardRef=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid());
        cardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                currentBalance=user.getBalance();
                addButtonClick();
                cardRef.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private static void updateTextTotal(){
        textView.setText(String.valueOf("0"));
    }
    private void addButtonClick(){
        order_botton=findViewById(R.id.order);
        order_botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(CartActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.order_dialog_box);
                TextView bal=dialog.findViewById(R.id.balance_card);
                TextView ord=dialog.findViewById(R.id.orer_total);
                TextView currBal=dialog.findViewById(R.id.new_balance);

                bal.setText(getText(R.string.rupees)+currentBalance);
                ord.setText(getText(R.string.rupees)+order_Total);
                final Double rest=Double.valueOf(currentBalance)-Double.valueOf(order_Total);
                currBal.setText(getText(R.string.rupees)+String.valueOf(rest));
                dialog.show();
                Button button=dialog.findViewById(R.id.confirm_order);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.updateBalance(rest,CartActivity.this);
                        Utils.emptyCart(CartActivity.this);
                        setOrder();


                        Toast.makeText(CartActivity.this,"Order Placed",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    private void setOrder(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        recent=FirebaseDatabase.getInstance().getReference();
        recent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(getString(R.string.recent_order))){
                    getLastOrderNumber(recent);
                    recent.removeEventListener(this);
                }else{
                    setData(1);
                    recent.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void setData(int ord){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.newOrder));
        DatabaseReference currentRef=FirebaseDatabase.getInstance().getReference().child(getString(R.string.current));
        Date date = new Date();
        final String dayOfTheWeek = (String) DateFormat.format("EEEE", date);
        final String day = (String) DateFormat.format("dd", date);
        final String monthString = (String) DateFormat.format("MMM", date);
        String da=dayOfTheWeek+","+monthString+" "+day;
        String  time = (String )DateFormat.format("HH:mm a",date);
        pushId=  databaseReference.push().getKey();
        HistoryData historyData=new HistoryData(da,String.valueOf(time),order_Total,pushId,String.valueOf(ord),firebaseUser.getUid());
        databaseReference.child(pushId).setValue(historyData);
        currentRef.child(pushId).setValue(historyData);
        recent.child(getString(R.string.recent_order)).setValue(historyData);

        setOrderDetails();

    }
    private void  getLastOrderNumber(DatabaseReference databaseReference){

        final DatabaseReference newRef=databaseReference.child(getString(R.string.recent_order));
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HistoryData hist=dataSnapshot.getValue(HistoryData.class);
                orderNumber=Integer.parseInt(hist.getOrderNumber());
                orderNumber=orderNumber+1;
                setData(orderNumber);
                Toast.makeText(CartActivity.this,"Order no:"+orderNumber,Toast.LENGTH_SHORT).show();
                newRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setOrderDetails(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userHist=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.newOrder)).child(pushId).child(getString(R.string.order_history));
        DatabaseReference forStaff=FirebaseDatabase.getInstance().getReference().child(getString(R.string.current)).child(pushId).child(getString(R.string.order_history));
        for (int i=0;i<foodItems.size();i++){
            FoodItem foodItem=foodItems.get(i);
            userHist.child(foodItem.getName()).setValue(foodItem);
            forStaff.child(foodItem.getName()).setValue(foodItem);
        }
        foodItems.removeAll(foodItems);
        cartAdapter.notifyDataSetChanged();
        updateTextTotal();
        startActivity(new Intent(CartActivity.this,OrderDates.class));
    }
}
