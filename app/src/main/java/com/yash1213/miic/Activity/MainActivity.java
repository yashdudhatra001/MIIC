package com.yash1213.miic.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.yash1213.miic.Adapter.ViewPagerAdapter;
import com.yash1213.miic.Notification.Token;
import com.yash1213.miic.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private CircleIndicator indicator;
    private LinearLayout llDashboard, llUpdates, llC2C, llLibrary;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView tvFirstChar, tvName, tvEmail;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private ImageView instaImage,facebookImage, linkedInImage, youtubeImage;

    private String sId;
    private Handler handler;
    private Timer swipeTimer;
    private boolean doubleBackToExitPressedOnce = false;
    private String play = "";


    private static final String instaUrl = "https://www.instagram.com/invites/contact/?i=dttgsyjcb9yg&utm_content=83zd7zp";
    private static final String facebookUrl = "https://facebook.com/MIIC-Mechanical-Industrial-Interaction-Cell-106195180724433";
    private static final String linkedInUrl = "https://www.linkedin.com/company/mechanical-industrial-interaction-cell";
    private static final String formUrl = "https://forms.gle/UuVayjcZWfkY7WbZA";
    private static final String youtubeUrl = "https://youtube.com/channel/UCjXSZKQgZ62BWY4b00FDfrw";
    private static final String privacyPolicy = "https://miic4u.blogspot.com/2021/04/privacy-policy-yash-dudhatra-built.html";
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private PagerAdapter adapter;

    private String userId;
    public static String[] gridViewStrings = {
            "Dashboard",
            "Updates",
            "Library",
            "C2C"
    };
    public static int img[] = {
            R.drawable.dashboard,
            R.drawable.rem,
            R.drawable.book,
            R.drawable.school,
    };

    public ArrayList<String> imageUri  = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        sId = mAuth.getCurrentUser().getUid();

        viewPager = findViewById(R.id.slideImageView);
        indicator = findViewById(R.id.indicator);


        llDashboard = findViewById(R.id.dashboard);
        llUpdates = findViewById(R.id.updates);
        llC2C = findViewById(R.id.c2c);
        llLibrary = findViewById(R.id.library);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open left drawer
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        llLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LibraryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
            }
        });

        llC2C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CampusActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
            }
        });

        llUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdatesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
            }
        });

        llDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        tvFirstChar = navigationView.getHeaderView(0).findViewById(R.id.textFirstChar);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.textName);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.textEmail);
        dbRef = FirebaseDatabase.getInstance().getReference("students");
        dbRef.orderByChild("studentId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        tvFirstChar.setText(dataSnapshot.child("studentName").getValue().toString().substring(0,1).concat(""));
                        tvName.setText(dataSnapshot.child("studentName").getValue().toString().concat(""));
                        tvEmail.setText(dataSnapshot.child("studentEmail").getValue().toString().concat(""));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        instaImage = findViewById(R.id.instaImage);
        facebookImage = findViewById(R.id.facebookImage);
        linkedInImage = findViewById(R.id.linkedInImage);
        youtubeImage = findViewById(R.id.youtubeImage);
        instaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(instaUrl)));
            }
        });
        facebookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
            }
        });
        linkedInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl)));
            }
        });

        youtubeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)));
            }
        });
    }



    private void getImageUrls() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Posts");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageUri.clear();
                int temp= (int) snapshot.getChildrenCount();
                int i=0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    i++;
                    if(temp<5){
                        imageUri.add(dataSnapshot.child("imageUri").getValue().toString());
                    }else if(temp<i+4){
                        imageUri.add(dataSnapshot.child("imageUri").getValue().toString());
                    }
                }
                Collections.sort(imageUri,Collections.<String>reverseOrder());
                //Toast.makeText(MainActivity.this, ""+imageUri.size(), Toast.LENGTH_SHORT).show();
                adapter = new ViewPagerAdapter(MainActivity.this,imageUri);
                viewPager.setAdapter(adapter);

                indicator.setViewPager(viewPager);
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        currentPage = position;
                    }
                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }
                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }});
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "error in loading image", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_item_one) {
            Intent ii = new Intent(MainActivity.this,OurStoryActivity.class);
            startActivity(ii);
            overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
        }else if(id == R.id.nav_item_two) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(formUrl)));
        }else if(id == R.id.nav_item_three) {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
            finish();
        }else if(id == R.id.nav_item_zero){
            Intent ii = new Intent(MainActivity.this,YourProfileActivity.class);
            startActivity(ii);
            overridePendingTransition(R.xml.activity_in,R.xml.activity_out);

        }else if(id == R.id.nav_item_four){
            Intent ii = new Intent(MainActivity.this,CurrentCosActivity.class);
            startActivity(ii);
            overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
        }else if(id == R.id.nav_item_five){
            Intent ii = new Intent(MainActivity.this,FounderActivity.class);
            startActivity(ii);
            overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
        }else if(id == R.id.nav_item_six){
            Intent ii = new Intent(MainActivity.this,DeveloperActivity.class);
            startActivity(ii);
            overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
        }else if(id == R.id.nav_item_seven){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicy)));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        DatabaseReference dbRun = FirebaseDatabase.getInstance().getReference("run");
        dbRun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                play = snapshot.child("play").getValue().toString();
                if(play.equals("false")){
                    Toast.makeText(getApplicationContext(), "App Closed By Admin", Toast.LENGTH_LONG).show();
                    try {
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                        alertDialog.setTitle("Info");
                        alertDialog.setMessage("App under Development.");
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        alertDialog.show();
                    } catch (Exception e) {
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            } catch (Exception e) {
            }
        }  else{
            getImageUrls();

            handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    NUM_PAGES = 4;
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    viewPager.setCurrentItem(currentPage++, true);
                }
            };
            swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, 500, 3000);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        swipeTimer.cancel();
    }
    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(sId).setValue(token1);
    }

}