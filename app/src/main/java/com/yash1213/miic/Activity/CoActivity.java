package com.yash1213.miic.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yash1213.miic.R;


public class CoActivity extends AppCompatActivity {

    CardView cardUser, cardUpdates, cardCampusConnect, cardTeam, cardLib;
    ImageView imageBack;
    DatabaseReference dbRef;
    SwitchCompat aSwitch;
    String play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co);
        cardUser = findViewById(R.id.card_showUser);
        cardUpdates = findViewById(R.id.card_updates);
        cardCampusConnect = findViewById(R.id.card_campusConnect);
        cardTeam = findViewById(R.id.card_team);
        cardLib = findViewById(R.id.card_lib);
        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        aSwitch = findViewById(R.id.switch_compat);
        aSwitch.setVisibility(View.GONE);
        dbRef = FirebaseDatabase.getInstance().getReference("run");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(CoActivity.this, ""+snapshot.child("play").getValue().toString(), Toast.LENGTH_SHORT).show();
                play = snapshot.child("play").getValue().toString();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                aSwitch.setChecked(Boolean.parseBoolean(play));
                aSwitch.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if(aSwitch.isChecked()){
                    play ="true";
                    dbRef.child("play").setValue(play);
                    Snackbar.make(buttonView, "Application Running", Snackbar.LENGTH_LONG)
                            .setAction("ACTION",null).show();
                }
                else{
                    play ="false";
                    dbRef.child("play").setValue(play);
                    Snackbar.make(buttonView, "Application Stopped", Snackbar.LENGTH_LONG)
                            .setAction("ACTION",null).show();
                }
            }
        });

        cardUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoActivity.this, CoUsersActivity.class);
                startActivity(intent);
            }
        });

        cardUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoActivity.this, CoUpdateActivity.class);
                startActivity(intent);
            }
        });

        cardCampusConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoActivity.this, CoCampusActivity.class);
                startActivity(intent);
            }
        });

        cardTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoActivity.this, CoCurrentActivity.class);
                startActivity(intent);
            }
        });

        cardLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoActivity.this, CoLibActivity.class);
                startActivity(intent);
            }
        });
    }
}