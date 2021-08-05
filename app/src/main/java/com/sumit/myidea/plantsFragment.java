package com.sumit.myidea;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;


public class plantsFragment extends Fragment {
    View view;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_plants, container, false);
        ArrayList<plants> plant_detail=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getInstance().getReference();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Plants");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String update=dataSnapshot.child("plant_name").getValue(String.class);
                    Log.v("name chk",""+update);
                    String uri=dataSnapshot.child("uri").getValue(String.class);
                    Log.v("URI..VALUE>>>>>",""+uri);
                    plant_detail.add(new plants(update,uri));
                    plants_adapter plantsarrayadapter=new plants_adapter(plant_detail);
                    RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.list_view_in_plants);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(plantsarrayadapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        ArrayAdapter<plants> plantsarrayadapter=new ArrayAdapter<plants>(getActivity(),plant_detail);



        return view;
    }
//    public void getcurrentuserprofileimage(){
//        storageReference.child("images_profile/" + auth.getCurrentUser().getUid().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri.toString()).into();
//                pd.dismiss();
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                pd.dismiss();
//            }
//        });
}