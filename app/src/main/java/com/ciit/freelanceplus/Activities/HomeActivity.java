package com.ciit.freelanceplus.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ciit.freelanceplus.Adapters.RecycleviewAdapter_navigation;
import com.ciit.freelanceplus.Fragments.HomeFragment;
import com.ciit.freelanceplus.R;

import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    ListView navigation_recycleview;
    private RecycleviewAdapter_navigation adapter_navigation;
    ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigation_recycleview = findViewById(R.id.navigation_recycleview);
        names = new ArrayList<>();
        names.add("Home");
        names.add("Orders");
        names.add("Notifications");
        names.add("Setting");
        names.add("Logout");
        adapter_navigation = new RecycleviewAdapter_navigation(HomeActivity.this,names);
        navigation_recycleview.setAdapter(adapter_navigation);
        navigation_recycleview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String x = names.get(i);
                if(x.equals("Orders"))
                {
                    Intent intent = new Intent(HomeActivity.this, AllOrderActivity.class);
                    startActivity(intent);
                }

                if(x.equals("Logout"))
                {
                    SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().apply();

                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ImageView menuBtn = findViewById(R.id.home_menu_btn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!drawer.isDrawerOpen(GravityCompat.START)) drawer.openDrawer(Gravity.LEFT);
                else drawer.closeDrawer(Gravity.LEFT);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, new HomeFragment()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


}
