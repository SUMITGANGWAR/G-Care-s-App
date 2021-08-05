package com.sumit.myidea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class profile extends AppCompatActivity {
    public static ImageView profileimage;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth auth;
    private int STORAGE_PERMISSION_CODE = 1;
    private int CAMERA_REQUEST_CODE=102;
    ProgressDialog pd;
    boolean imgchk=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView username=(TextView) findViewById(R.id.usernameinprofile);
        TextView email=(TextView) findViewById(R.id.emilinprofile);

        TextView mobile=(TextView) findViewById(R.id.mobileinprofile);


        profileimage=(ImageView) findViewById(R.id.imageinprofile);
        auth= FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.show();
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getInstance().getReference();
        getcurrentuserprofileimage();
        //Request camera permission
        //initiliziting database reference for user values like username,email,mobileno

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usernameDB=snapshot.child(auth.getCurrentUser().getUid()).child("username").getValue(String.class);
                String emilDB=snapshot.child(auth.getCurrentUser().getUid()).child("email").getValue(String.class);
                String mobileDB=snapshot.child(auth.getCurrentUser().getUid()).child("mobileno").getValue(String.class);
                username.setText(usernameDB);
                email.setText("Email : "+emilDB);
                mobile.setText("Mobile No. : "+mobileDB);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                username.setText("not");

            }
        });
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                    Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera,CAMERA_REQUEST_CODE);
                } else {
                    requestStoragePermission();
                }
            }
        });

    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(profile.this,
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
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
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

            // Get the Uri of data
//            filePath = data.getData();


                // Setting image on image view using Bitmap
                Bitmap bitmap=(Bitmap) data.getExtras().get("data");
                profileimage.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imgdata = baos.toByteArray();
                uploadImage(imgdata);

        }
        Log.v("Capturing data.....", "data captured successfully");
    }

    private void uploadImage(byte[] x)
    {
        if (x!= null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images_profile/"
                                    + auth.getCurrentUser().getUid().toString());

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
                                    Toast.makeText(profile.this,"Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    imgchk=true;
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(profile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        storageReference.child("images_profile/" + auth.getCurrentUser().getUid().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(profileimage);
                pd.dismiss();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            pd.dismiss();
            }
        });
    }
}

