package com.example.android.caferecharge;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

public class SectionActivity extends AppCompatActivity implements SectionAdapter.ListItemInterface{
   private ArrayList<Sections> arrayListSections;
   private RecyclerView recyclerView;
   ActionBarDrawerToggle actionBarDrawerToggle;
   private String cardNumber;
   private String account_bal;
   private DatabaseReference cardRef;
   private View headerView;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        final DatabaseReference uidRef=FirebaseDatabase.getInstance().getReference();
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                if(dataSnapshot.hasChild(firebaseUser.getUid())){
                    getDetails();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
       actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        headerView=navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.sign_out) {
                    FirebaseAuth.getInstance().signOut();
                   Toast.makeText(SectionActivity.this,getString(R.string.signed_out), Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(SectionActivity.this,MainActivity.class));
                    return false;
                }else if(item.getItemId()==R.id.cart){
                    startActivity(new Intent(SectionActivity.this,CartActivity.class));
                }else if(item.getItemId()==R.id.recharge_button){
                    launchRechargeDialog();
                }else if(item.getItemId()==R.id.order_history_menu){
                    startActivity(new Intent(SectionActivity.this,OrderDates.class));
                }else if(item.getItemId()==R.id.favorites){
                    startActivity(new Intent(SectionActivity.this,favorite.class));
                }

                return false;
            }
        });

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(getString(R.string.sections_card))){
                    Toast.makeText(SectionActivity.this,"has sections", Toast.LENGTH_SHORT).show();
                    readSections();
                    databaseReference.removeEventListener(this);
                }else{
                    Toast.makeText(SectionActivity.this,"does'nt have sections", Toast.LENGTH_SHORT).show();
                    addSections();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void updateHeader(){
        TextView bal=headerView.findViewById(R.id.user_Card_balance);
        bal.setText(account_bal);
    }
    private void launchRechargeDialog(){
        final Dialog dialog=new Dialog(SectionActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rechargedialog);
        TextView cardNumText=dialog.findViewById(R.id.last_four);
        TextView cardBalance=dialog.findViewById(R.id.balance_card);
        final EditText rechargeEdit=dialog.findViewById(R.id.recharge_amount);
        Button rech=dialog.findViewById(R.id.rechButton);
        String lastDigits=cardNumber.substring(12);
        cardBalance.setText(account_bal);
        cardNumText.setText(lastDigits);
        rech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String total=rechargeEdit.getText().toString();
                Utils.rechargeBalance(total,account_bal,SectionActivity.this);
                dialog.dismiss();


            }
        });

        dialog.show();
   }
    private void addSections(){
        arrayListSections=new ArrayList<>();
        arrayListSections.add(new Sections("https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/nonveg.png?alt=media&token=695e4dfe-7af9-4328-8917-b69ea4564ced",getString(R.string.non_veg)));
        arrayListSections.add(new Sections("https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/drinks.png?alt=media&token=99e7e63d-b6d5-41cb-935a-74e05b3898c7",getString(R.string.drinks)));
        arrayListSections.add(new Sections("https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Veg%2Fdaalmakhani.png?alt=media&token=22aa6f71-ed79-4f56-824a-25d26ccc217e",getString(R.string.veg_food)));
        arrayListSections.add(new Sections("https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/chillipaneer.png?alt=media&token=1ad1d04d-adb6-43f9-85cd-5d1173a9b55d",getString(R.string.new_arrive)));
        arrayListSections.add(new Sections("https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/Breads%2Fplainnaan.png?alt=media&token=1c5fba46-e3e3-4ff4-9e19-e5c30135afde",getString(R.string.breads)));
        arrayListSections.add(new Sections("https://firebasestorage.googleapis.com/v0/b/cafecharge1.appspot.com/o/combos.png?alt=media&token=214ed88d-313e-46cc-83ab-713c436c1abc",getString(R.string.combos)));
       DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(getString(R.string.sections_card));
        for(int i=0;i<arrayListSections.size();i++){
            Sections sections=arrayListSections.get(i);
            databaseReference.child(sections.getSection_text()).setValue(sections);

        }
    }

   private void readSections(){
        arrayListSections=new ArrayList<>();
       final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(getString(R.string.sections_card));
       databaseReference.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               arrayListSections.add(dataSnapshot.getValue(Sections.class));
               RecyclerView  recyclerView=findViewById(R.id.recycler_view_section);
               SectionAdapter sectionAdapter=new SectionAdapter(SectionActivity.this,arrayListSections,SectionActivity.this);
               recyclerView.setLayoutManager(new GridLayoutManager(SectionActivity.this,2));
               recyclerView.setHasFixedSize(true);
               recyclerView.setAdapter(sectionAdapter);
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
        Intent intent=new Intent(SectionActivity.this,MenuActivity.class);
        intent.putExtra(getString(R.string.section_name),arrayListSections.get(listitemindex).getSection_text());
        startActivity(intent);
        }

        private void getDetails(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
          cardRef=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid());
          cardRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  User user=dataSnapshot.getValue(User.class);
                  cardNumber=user.getCard();
                  account_bal=user.getBalance();
                  updateHeader();
                 // Toast.makeText(SectionActivity.this,cardNumber,Toast.LENGTH_SHORT).show();

              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
        }

}
