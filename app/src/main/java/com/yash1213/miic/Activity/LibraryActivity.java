package com.yash1213.miic.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yash1213.miic.R;

public class LibraryActivity extends AppCompatActivity {

    private LinearLayout lsem1, lsem2, lsem3, lsem4, lsem5, lsem6, lsem7, lsem8, comp;
    private ImageView imageBack;
    private DatabaseReference dbRef;
    private String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_library);


        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lsem1 = findViewById(R.id.sem1);
        lsem2 = findViewById(R.id.sem2);
        lsem3 = findViewById(R.id.sem3);
        lsem4 = findViewById(R.id.sem4);
        lsem5 = findViewById(R.id.sem5);
        lsem6 = findViewById(R.id.sem6);
        lsem7 = findViewById(R.id.sem7);
        lsem8 = findViewById(R.id.sem8);
        comp = findViewById(R.id.comp);

        dbRef = FirebaseDatabase.getInstance().getReference("library");


        lsem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("sem1");
            }
        });

        lsem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("sem2");
            }
        });
        lsem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("sem3");
            }
        });
        lsem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("sem4");
            }
        });
        lsem5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("sem5");
            }
        });
        lsem6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("sem6");
            }
        });
        lsem7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("sem7");
            }
        });
        lsem8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("sem8");
            }
        });
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("comp");
            }
        });

    }

    private void getData(final String key){
        value = "";
        dbRef = FirebaseDatabase.getInstance().getReference("library");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                value = snapshot.child(key).getValue().toString();
                if(value.equals("")){
                    Toast.makeText(LibraryActivity.this, "Unavailable", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(value)));
                    } catch (Exception e) {
                        Toast.makeText(LibraryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}