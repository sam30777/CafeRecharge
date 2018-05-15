package com.example.android.caferecharge;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemDetaills extends AppCompatActivity {
    ImageView imageView;
    EditText editText;
    TextView addReview;
    String userName;
    DatabaseReference databaseReference;
    ArrayList<ReviewData> reviewList;
    RecyclerView recyclerView;
    RelativeLayout addCartClicked;
    LinearLayout addCartDetails;
    Double priceOfItem;
    String item_name;
    TextView addedToCart;
    String image_url;
    String sectionName;
    Double totalPrice;
    RelativeLayout addFavorie;
    TextView reviewCount;
    ImageView favoriteImage;
    TextView favoriteText;
    int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detaills);
        Intent intent=getIntent();
        getUserName();

        favoriteImage=findViewById(R.id.favorites_image);
        favoriteText=findViewById(R.id.favorite_text);
        reviewCount=findViewById(R.id.review_count);
        addedToCart=findViewById(R.id.added_to_Cart_details);
        addCartClicked=findViewById(R.id.add_to_cart_click);
        addCartClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCartClicked.setVisibility(View.GONE);
                addCartDetails.setVisibility(View.VISIBLE);
                addCartDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        TextView dec=findViewById(R.id.decrease_quantity);
                        final TextView quant=findViewById(R.id.item_quantity);
                        TextView inc=findViewById(R.id.increase_quantity);
                        ImageView done=findViewById(R.id.quantity_done);
                        quantity= Integer.parseInt(quant.getText().toString());
                        dec.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                             quantity= Integer.parseInt(quant.getText().toString());
                             if(quantity>1){
                                 quantity=quantity-1;
                                 quant.setText(String.valueOf(quantity));
                             }
                            }
                        });
                        inc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                quantity= Integer.parseInt(quant.getText().toString());
                                quantity=quantity+1;
                                quant.setText(String.valueOf(quantity));
                            }
                        });
                        done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                 quantity= Integer.parseInt(quant.getText().toString());

                                   totalPrice=quantity*priceOfItem;
                                   FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                             DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.student_Cart)).child(getString(R.string.item_list));
                                FoodItem foodItem=new FoodItem(item_name, String.valueOf(priceOfItem), String.valueOf(quantity),image_url,false,sectionName);
                                databaseReference.child(item_name).setValue(foodItem);
                                addedToCart.setVisibility(View.VISIBLE);
                                addCartDetails.setVisibility(View.GONE);
                                updateTotal();
                                Toast.makeText(ItemDetaills.this,"Added to cart", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        addCartDetails=findViewById(R.id.add_to_cart_box);
        recyclerView=findViewById(R.id.user_reviews);
        String sectionName=intent.getStringExtra(getString(R.string.section_name));

        String itemName=intent.getStringExtra(getString(R.string.item_name));
        databaseReference= FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.sections_card))
                .child(sectionName)
                .child(getString(R.string.menu_item))
                .child(itemName);
        imageView=findViewById(R.id.item_detail_image);

        editText=findViewById(R.id.edit_text_review);
        addReview=findViewById(R.id.send_review);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(getString(R.string.reviews_list))){
                    readReview();
                    databaseReference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String review=editText.getText().toString();
                if(userName.equals("Santosh")){
                    ReviewData reviewData=new ReviewData(userName,review,R.drawable.me);
                    databaseReference.child(getString(R.string.reviews_list)).push().setValue(reviewData);


                }else{
                    ReviewData reviewData=new ReviewData(userName,review,R.drawable.drinks);
                    databaseReference.child(getString(R.string.reviews_list)).push().setValue(reviewData);


                }

                editText.setText("");

            }
        });

                CollapsingToolbarLayout collapsingToolbarLayout =findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle(itemName);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        readItemData();
    }
    private void checkFavorite(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.favorites_list));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(item_name)){
                    favoriteImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                    favoriteText.setText(getString(R.string.added));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void updateTotal(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference section_cart_ref=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.student_Cart));

        section_cart_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(getString(R.string.cart_total))){
                    DatabaseReference ref=section_cart_ref.getRef().child(getString(R.string.cart_total));
                    ref.setValue(String.valueOf(totalPrice));
                    section_cart_ref.removeEventListener(this);

                }else{
                    Utils.readTotal(totalPrice,ItemDetaills.this,getString(R.string.add_ttoal));
                    section_cart_ref.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void readItemData(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FoodItem foodItem=dataSnapshot.getValue(FoodItem.class);


                priceOfItem= Double.valueOf(foodItem.getPrice());
                sectionName=foodItem.getSection_name();
                image_url=foodItem.getImageUrl();
                Uri uri= Uri.parse(image_url);
                Picasso.with(ItemDetaills.this).load(uri).into(imageView);
                item_name=foodItem.getName();
                isPresentInCart();
                checkFavorite();
                setFavorites(foodItem);
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void readReview(){

        reviewList=new ArrayList<>();
       DatabaseReference revListRef= databaseReference.child(getString(R.string.reviews_list));

        revListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                reviewList.add(0,dataSnapshot.getValue(ReviewData.class));
                reviewCount.setText("("+ String.valueOf(reviewList.size())+")");
                ReviewAdapter reviewAdapter=new ReviewAdapter(ItemDetaills.this,reviewList);
                recyclerView.setLayoutManager(new LinearLayoutManager(ItemDetaills.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(reviewAdapter);
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
    private void setFavorites(final FoodItem foodItem){
        addFavorie=findViewById(R.id.add_to_favorite);
        addFavorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.favorites_list));
                databaseReference.child(foodItem.getName()).setValue(foodItem);
                Toast.makeText(ItemDetaills.this,getString(R.string.added), Toast.LENGTH_SHORT).show();
                favoriteImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                favoriteText.setText(getString(R.string.added));

            }

        });
    }
    private void isPresentInCart(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.student_Cart)).child(getString(R.string.item_list));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(item_name)){

                    addedToCart.setVisibility(View.VISIBLE);
                   addCartClicked.setVisibility(View.GONE);
                   databaseReference.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getUserName(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference nameRef=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(getString(R.string.name));
        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName=dataSnapshot.getValue(String.class);
                Toast.makeText(ItemDetaills.this,userName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
