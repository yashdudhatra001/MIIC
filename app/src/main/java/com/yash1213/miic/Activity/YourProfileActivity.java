package com.yash1213.miic.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yash1213.miic.Adapter.CoCommentsAdapter;
import com.yash1213.miic.Model.StudentDetails;
import com.yash1213.miic.R;

import java.util.ArrayList;

public class YourProfileActivity extends AppCompatActivity {

    private ImageView imageBack;
    private TextView tvProfile;
    private TextView tvName, tvPoints, tvDescription;
    private RecyclerView commentsView;
    private ProgressBar progressBar;
    private String sId;
    private DatabaseReference dbRef, dbsRef;
    private FirebaseAuth mAuth;
    private CoCommentsAdapter commentsAdapter;
    private ArrayList<StudentDetails> sdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_your_profile);

        mAuth = FirebaseAuth.getInstance();
        sId = mAuth.getCurrentUser().getUid();

        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvProfile = findViewById(R.id.textFirstChar);
        tvName = findViewById(R.id.name);
        tvPoints = findViewById(R.id.stars);
        tvDescription = findViewById(R.id.description2);

        progressBar = findViewById(R.id.progress_circular);
        commentsView = findViewById(R.id.listComments);
        commentsView.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        commentsView.setLayoutManager(layoutManager);

        dbsRef = FirebaseDatabase.getInstance().getReference("students");
        dbsRef.orderByChild("studentId").equalTo(sId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        tvName.setText(snapshot.child("studentName").getValue().toString());
                        tvProfile.setText(snapshot.child("studentName").getValue().toString().substring(0,1));
                        tvPoints.setText(snapshot.child("totalPoint").getValue().toString()+ " star");
                        tvDescription.setText(snapshot.child("studentEmail").getValue().toString()+"\n"
                                +snapshot.child("studentPh").getValue().toString());
                    }
                    dbRef = FirebaseDatabase.getInstance().getReference("students");
                    dbRef.child(sId).child("profiles").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds:snapshot.getChildren()){
                                StudentDetails sd = ds.getValue(StudentDetails.class);
                                sdList.add(sd);
                            }
                            commentsAdapter = new CoCommentsAdapter(getApplicationContext(),sdList);
                            commentsView.setAdapter(commentsAdapter);
                            commentsAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            commentsView.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(YourProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(YourProfileActivity.this, "Guest", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(YourProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
}