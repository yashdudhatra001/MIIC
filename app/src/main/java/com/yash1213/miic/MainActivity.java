package com.yash1213.miic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener
{

    ViewPager2 viewPager2;
    CircleIndicator circleIndicator;
    ArrayList arrayList;
    GridView grid;
    Context context;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    public static String[] gridViewStrings = {
            "one",
            "two",
            "three",
            "four"
    };
    public static int img[] = {
            R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.five,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.slideImageView);
        circleIndicator = findViewById(R.id.indicator);
        //imageDraw = findViewById(R.id.imageDraw);
        grid = findViewById(R.id.grid);
        grid.setAdapter(new CustomAdapterGridViewAdapter(this,gridViewStrings,img));

        //toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open right drawer
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(context, DashboardActivity.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(context, CampusActivity.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(context, UpdatesActivity.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    //Intent intent = new Intent(context, LibararyActivity.class);
                    //startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_item_one) {
            Toast.makeText(context, "item 1 selected", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.nav_item_two) {
            Toast.makeText(context, "Item 2 selected", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.nav_item_three) {
            Toast.makeText(context, "Item 3 selected", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.nav_item_four){
            Toast.makeText(context, "Item 4 selected", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.nav_item_five) {
            Toast.makeText(context, "Item 5 selected", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}