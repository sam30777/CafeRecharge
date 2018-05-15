package com.example.android.caferecharge;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cafedroid.android.caferecharge.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
     private Context context;
     private ArrayList<FoodItem> foodItemArrayList;
     private Integer item_to_cart;

    public CartAdapter(Context context, ArrayList<FoodItem> foodItems) {
        super();
        this.context=context;
        this.foodItemArrayList=foodItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FoodItem currentItem=foodItemArrayList.get(position);
        holder.nameView.setText(currentItem.getName());
        Uri uri= Uri.parse(currentItem.getImageUrl());
        Picasso.with(context).load(uri).into(holder.imageView);
        holder.priceView.setText(currentItem.getPrice());
        holder.quantity_adder.setText(currentItem.getQuantity());
        holder.sectionName.setText(currentItem.getSection_name());
        holder.removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(context.getString(R.string.student_Cart)).child(context.getString(R.string.item_list)).child(currentItem.getName());
                databaseReference.removeValue();


                Double curentQuant= Double.valueOf(holder.quantity_adder.getText().toString());
                Double price= Double.valueOf(holder.priceView.getText().toString());

                Double currentTotal=curentQuant*price;

                Utils.readTotal(currentTotal,context,context.getString(R.string.minus));
                foodItemArrayList.remove(currentItem);
                notifyDataSetChanged();

            }
        });




    }

    @Override
    public int getItemCount() {
        if(foodItemArrayList!=null){
            return foodItemArrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameView;
        ImageView imageView;
        TextView priceView;
        TextView quantity_adder;
        TextView mult;
        TextView removeFromCart;
        TextView sectionName;
        public ViewHolder(View itemView) {
            super(itemView);
            removeFromCart=itemView.findViewById(R.id.remove_from_cart);
            nameView=itemView.findViewById(R.id.item_text);
            imageView=itemView.findViewById(R.id.item_image);
            priceView=itemView.findViewById(R.id.price_text);
            quantity_adder=itemView.findViewById(R.id.item_quantity);
            sectionName=itemView.findViewById(R.id.section_name);
            mult=itemView.findViewById(R.id.multiply);
        }
    }
}
