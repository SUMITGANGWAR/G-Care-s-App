package com.sumit.myidea;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;


import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class diseaseFragment extends Fragment {
    View view;
    StateProgressBar stateProgressBar;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth auth;
    Bitmap bitmap;
    RelativeLayout disease_processed_data,text_process_for_user;
    public static ImageView disease_image;
    public static Button disease_process;
    private int STORAGE_PERMISSION_CODE = 1;
    private int CAMERA_REQUEST_CODE=102;
    ProgressDialog pd;
    boolean imgchk=false;
    String[] descriptiondata={"Image \n Uploaded","Team \n Servey","Servey \n Completed","Order \n Medicine"};

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_disease, container, false);
        disease_process=view.findViewById(R.id.disease_process_button);
        disease_image=view.findViewById(R.id.disease_image);
        TextView disease_name=view.findViewById(R.id.disease_txt);
        TextView medicine_name=view.findViewById(R.id.medicine_txt);
        TextView treatement_name=view.findViewById(R.id.treatement_txt);
        stateProgressBar=view.findViewById(R.id.your_state_progress_bar_id);
        disease_processed_data=view.findViewById(R.id.disease_processed_data);
        text_process_for_user=view.findViewById(R.id.process_for_user);
        auth= FirebaseAuth.getInstance();
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait");
        pd.show();
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseDatabase =FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference("Disease");
        Uri uri = Uri.parse(String.valueOf(disease_image));
        getcurrentuserprofileimage();



        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String disease_nameDB=snapshot.child(auth.getCurrentUser().getUid()).child("Disease_name").getValue(String.class);
                String medicine_nameDB=snapshot.child(auth.getCurrentUser().getUid()).child("Medicine_name").getValue(String.class);
                String treatement_nameDB=snapshot.child(auth.getCurrentUser().getUid()).child("Treatement_Process").getValue(String.class);
                if(imgchk==true && disease_nameDB.equals("null1")){
                    disease_processed_data.setVisibility(View.GONE);
                    text_process_for_user.setVisibility(View.VISIBLE);
                    disease_process.setVisibility(View.INVISIBLE);
                }else{

                    disease_name.setText("Disease_name : "+disease_nameDB);
                    medicine_name.setText("Medicine_name : "+medicine_nameDB);
                    treatement_name.setText("Treatement_Process : "+treatement_nameDB);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    stateProgressBar.setStateDescriptionData(descriptiondata);

                    text_process_for_user.setVisibility(View.GONE);
                    disease_processed_data.setVisibility(View.VISIBLE);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                treatement_name.setText("not");

            }
        });
//        InputStream stream = getContentResolver().openInputStream(uri);


//        reference.child(auth.getCurrentUser().getUid().toString()).setValue("null");
//        reference.child("Disease").toString();
        disease_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                    Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera,CAMERA_REQUEST_CODE);
                } else {
                    requestStoragePermission();
                }
            }
        });

        return view;
    }

    //fuctions.......>>>>>>>>>>>>>>>>>>


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) { new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //this function provide camera acess to the users

    private void opencamera(){
        Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,CAMERA_REQUEST_CODE);
    }

    //this override function provides result after taking image

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE ) {
            // Setting image on image view using Bitmap
             bitmap=(Bitmap) data.getExtras().get("data");
            disease_image.setImageBitmap(bitmap);
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
            byte[] imgdata = baos1.toByteArray();
            uploadImage(imgdata);

        }
        Log.v("Capturing data.....", "data captured successfully");
    }

    private void uploadImage(byte[] x)
    {
        if (x!= null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images_disease/" + auth.getCurrentUser().getUid().toString());

            // adding listeners on upload
            // or failure of image
            ref.putBytes(x)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(),"Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    text_process_for_user.setVisibility(View.INVISIBLE);
                                    reference.child(auth.getCurrentUser().getUid()).child("Disease_name").setValue("null1").toString();
                                    reference.child(auth.getCurrentUser().getUid()).child("Medicine_name").setValue("null1").toString();
                                    reference.child(auth.getCurrentUser().getUid()).child("Treatement_Process").setValue("null1").toString();
                                    disease_process.setVisibility(View.INVISIBLE);
                                    disease_processed_data.setVisibility(View.INVISIBLE);
                                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                }
                            });
        }
    }
    public void getcurrentuserprofileimage(){
        storageReference.child("images_disease/" + auth.getCurrentUser().getUid().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(disease_image);
                text_process_for_user.setVisibility(View.INVISIBLE);
                imgchk=true;
                pd.dismiss();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                text_process_for_user.setVisibility(View.VISIBLE);
                pd.dismiss();
            }
        });
    }
}