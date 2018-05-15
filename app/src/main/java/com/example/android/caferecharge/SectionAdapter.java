package com.example.android.caferecharge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cafedroid.android.caferecharge.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {

    private ArrayList<Sections> arraySections;
   private Context context;
   private ListItemInterface listItemInterface;

    public SectionAdapter(Context context, ArrayList<Sections> arraySections, ListItemInterface listItemInterface) {
        super();
        this.context=context;
        this.arraySections=arraySections;
        this.listItemInterface=listItemInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.sections_item,parent,false);
        return new SectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.sectionText.setText(arraySections.get(position).getSection_text());
        String ur=arraySections.get(position).getSection_image();
        Uri uri= Uri.parse(ur);
        Picasso.with(context).load(uri).into(holder.sectionImage);

        holder.sectionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MenuActivity.class);
                intent.putExtra(context.getString(R.string.section_name),arraySections.get(position).getSection_text());
                Toast.makeText(context,"Cliced", Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(arraySections!=null){
            return arraySections.size();
        }
        return 0;
    }
    public interface ListItemInterface {
        void onItemClicked(int listitemindex);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView sectionImage;
        public TextView sectionText;
        public ViewHolder(View itemView) {
            super(itemView);

            sectionImage=itemView.findViewById(R.id.section_image);
            sectionText=itemView.findViewById(R.id.section_text);
        }

        @Override
        public void onClick(View view) {
            int index=getAdapterPosition();
            listItemInterface.onItemClicked(index);

        }
    }
}
