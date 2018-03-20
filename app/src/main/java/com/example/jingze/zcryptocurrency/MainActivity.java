package com.example.jingze.zcryptocurrency;

import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.main_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer_nav_left) NavigationView navigationView;
    @BindView(R.id.main_viewpager) ViewPager viewPager;
    @BindView((R.id.main_viewpager_tab)) TabLayout viewPager_tab;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupUI();
    }

    private void setupUI() {
        setSupportActionBar(toolbar);
        setupActionBar();
        setupViewpager();
    }

    private void setupActionBar() {
        //If you add toolbar when you call ActionBarDrawerToggle method, you can skip the two steps below.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);

//        drawerToggle.syncState();
//        navigationView.setNavigationItemSelectedListener(null);
    }

    private void setupViewpager() {
        ArrayList<ArrayList<Coin>> mainMenu = mockData();
        viewPager.setAdapter(new ViewpagerAdapter(getSupportFragmentManager(), mainMenu));

        viewPager_tab.setupWithViewPager(viewPager);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
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

    private ArrayList<ArrayList<Coin>> mockData() {
        String jsonListList = "[[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"priceUSD\":13000.13,\"dailyChange\":0.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"priceUSD\":1333.13,\"dailyChange\":0.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"priceUSD\":393.13,\"dailyChange\":0.3131}],[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"priceUSD\":13000.13,\"dailyChange\":0.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"priceUSD\":1333.13,\"dailyChange\":0.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"priceUSD\":393.13,\"dailyChange\":0.3131},{\"coinType\":\"NEM\",\"currencyType\":\"USD\",\"priceUSD\":0.31,\"dailyChange\":-0.13},{\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"coinType\":\"XRP\",\"currencyType\":\"USD\",\"priceUSD\":0.74,\"dailyChange\":0.0}]]";
        ArrayList<ArrayList<Coin>> total = ModelUtils.toObject(jsonListList, new TypeToken<ArrayList<ArrayList<Coin>>>(){});
        return total;
    }
}
