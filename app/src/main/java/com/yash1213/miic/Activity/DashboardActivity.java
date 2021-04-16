package com.yash1213.miic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.yash1213.miic.R;
import com.yash1213.miic.Model.Students;
import com.yash1213.miic.Adapter.UsersAdapter;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private ImageView imageBack;
    private TextView tvProfile;
    private RecyclerView recyclerViewTop;
    private ProgressBar progressBar;
    private RelativeLayout profileView;
    private TextView star, name;
    private DatabaseReference dbRef,dbPRef;
    private UsersAdapter usersAdapter;
    private ArrayList<Students> studentList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String sId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_dashboard);

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
        star = findViewById(R.id.idRating);
        name = findViewById(R.id.idName);
        profileView = findViewById(R.id.profile_view);

        progressBar = findViewById(R.id.progress_circular);
        recyclerViewTop = findViewById(R.id.listTop);
        recyclerViewTop.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewTop.setLayoutManager(layoutManager);

        dbRef = FirebaseDatabase.getInstance().getReference("students");
        dbRef.orderByChild("totalPoint").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                for(DataSnapshot dataSnapshot :snapshot.getChildren()) {
                    Students st = new Students();
                    st.setStudentId(dataSnapshot.child("studentId").getValue().toString());
                    st.setStudentName(dataSnapshot.child("studentName").getValue().toString());
                    st.setTotalPoint(Integer.parseInt(dataSnapshot.child("totalPoint").getValue().toString()));
                    studentList.add(st);
                }
                usersAdapter = new UsersAdapter(getApplicationContext(),studentList);
                recyclerViewTop.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerViewTop.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DashboardActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        dbPRef = FirebaseDatabase.getInstance().getReference("students");
        dbPRef.orderByChild("studentId").equalTo(sId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        name.setText(snapshot.child("studentName").getValue().toString());
                        tvProfile.setText(snapshot.child("studentName").getValue().toString().substring(0,1));
                        star.setText(snapshot.child("totalPoint").getValue().toString() + " Star");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardActivity.this, "Oops! something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(DashboardActivity.this,YourProfileActivity.class);
                ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ii);
            }
        });
    }
}