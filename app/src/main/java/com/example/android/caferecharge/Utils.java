package com.example.android.caferecharge;

import android.content.Context;

import com.cafedroid.android.caferecharge.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Utils {
    static Context menuContext;

    public static void readTotal(final Double tot, final Context context, final String action) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(context.getString(R.string.student_Cart)).child(context.getString(R.string.cart_total));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double total = Double.valueOf(dataSnapshot.getValue(String.class));

                if (action.equals(context.getString(R.string.add_ttoal))) {
                    total = total + tot;
                    databaseReference.setValue(String.valueOf(total));
                    menuContext = context;
                } else if (action.equals(context.getString(R.string.minus))) {
                    total = total - tot;
                    databaseReference.setValue(String.valueOf(total));
                    menuContext = context;
                    CartActivity.readTotal(context);
                }

                MenuActivity.AddTextView(menuContext);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static void emptyCart(Context context){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference balRef = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(context.getString(R.string.student_Cart));
        balRef.removeValue();

    }
    public static void updateBalance(Double bal, Context context) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference balRef = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(context.getString(R.string.balance));
        balRef.setValue(String.valueOf(bal));
    }

    public static void rechargeBalance(String total, String bal, Context context) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid());
        Double newBal = Double.valueOf(total)
                + Double.valueOf(bal);
        databaseReference.child(context.getString(R.string.balance)).setValue(String.valueOf(newBal));

    }
}