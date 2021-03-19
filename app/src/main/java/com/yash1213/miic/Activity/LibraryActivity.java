package com.yash1213.miic.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.yash1213.miic.Adapter.LibraryAdapter;
import com.yash1213.miic.R;

public class LibraryActivity extends AppCompatActivity {

    private int image[] = {
            R.drawable.ic_round_folder,
            R.drawable.ic_round_folder,
            R.drawable.ic_round_folder,
            R.drawable.ic_round_folder,
            R.drawable.ic_round_folder,
            R.drawable.ic_round_folder,
            R.drawable.ic_round_folder,
            R.drawable.ic_round_folder
    };

    private String st[] = {"SEM - I","SEM - II","SEM - III","SEM - IV","SEM - V","SEM - VI","SEM - VII","SEM - VIII"};

    private String links[] = {"1","2","3","4","5","6","7","8"};
    private GridView gridView;
    private ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_library);

        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        gridView = findViewById(R.id.grid);
        gridView.setAdapter(new LibraryAdapter(this,st,image,links));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                }
                if (position == 1) {
                }
                if (position == 2) {
                }
                if (position == 3) {
                }
                if (position ==4) {
                }
                if (position == 5) {
                }
                if (position == 6) {
                }
                if (position == 7) {
                }
            }
        });


    }



}