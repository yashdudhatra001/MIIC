package com.yash1213.miic.Activity;

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
import com.yash1213.miic.Model.COs;
import com.yash1213.miic.Adapter.CurrentCosAdapter;
import com.yash1213.miic.R;

import java.util.ArrayList;

public class CurrentCosActivity extends AppCompatActivity {

    private ImageView imageBack;
    private ProgressBar progressBar;
    private RecyclerView rvTeam;
    private DatabaseReference dbRef;
    private CurrentCosAdapter adapter;
    private ArrayList<COs> coList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_current_cos);

        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressBar = findViewById(R.id.progress_circular);
        rvTeam = findViewById(R.id.rvCurrentTeam);
        rvTeam.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvTeam.setLayoutManager(layoutManager);

        dbRef = FirebaseDatabase.getInstance().getReference("team");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    COs c = dataSnapshot.getValue(COs.class);
                    coList.add(c);
                }
                adapter = new CurrentCosAdapter(CurrentCosActivity.this,coList);
                rvTeam.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                rvTeam.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CurrentCosActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}