package com.example.android.caferecharge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cafedroid.android.caferecharge.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

      ArrayList<ReviewData> arrayList;
      Context context;
      public ReviewAdapter(Context context, ArrayList<ReviewData> arrayList){

          this.context=context;
          this.arrayList=arrayList;
      }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
        View view= LayoutInflater.from(context).inflate(R.layout.reviews_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
          ReviewData reviewData=arrayList.get(position);
          holder.review_user.setText(reviewData.getReview());
          holder.user_name.setText(reviewData.user_Name);
          holder.user_image.setImageResource(reviewData.getUser_image());
    }

    @Override
    public int getItemCount() {
          if(arrayList!=null){
              return arrayList.size();
          }
        return 0 ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
          public ImageView user_image;
          public TextView user_name;
          public TextView review_user;

        public ViewHolder(View itemView) {
            super(itemView);
            user_image=itemView.findViewById(R.id.user_image);
            user_name=itemView.findViewById(R.id.user_name);
            review_user=itemView.findViewById(R.id.user_review);
        }


    }

}
