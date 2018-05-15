package com.example.android.caferecharge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cafedroid.android.caferecharge.R;

import java.util.ArrayList;

public class OrderHistoryDetailAdapter extends RecyclerView.Adapter<OrderHistoryDetailAdapter.ViewHolder>{
    private ArrayList<FoodItem> foodItems;
    private Context context;

    OrderHistoryDetailAdapter(ArrayList<FoodItem> foodItems, Context context){
        this.foodItems=foodItems;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.order_detail_item,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      FoodItem foodItem=foodItems.get(position);
      holder.itemQuant.setText(foodItem.getQuantity());
      holder.itemPrice.setText(foodItem.getPrice());
      holder.itemName.setText(foodItem.getName());

    }

    @Override
    public int getItemCount() {
        if(foodItems!=null){
            return foodItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView itemName;
       TextView itemQuant;
       TextView itemPrice;
       public ViewHolder(View itemView) {
           super(itemView);
           itemName=itemView.findViewById(R.id.item_name_order_detail);
           itemPrice=itemView.findViewById(R.id.item_price_details);
           itemQuant=itemView.findViewById(R.id.item_quantity_details);
       }
   }
}
