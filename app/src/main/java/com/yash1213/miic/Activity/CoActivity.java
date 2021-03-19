package com.yash1213.miic.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.yash1213.miic.R;


public class CoActivity extends AppCompatActivity {

    CardView cardUser, cardUpdates, cardCampusConnect, cardTeam;
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co);

        cardUser = findViewById(R.id.card_showUser);
        cardUpdates = findViewById(R.id.card_updates);
        cardCampusConnect = findViewById(R.id.card_campusConnect);
        cardTeam = findViewById(R.id.card_team);
        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

    }
}