package com.sumit.myidea;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

public class plants_adapter extends RecyclerView.Adapter<plants_adapter.viewholder> {

    private ArrayList<plants> arrayList;
    public plants_adapter(ArrayList<plants> arrayList){
        this.arrayList=arrayList;
    }


    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_view,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        plants txt= arrayList.get(position);
        String name=txt.getPlant_name();
        holder.nameTextView.setText(name);
        Picasso.get().load(txt.getUri()).into(holder.listimage);

//        holder.listimage.setImageURI(Picasso.get().load(txt.getUri()));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        ImageView listimage;
        TextView nameTextView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            nameTextView = (TextView)  itemView.findViewById(R.id.list_name);
            listimage=(ImageView) itemView.findViewById(R.id.list_image);
        }
    }
}
