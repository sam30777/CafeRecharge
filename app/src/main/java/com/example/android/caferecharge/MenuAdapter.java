package com.example.android.caferecharge;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cafedroid.android.caferecharge.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
     private Context context;
     private ArrayList<FoodItem> arrayListFood;
     private Integer item_to_cart;
     private ListItemInterface listItemInterface;


    public MenuAdapter(Context context, ArrayList<FoodItem> arrayListFood, ListItemInterface listItemInterface) {
        super();
        this.context=context;
        this.arrayListFood=arrayListFood;
        this.listItemInterface=listItemInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View view= LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final FoodItem currentItem=arrayListFood.get(position);
        holder.nameView.setText(currentItem.getName());
        Uri uri= Uri.parse(currentItem.getImageUrl());
        Picasso.with(context).load(uri).into(holder.imageView);
        holder.itemLeft.setText(currentItem.getQuantity());
        holder.priceView.setText(currentItem.getPrice());
        item_to_cart= Integer.parseInt(holder.quantity_adder.getText().toString()) ;
        holder.quantity_adder.setText(String.valueOf(1));
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(context.getString(R.string.student_Cart)).child(context.getString(R.string.item_list));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentItem.getName())){
                    holder.added_to_cart.setVisibility(View.VISIBLE);
                    holder.cart.setVisibility(View.INVISIBLE);
                    databaseReference.removeEventListener(this);

                }else{

                    holder.added_to_cart.setVisibility(View.GONE);
                    holder.cart.setVisibility(View.VISIBLE);
                    databaseReference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cart.setVisibility(View.GONE);
               holder.increase.setVisibility(View.VISIBLE);
               holder.decrease.setVisibility(View.VISIBLE);
                holder.quantity_adder.setVisibility(View.VISIBLE);
                holder.quantity_done.setVisibility(View.VISIBLE);
                holder.mult.setVisibility(View.VISIBLE);
            }
        });
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                item_to_cart= Integer.parseInt(holder.quantity_adder.getText().toString()) ;
                item_to_cart=item_to_cart+1;
                holder.quantity_adder.setText(String.valueOf(item_to_cart));

            }
        });
       holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_to_cart= Integer.parseInt(holder.quantity_adder.getText().toString()) ;
                if(item_to_cart>1){

                    item_to_cart=item_to_cart-1;
                    holder.quantity_adder.setText(String.valueOf(item_to_cart));
                }

            }
        });
       holder.quantity_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_to_cart= Integer.parseInt(holder.quantity_adder.getText().toString());
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(context.getString(R.string.student_Cart)).child(context.getString(R.string.item_list));

                Double price= Double.valueOf(currentItem.getPrice());

                final Double totalPrice=price*item_to_cart;
                FoodItem foodItem=new FoodItem(currentItem.getName(),currentItem.getPrice(), String.valueOf(item_to_cart),currentItem.getImageUrl(),true,currentItem.getSection_name());
                databaseReference.child(foodItem.getName()).setValue(foodItem);
                holder.added_to_cart.setVisibility(View.VISIBLE);
                holder.increase.setVisibility(View.GONE);
                holder.decrease.setVisibility(View.GONE);
                holder.quantity_adder.setVisibility(View.GONE);
                holder.quantity_done.setVisibility(View.GONE);
                holder.mult.setVisibility(View.GONE);


                Toast.makeText(context,"Added to Cart", Toast.LENGTH_SHORT).show();

                final DatabaseReference section_cart_ref=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(context.getString(R.string.student_Cart));

                section_cart_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.hasChild(context.getString(R.string.cart_total))){
                            DatabaseReference ref=section_cart_ref.getRef().child(context.getString(R.string.cart_total));
                            ref.setValue(String.valueOf(totalPrice));
                            MenuActivity.AddTextView(context);
                        }else{
                            Utils.readTotal(totalPrice,context,context.getString(R.string.add_ttoal));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DatabaseReference sectionItemRef = FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.sections_card)).child(currentItem.getSection_name()).child(context.getString(R.string.menu_item));
                currentItem.setAdded_to_cart(true);
                sectionItemRef.child(currentItem.getName()).setValue(currentItem);


                }
        });
    }


    @Override
    public int getItemCount() {
        if(arrayListFood!=null){
          return   arrayListFood.size();
        }
        return 0;
    }

    public interface ListItemInterface {
        void onItemClicked(int listitemindex);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameView;
        ImageView imageView;
        TextView itemLeft;
        TextView priceView;
        ImageView cart;
        TextView increase;
        TextView decrease;
        TextView quantity_adder;
        ImageView quantity_done;
        TextView mult;
        TextView added_to_cart;
        public ViewHolder(View itemView) {
            super(itemView);
             nameView=itemView.findViewById(R.id.item_text);
             imageView=itemView.findViewById(R.id.item_image);
             itemLeft=itemView.findViewById(R.id.item_left);
           priceView=itemView.findViewById(R.id.price_text);
              cart=itemView.findViewById(R.id.add_to_cart);
             increase=itemView.findViewById(R.id.increase_quantity);
            decrease=itemView.findViewById(R.id.decrease_quantity);
           quantity_adder=itemView.findViewById(R.id.item_quantity);
           quantity_done=itemView.findViewById(R.id.quantity_done);
           mult=itemView.findViewById(R.id.multiply);
           added_to_cart=itemView.findViewById(R.id.added_to_Cart);
           itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int index=getAdapterPosition();
            listItemInterface.onItemClicked(index);

        }
    }

}
