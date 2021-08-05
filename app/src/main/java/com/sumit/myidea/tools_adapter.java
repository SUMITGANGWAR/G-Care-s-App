package com.sumit.myidea;


import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;

public class tools_adapter extends RecyclerView.Adapter<tools_adapter.viewholder> {
    private ArrayList<tools> arrayList;
    public tools_adapter(ArrayList<tools> arrayList){
        this.arrayList=arrayList;
    }


    @Override
    public tools_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_view,parent,false);

        return new tools_adapter.viewholder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull tools_adapter.viewholder holder, int position) {
        tools txt= arrayList.get(position);
        String name=txt.getTools_name();
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
