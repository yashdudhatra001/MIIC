package com.yash1213.miic.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yash1213.miic.R;

public class CoLibActivity extends AppCompatActivity {

    private LinearLayout lsem1, lsem2, lsem3, lsem4, lsem5, lsem6, lsem7, lsem8, comp;
    private ImageView imageBack;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_lib);

        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lsem1 = findViewById(R.id.sem1);
        lsem2 = findViewById(R.id.sem2);
        lsem3 = findViewById(R.id.sem3);
        lsem4 = findViewById(R.id.sem4);
        lsem5 = findViewById(R.id.sem5);
        lsem6 = findViewById(R.id.sem6);
        lsem7 = findViewById(R.id.sem7);
        lsem8 = findViewById(R.id.sem8);
        comp = findViewById(R.id.comp);

        dbRef = FirebaseDatabase.getInstance().getReference("library");

        lsem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withEditText("sem1");
            }
        });

        lsem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withEditText("sem2");
            }
        });
        lsem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withEditText("sem3");
            }
        });
        lsem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withEditText("sem4");
            }
        });
        lsem5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withEditText("sem5");
            }
        });

        lsem6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withEditText("sem6");
            }
        });

        lsem7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withEditText("sem7");
            }
        });

        lsem8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withEditText("sem8");
            }
        });

        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withEditText("comp");
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlLink[8])));
            }
        });
    }

    public void withEditText(final String key) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Link for...");
        final EditText input = new EditText(CoLibActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbRef = FirebaseDatabase.getInstance().getReference("library");
                dbRef.child(key).setValue(input.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CoLibActivity.this, "Link Updated successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CoLibActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CoLibActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}