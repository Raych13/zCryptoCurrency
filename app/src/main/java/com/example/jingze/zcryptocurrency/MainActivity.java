package com.example.jingze.zcryptocurrency;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.view.View;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.utils.ModelUtils;
import com.example.jingze.zcryptocurrency.utils.SnackBarUtils;
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
    private ViewpagerAdapter viewpagerAdapter;
    private Looper dataThreadLooper;
    private Handler dataThreadHandler;
    private ArrayList<CoinMenu> mainMenu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.i("Taych", "Is: " + (savedInstanceState == null));
        HandlerThread dataThread = new HandlerThread("DataThread", Process.THREAD_PRIORITY_BACKGROUND);
        dataThread.start();
        dataThreadLooper = dataThread.getLooper();
        dataThreadHandler = new Handler(dataThreadLooper);
        mainMenu = mockData();
        SnackBarUtils.setFindView(new SnackBarUtils.FindView() {
            @Override
            public View findView() {
                return getWindow().getDecorView();
            }
        });
        setupUI(savedInstanceState);
    }

    private void setupUI(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(10);
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
        viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(),
                viewPager_tab, dataThreadHandler, mainMenu);
        viewPager.setAdapter(viewpagerAdapter);
        viewPager_tab.setupWithViewPager(viewPager);
        viewpagerAdapter.setupTabLayout(mainMenu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewpagerAdapter.getItem(0);
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

    private ArrayList<CoinMenu> mockData() {
        //???Json is Not corret
//        String jsonListList = "[{\"name\":\"Favorite\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"price\":13000.13,\"dailyChangeRate\":0.3133},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"price\":1333.13,\"dailyChangeRate\":0.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"price\":393.13,\"dailyChangeRate\":0.3113}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"price\":13000.13,\"dailyChangeRate\":0.3639},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"price\":1333.13,\"dailyChangeRate\":0.6313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"price\":393.13,\"dailyChangeRate\":0.3836},{\"coinType\":\"EOS\",\"currencyType\":\"USD\",\"price\":7.98,\"dailyChangeRate\":0.066},{\"coinType\":\"ETC\",\"currencyType\":\"USD\",\"price\":20.13},{\"coinType\":\"XRP\",\"currencyType\":\"USD\",\"price\":0.5913,\"dailyChangeRate\":-0.133}]}]";
//        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"price\":13000.13,\"dailyChangeRate\":0.3639},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"price\":1333.13,\"dailyChangeRate\":0.6313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"price\":393.13,\"dailyChangeRate\":0.3836},{\"coinType\":\"EOS\",\"currencyType\":\"USD\",\"price\":7.98,\"dailyChangeRate\":0.066},{\"coinType\":\"ETC\",\"currencyType\":\"USD\",\"price\":20.13},{\"coinType\":\"XRP\",\"currencyType\":\"USD\",\"price\":0.5913,\"dailyChangeRate\":-0.133}]}]";
//        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"price\":13000.13,\"dailyChangeRate\":0.3639}]}]";

        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"coinType\":\"XRP\",\"currencyType\":\"USD\"},{\"coinType\":\"BCH\",\"currencyType\":\"USD\"},{\"coinType\":\"NEO\",\"currencyType\":\"USD\"},{\"coinType\":\"OMG\",\"currencyType\":\"USD\"},{\"coinType\":\"XMR\",\"currencyType\":\"USD\"}]}]";

        ArrayList<CoinMenu> total = ModelUtils.toObject(jsonListList, new TypeToken<ArrayList<CoinMenu>>(){});
        return total;
    }
}
