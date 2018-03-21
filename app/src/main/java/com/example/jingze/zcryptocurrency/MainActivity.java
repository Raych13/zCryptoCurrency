package com.example.jingze.zcryptocurrency;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.utils.ModelUtils;
import com.example.jingze.zcryptocurrency.view.ViewpagerAdapter;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.main_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer_nav_left) NavigationView navigationView;
    @BindView(R.id.main_appbar) AppBarLayout appBarLayout;
    @BindView(R.id.main_viewpager) ViewPager viewPager;
    @BindView((R.id.main_viewpager_tab)) TabLayout viewPager_tab;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.i("Taych", "Is: " + (savedInstanceState == null));
        setupUI(savedInstanceState);
    }

    private void setupUI(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(1);
        setupActionBar();
        setupViewpager(savedInstanceState);
    }


    private void setupActionBar() {
        //If you add toolbar when you call ActionBarDrawerToggle method, you can skip the two steps below.
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

//        Immersive UI
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupViewpager(Bundle savedInstanceState) {
        ArrayList<ArrayList<Coin>> mainMenu = mockData();
        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(),
                mainMenu, viewPager_tab);
        viewPager.setAdapter(viewpagerAdapter);
        viewPager_tab.setupWithViewPager(viewPager);
        viewpagerAdapter.setupTabLayout();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Save the star menu;
    }

    private ArrayList<ArrayList<Coin>> mockData() {
        String jsonListList = "[[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"priceUSD\":13000.13,\"dailyChange\":0.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"priceUSD\":1333.13,\"dailyChange\":0.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"priceUSD\":393.13,\"dailyChange\":0.3131}],[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"priceUSD\":13000.13,\"dailyChange\":0.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"priceUSD\":1333.13,\"dailyChange\":0.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"priceUSD\":393.13,\"dailyChange\":0.3131},{\"coinType\":\"NEM\",\"currencyType\":\"USD\",\"priceUSD\":0.31,\"dailyChange\":-0.13},{\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"coinType\":\"XRP\",\"currencyType\":\"USD\",\"priceUSD\":0.74,\"dailyChange\":0.0}]]";
        ArrayList<ArrayList<Coin>> total = ModelUtils.toObject(jsonListList, new TypeToken<ArrayList<ArrayList<Coin>>>(){});
        return total;
    }
}
