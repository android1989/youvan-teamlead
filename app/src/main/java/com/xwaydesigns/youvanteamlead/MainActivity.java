package com.xwaydesigns.youvanteamlead;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xwaydesigns.youvanteamlead.Adapters.ViewPagerAdapter;
import com.xwaydesigns.youvanteamlead.ExtraClasses.Constants;
import com.xwaydesigns.youvanteamlead.ExtraClasses.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager mviewpager;
    private ViewPagerAdapter mViewPagerAdapter;
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar main_toolbar = findViewById(R.id.mainpage_toolbar);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setTitle("Youvan TeamLead");
        contextOfApplication = getApplicationContext();
        mviewpager = findViewById(R.id.view_pager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mviewpager.setAdapter(mViewPagerAdapter);
        bottomNavigationView =findViewById(R.id.bottom_navigation);

        //--------------------------------------------------------\\
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_dashboard:
                        mviewpager.setCurrentItem(0);
                        break;
                    case R.id.nav_item:
                        mviewpager.setCurrentItem(1);
                        break;
                    case R.id.nav_profile:
                        mviewpager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });

        //--------------------------------------------------------\\
        mviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.nav_dashboard).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.nav_item).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        //--------------------------------------------------------------------------\\


    }


    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if(item.getItemId()== R.id.main_logout)
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getString(R.string.menu_logout))
                    .setCancelable(false)
                    .setMessage("Do you want to LogOut ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SessionManager manager = new SessionManager(MainActivity.this);
                            manager.Logout();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // do nothing
                        }
                    })
                    .show();
        }
        if(item.getItemId()== R.id.main_change_password)
        {

        }
        return true;
    }

    public void displayExceptionMessage1(String msg) {
        Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
    }
    public void displayExceptionMessage2(String msg) {
        Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        ExitApp();
    }

    private void ExitApp() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Do you want to Exit App ?")
                .setIcon(R.drawable.app_logo_circle)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }


//
}