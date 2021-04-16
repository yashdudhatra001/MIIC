package com.yash1213.miic.Activity;

import android.content.Intent;
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
import com.yash1213.miic.Model.Posts;
import com.yash1213.miic.R;
import com.yash1213.miic.Adapter.UpdatesAdapter;

import java.util.ArrayList;

public class CoUpdateActivity extends AppCompatActivity {

    private ImageView imageBack, addUpdates;
    private ProgressBar progressBar;
    private RecyclerView rvUpdates;
    private DatabaseReference dbRef;
    private UpdatesAdapter updatesAdapter;
    private ArrayList<Posts> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_update);

        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressBar = findViewById(R.id.progress_circular);
        rvUpdates = findViewById(R.id.rvUpdates);
        rvUpdates.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvUpdates.setLayoutManager(layoutManager);

        addUpdates = findViewById(R.id.btAddUpdates);
        addUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(CoUpdateActivity.this, AddUpdatesActivity.class);
                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(R.xml.activity_in, R.xml.activity_out);
                startActivity(ii);
            }
        });

        dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Posts posts = dataSnapshot.getValue(Posts.class);
                    postList.add(posts);
                }
                updatesAdapter = new UpdatesAdapter(CoUpdateActivity.this,postList);
                rvUpdates.setAdapter(updatesAdapter);
                updatesAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                rvUpdates.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CoUpdateActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}