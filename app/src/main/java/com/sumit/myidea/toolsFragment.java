package com.sumit.myidea;

import android.app.Fragment;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class toolsFragment extends Fragment {
    View view;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_tools, container, false);
        ArrayList<tools> tool_detail=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Tools");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String update=dataSnapshot.child("tool_name").getValue(String.class);
                    Log.v("name chk",""+update);
                    String uri=dataSnapshot.child("uri").getValue(String.class);
                    Log.v("URI..VALUE>>>>>",""+uri);
                    tool_detail.add(new tools(update,uri));
                    tools_adapter toolsarrayadapter=new tools_adapter(tool_detail);
                    RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.list_view_in_tools);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(toolsarrayadapter);
//                    listView.setAdapter(plantsarrayadapter);
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//                        }
//                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}