package com.yash1213.miic.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yash1213.miic.Adapter.CampusAdapter;
import com.yash1213.miic.Model.Campus;
import com.yash1213.miic.R;

import java.util.ArrayList;

public class CampusActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView rvCampus;
    private ImageView imageBack, imageI;
    private DatabaseReference dbRef;
    private ArrayList<Campus> campuses = new ArrayList<>();
    private CampusAdapter campusAdapter;

    private static final String blogUrl = "https://forms.gle/rVsGdDFLzA9dwENm7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus);

        imageBack = findViewById(R.id.image_back);
        imageI = findViewById(R.id.image_i);
        imageI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(blogUrl)));
            }
        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressBar = findViewById(R.id.progress_circular);
        rvCampus = findViewById(R.id.campus_tips);
        rvCampus.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvCampus.setLayoutManager(layoutManager);

        dbRef = FirebaseDatabase.getInstance().getReference("campus");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Campus campus = dataSnapshot.getValue(Campus.class);
                    campuses.add(campus);
                }
                campusAdapter = new CampusAdapter(CampusActivity.this,campuses);
                rvCampus.setAdapter(campusAdapter);
                campusAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                rvCampus.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CampusActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}