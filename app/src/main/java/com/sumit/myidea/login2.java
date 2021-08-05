package com.sumit.myidea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login2 extends AppCompatActivity {
    EditText email,password;
    Button btn2;
    private FirebaseAuth mAuth;
    ProgressBar pgbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btn2=findViewById(R.id.btn2);
        pgbar=findViewById(R.id.pgbar);

        mAuth = FirebaseAuth.getInstance();
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=email.getText().toString().trim();
                String Password=password.getText().toString().trim();
                if(TextUtils.isEmpty(Email)){
                    email.setError("Correct Email is required");
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    password.setError("Correct Password is required");
                    return;
                }
                pgbar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login2.this, "Login Sucessfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else {
                            Toast.makeText(login2.this, "Login Again...."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pgbar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }
}