package com.yash1213.miic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yash1213.miic.Adapter.CoCommentsAdapter;
import com.yash1213.miic.Model.StudentDetails;
import com.yash1213.miic.Notification.Client;
import com.yash1213.miic.Notification.Data;
import com.yash1213.miic.Notification.MyResponse;
import com.yash1213.miic.Notification.Sender;
import com.yash1213.miic.Notification.Token;
import com.yash1213.miic.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CoProfileActivity extends AppCompatActivity {

    private BottomSheetBehavior sheetBehavior;
    private CoordinatorLayout coordinatorLayout;
    private Button btAdd;
    private LinearLayout layoutBottomSheet;
    private TextView tData, tP;
    private TextView tvProfile;
    private ImageView imageBack, btAddCmnts;
    private String sId;
    private int points=0;
    private int pos;
    private TextView tvName, tvPoints, tvDesc;
    private RecyclerView rvComments;
    private ProgressBar progressBar;
    private CoCommentsAdapter commentsAdapter;
    private DatabaseReference dbRef,dbsRef;
    private ArrayList<StudentDetails> sdList = new ArrayList<>();

    private APIService apiService;
    private boolean notify = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_profile);

        Intent x = getIntent();
        sId = x.getStringExtra("sId");

        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        tvProfile = findViewById(R.id.textFirstChar);
        tvName = findViewById(R.id.name);
        tvPoints = findViewById(R.id.stars);
        tvDesc = findViewById(R.id.description2);
        progressBar = findViewById(R.id.progress_circular);
        rvComments = findViewById(R.id.listComments);
        rvComments.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvComments.setLayoutManager(layoutManager);

        btAddCmnts = findViewById(R.id.btAddCmnts);
        btAddCmnts.setActivated(false);
        btAdd = findViewById(R.id.addAll);
        tData = findViewById(R.id.addData);
        tP = findViewById(R.id.addP);

        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        coordinatorLayout = findViewById(R.id.coordinator);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        btAddCmnts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        dbsRef = FirebaseDatabase.getInstance().getReference("students").child(sId);
        dbsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tvName.setText(dataSnapshot.child("studentName").getValue().toString());
                    tvProfile.setText(dataSnapshot.child("studentName").getValue().toString().substring(0,1));
                    tvPoints.setText(dataSnapshot.child("totalPoint").getValue().toString()+" star");
                    tvDesc.setText(dataSnapshot.child("studentEmail").getValue().toString()+"\n"
                    +dataSnapshot.child("studentPh").getValue().toString());
                    points = Integer.parseInt(dataSnapshot.child("totalPoint").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CoProfileActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        dbRef = FirebaseDatabase.getInstance().getReference("students");
        dbRef.child(sId).child("profiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sdList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    StudentDetails sd = ds.getValue(StudentDetails.class);
                    sdList.add(sd);
                }
                commentsAdapter = new CoCommentsAdapter(getApplicationContext(),sdList);
                rvComments.setAdapter(commentsAdapter);
                commentsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                rvComments.setVisibility(View.VISIBLE);
                btAddCmnts.setActivated(true);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CoProfileActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = tData.getText().toString();
                String spt = tP.getText().toString();

                if(!(TextUtils.isEmpty(data) || TextUtils.isEmpty(spt))){
                    int pt = Integer.parseInt(spt);
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    StudentDetails studentDetails = new StudentDetails(date,data,pt);
                    pos = sdList.size();
                    dbRef = FirebaseDatabase.getInstance().getReference("students").child(sId).child("profiles");
                    dbRef.child(""+pos).setValue(studentDetails);
                    Map<String,Object> upMap = new HashMap<>();
                    upMap.put("totalPoint",points+pt);
                    dbsRef = FirebaseDatabase.getInstance().getReference("students").child(sId);
                    dbsRef.updateChildren(upMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            tData.setText("");
                            tP.setText("");
                            notify = true;
                            commentsAdapter.notifyDataSetChanged();
                            if(notify){
                                sendNotification(sId);
                                notify = false;
                            }
                          }});
                }
            }
        });
    }

    private void sendNotification(String receiver){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(sId, R.mipmap.ic_icon,"check your activity score." , "Profile Updated.",
                            sId);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(CoProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }else{
                                           // Toast.makeText(CoProfileActivity.this, "SuccessNotification", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}