package com.yash1213.miic.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private ImageView imageBack, imageDel;
    private TextView tvProfile;
    private TextView tvName, tvPoints, tvDescription;
    private RecyclerView commentsView;
    private ProgressBar progressBar;
    private String sId;
    private DatabaseReference dbRef, dbsRef , dbRef1,dbRef2;
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
        imageDel = findViewById(R.id.image_del);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imageDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleAlert(sId);
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
                    commentList();
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

    private void commentList(){

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
    }

    public void simpleAlert(final String sId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(YourProfileActivity.this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to delete your account ?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbRef1 = FirebaseDatabase.getInstance().getReference("students");
                        dbRef1.orderByChild("studentId").equalTo(sId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    dbRef1.child(sId).removeValue();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(YourProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dbRef2 = FirebaseDatabase.getInstance().getReference("Tokens");
                        dbRef2.child(sId).removeValue();
                        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent ii = new Intent(YourProfileActivity.this,LoginActivity.class);
                                    startActivity(ii);
                                    finish();
                                }
                            }
                        });
                    }
                });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(YourProfileActivity.this,
                        android.R.string.no, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    
}