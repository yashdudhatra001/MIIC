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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yash1213.miic.Adapter.CoUserAdapter;
import com.yash1213.miic.R;
import com.yash1213.miic.Model.Students;

import java.util.ArrayList;

public class CoUsersActivity extends AppCompatActivity {

    private ImageView imageBack;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView wrongText;
    private ArrayList<Students> studentList = new ArrayList<>();
    private CoUserAdapter coUserAdapter;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_users);

        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        wrongText = findViewById(R.id.text_wrong);
        progressBar = findViewById(R.id.progress_circular);
        recyclerView = findViewById(R.id.user_recyclerview);
        recyclerView.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

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
                coUserAdapter = new CoUserAdapter(CoUsersActivity.this,studentList);
                recyclerView.setAdapter(coUserAdapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                coUserAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CoUsersActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                wrongText.setVisibility(View.VISIBLE);
            }
        });
    }
}