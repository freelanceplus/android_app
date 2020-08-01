package com.ciit.freelanceplus.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ciit.freelanceplus.Fragments.BuyerAllOrders;
import com.ciit.freelanceplus.Fragments.BuyerDashboardFragment;
import com.ciit.freelanceplus.Model.Utils;
import com.ciit.freelanceplus.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BuyerDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_dashboard);

        getSupportFragmentManager().beginTransaction().replace(R.id.buyer_dashboard_Frame, new BuyerDashboardFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.menu_home)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.buyer_dashboard_Frame, new BuyerDashboardFragment()).commit();
                }
                if(menuItem.getItemId() == R.id.menu_orders)
                {

                    getSupportFragmentManager().beginTransaction().replace(R.id.buyer_dashboard_Frame, new BuyerAllOrders()).commit();
                }
                if (menuItem.getItemId() == R.id.menu_notificaton)
                {

                }

                if(menuItem.getItemId() == R.id.menu_account){

                }
                return false;
            }
        });
    }
}
