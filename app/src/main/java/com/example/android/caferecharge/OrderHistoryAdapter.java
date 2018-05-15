package com.example.android.caferecharge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cafedroid.android.caferecharge.R;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>{
    private Context context;
    private ArrayList<HistoryData> arrayList;
    private ListItemInterface listItemInterface;
    public OrderHistoryAdapter(Context context, ArrayList<HistoryData> arrayList, ListItemInterface listItemInterface){
        this.context=context;
        this.arrayList=arrayList;
        this.listItemInterface=listItemInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.order_history_item,parent,false);
        return  new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryData historyData=arrayList.get(position);
        holder.order_time.setText(historyData.getOrderTime());
        holder.order_number.setText(historyData.getOrderNumber());
        holder.order_Total.setText(context.getString(R.string.rupees)+historyData.getOrderTotal());
        holder.orderDate.setText(historyData.getOrderDate());
    }

    @Override
    public int getItemCount() {
        if(arrayList!=null)return arrayList.size();
        return 0;
    }
    public interface ListItemInterface{
        void onclick(int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       TextView orderDate;
       TextView order_state;
       TextView order_Total;
       TextView order_time;
       TextView order_number;
        public ViewHolder(View itemView) {
            super(itemView);
            orderDate=itemView.findViewById(R.id.order_date);
            order_Total=itemView.findViewById(R.id.order_total);
            order_state=itemView.findViewById(R.id.order_status);
            order_time=itemView.findViewById(R.id.order_time);
            order_number=itemView.findViewById(R.id.order_number_text);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int pos=getAdapterPosition();
            listItemInterface.onclick(pos);
        }
    }
}
