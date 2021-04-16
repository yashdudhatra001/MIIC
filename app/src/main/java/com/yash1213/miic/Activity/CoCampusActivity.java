package com.yash1213.miic.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yash1213.miic.Adapter.CampusAdapter;
import com.yash1213.miic.Model.Campus;
import com.yash1213.miic.Notification.Client;
import com.yash1213.miic.Notification.Data;
import com.yash1213.miic.Notification.MyResponse;
import com.yash1213.miic.Notification.Sender;
import com.yash1213.miic.Notification.Token;
import com.yash1213.miic.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoCampusActivity extends AppCompatActivity {

    private ImageView imageBack, btAddCampus;
    private RecyclerView rvCampus;
    private ProgressBar progressBar;
    private Button btAdd;
    private EditText addTitle, addDesc;
    private BottomSheetBehavior sheetBehavior;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayout layoutBottomSheet;

    private CampusAdapter campusAdapter;
    private DatabaseReference dbRef, dbIRef;
    private ArrayList<Campus> campuses = new ArrayList<>();

    private APIService apiService;
    private boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_campus);

        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        progressBar = findViewById(R.id.progress_circular);
        rvCampus = findViewById(R.id.campus_tips);
        rvCampus.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvCampus.setLayoutManager(layoutManager);

        btAddCampus = findViewById(R.id.btAddCampus);
        btAdd = findViewById(R.id.addCampus);
        addTitle = findViewById(R.id.addTitle);
        addDesc = findViewById(R.id.addDesc);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        coordinatorLayout = findViewById(R.id.coordinator_campus);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        btAddCampus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dbRef = FirebaseDatabase.getInstance().getReference("campus");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                campuses.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Campus c = dataSnapshot.getValue(Campus.class);
                    campuses.add(c);
                }
                campusAdapter = new CampusAdapter(CoCampusActivity.this,campuses);
                rvCampus.setAdapter(campusAdapter);
                campusAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                rvCampus.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CoCampusActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = addTitle.getText().toString().concat("");
                String desc = addDesc.getText().toString().concat("");
                Campus campus = new Campus(title,desc);
                dbIRef = FirebaseDatabase.getInstance().getReference("campus");
                dbIRef.push().setValue(campus).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            addTitle.setText("");
                            addDesc.setText("");
                            notify= true;
                            if(notify) {
                                sendNotification();
                            }
                            campusAdapter.notifyDataSetChanged();
                            Toast.makeText(CoCampusActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    private void sendNotification(){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        //Query query = tokens.orderByKey().equalTo(receiver);
        tokens.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(snapshot.getKey(), R.mipmap.ic_icon,"New Tips Added. Take a look." , "MIIC Campus note",
                            snapshot.getKey());
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            //Toast.makeText(CoCampusActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }else{
                                           // Toast.makeText(CoCampusActivity.this, "SuccessNotification", Toast.LENGTH_SHORT).show();
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