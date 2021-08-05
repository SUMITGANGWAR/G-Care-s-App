package com.sumit.myidea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.FragmentManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    NavigationView nav;
    Button logout;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    FirebaseAuth auth;
    TextView txtintoolbar;
    BottomNavigationView nav_bottom;
//    Fragment profilefragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get instance of firebase

        auth=FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        nav=findViewById(R.id.navigationview);
        nav_bottom=findViewById(R.id.bottom_navigation);
        txtintoolbar=findViewById(R.id.txtintoolbar);

        //taking database reference for username in toolbox
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val=snapshot.child(auth.getCurrentUser().getUid()).child("username").getValue(String.class);
                txtintoolbar.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                txtintoolbar.setText("Error.....goluu");

            }
        });

        //code for navigation drawer openning and closing
        drawerLayout=findViewById(R.id.main_drawer);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //code for selected items in navigation drawer
                switch (item.getItemId()){
                    case R.id.MyProfile:
                        Toast.makeText(MainActivity.this, "Yes is openning", Toast.LENGTH_SHORT).show();
                       Intent intent=new Intent(getApplicationContext(),profile.class);
                       startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logoutmenu:
                        Toast.makeText(MainActivity.this, "Yes is openning", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        startActivity(new Intent(getApplicationContext(),login.class));
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;



                }
                return true;
            }
        });
        nav_bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.plants:
                        loadFragment(new plantsFragment());
                        break;
                    case R.id.tools:
                        loadFragment(new toolsFragment());
                        break;
                    case R.id.disease:
                        loadFragment(new diseaseFragment());
                        break;
                }
                return true;
            }
        });
        if (savedInstanceState == null) {
            nav_bottom.setSelectedItemId(R.id.plants); // change to whichever id should be default
        }


   }

   //function for fragment change from one to other
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit(); // save the changes
    }

}