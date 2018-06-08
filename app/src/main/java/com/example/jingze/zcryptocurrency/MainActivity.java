package com.example.jingze.zcryptocurrency;

import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
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
import android.view.View;

import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.utils.ModelUtils;
import com.example.jingze.zcryptocurrency.utils.NetworkServiceUtils;
import com.example.jingze.zcryptocurrency.utils.RunningTimeUtils;
import com.example.jingze.zcryptocurrency.utils.SnackBarUtils;
import com.example.jingze.zcryptocurrency.view.ViewpagerAdapter;
import com.example.jingze.zcryptocurrency.view.market_list.MarketListFragment;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.main_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer_nav_left) NavigationView navigationView;
    @BindView(R.id.main_appbar) AppBarLayout appBarLayout;
    @BindView(R.id.main_viewpager) ViewPager viewPager;
    @BindView((R.id.main_viewpager_tab)) TabLayout viewPager_tab;

    private final int VIEWPAGER_INITIAL_POSITION = 0;
    private ActionBarDrawerToggle drawerToggle;
    private ViewpagerAdapter viewpagerAdapter;
    private Handler dataThreadHandler;
    private ArrayList<CoinMenu> mainMenu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RunningTimeUtils.start();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.i("Taych", "Is: " + (savedInstanceState == null));

        HandlerThread dataThread = new HandlerThread("DataThread", Process.THREAD_PRIORITY_BACKGROUND);
        dataThread.start();

        dataThreadHandler = new Handler(dataThread.getLooper());
        mainMenu = mockData();

        setupUI(savedInstanceState);
//        RunningTimeUtils.end("MainActivity onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        initiateUtils();
//        RunningTimeUtils.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void initiateUtils() {
//        NetworkServiceUtils.setGetContext(new NetworkServiceUtils.GetContext() {
//            @Override
//            public Context getContext() {
//                return getApplicationContext();
//            }
//        });
        NetworkServiceUtils.setContext(getApplicationContext());
        SnackBarUtils.setFindView(new SnackBarUtils.FindView() {
            @Override
            public View findView() {
                return getWindow().getDecorView();
            }
        });
    }

    private void setupUI(Bundle savedInstanceState) {
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
//        toolbar.setTitle("zCryptoCurrency");
        setSupportActionBar(toolbar);
        setTitle("zCryptoCurrency");

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
        viewPager.setCurrentItem(VIEWPAGER_INITIAL_POSITION);
        MarketListFragment.viewPager = viewPager;
//        MarketListFragment.currentPagePosition = VIEWPAGER_INITIAL_POSITION;
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i("RaychTest1", "onPageScrolled(): Position: " + position + ". PositionOffset: " + positionOffset + ".");
//                MarketListFragment.currentPagePosition = position;
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.i("RaychTest2", "onPageSelected(): Position: " + position + ".");
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                Log.i("RaychTest3", "onPageScrollStateChanged(): State: " + state);
//                MarketListFragment.viewPagerScrollState = state;
//            }
//        });
        viewPager_tab.setupWithViewPager(viewPager);
        viewpagerAdapter.setupTabLayout(mainMenu);
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
//        //Without title
//        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"coinType\":\"XRP\",\"currencyType\":\"USD\"},{\"coinType\":\"BCH\",\"currencyType\":\"USD\"},{\"coinType\":\"NEO\",\"currencyType\":\"USD\"},{\"coinType\":\"OMG\",\"currencyType\":\"USD\"},{\"coinType\":\"XMR\",\"currencyType\":\"USD\"},{\"coinType\":\"BTG\",\"currencyType\":\"USD\"},{\"coinType\":\"ELF\",\"currencyType\":\"USD\"},{\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"coinType\":\"BAT\",\"currencyType\":\"USD\"}]}]";
//        //Single List
//        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"},{\"title\":\"○  BCH - USD\",\"coinType\":\"BCH\",\"currencyType\":\"USD\"},{\"title\":\"○  NEO - USD\",\"coinType\":\"NEO\",\"currencyType\":\"USD\"},{\"title\":\"○  OMG - USD\",\"coinType\":\"OMG\",\"currencyType\":\"USD\"},{\"title\":\"○  XMR - USD\",\"coinType\":\"XMR\",\"currencyType\":\"USD\"},{\"title\":\"○  BTG - USD\",\"coinType\":\"BTG\",\"currencyType\":\"USD\"},{\"title\":\"○  ELF - USD\",\"coinType\":\"ELF\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"}]}]";
//          /update Data Test
//        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"},{\"title\":\"○  BCH - USD\",\"coinType\":\"BCH\",\"currencyType\":\"USD\"},{\"title\":\"○  NEO - USD\",\"coinType\":\"NEO\",\"currencyType\":\"USD\"},{\"title\":\"○  OMG - USD\",\"coinType\":\"OMG\",\"currencyType\":\"USD\"},{\"title\":\"○  XMR - USD\",\"coinType\":\"XMR\",\"currencyType\":\"USD\"},{\"title\":\"○  BTG - USD\",\"coinType\":\"BTG\",\"currencyType\":\"USD\"},{\"title\":\"○  ELF - USD\",\"coinType\":\"ELF\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"}]}]";
//          //Double Lists
//        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"},{\"title\":\"○  BCH - USD\",\"coinType\":\"BCH\",\"currencyType\":\"USD\"},{\"title\":\"○  NEO - USD\",\"coinType\":\"NEO\",\"currencyType\":\"USD\"},{\"title\":\"○  OMG - USD\",\"coinType\":\"OMG\",\"currencyType\":\"USD\"},{\"title\":\"○  XMR - USD\",\"coinType\":\"XMR\",\"currencyType\":\"USD\"},{\"title\":\"○  BTG - USD\",\"coinType\":\"BTG\",\"currencyType\":\"USD\"},{\"title\":\"○  ELF - USD\",\"coinType\":\"ELF\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"}]}]";
//        //Triple lists
//        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"},{\"title\":\"○  BCH - USD\",\"coinType\":\"BCH\",\"currencyType\":\"USD\"},{\"title\":\"○  NEO - USD\",\"coinType\":\"NEO\",\"currencyType\":\"USD\"},{\"title\":\"○  OMG - USD\",\"coinType\":\"OMG\",\"currencyType\":\"USD\"},{\"title\":\"○  XMR - USD\",\"coinType\":\"XMR\",\"currencyType\":\"USD\"},{\"title\":\"○  BTG - USD\",\"coinType\":\"BTG\",\"currencyType\":\"USD\"},{\"title\":\"○  ELF - USD\",\"coinType\":\"ELF\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"}]}]";
        // Five
//        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"},{\"title\":\"○  BCH - USD\",\"coinType\":\"BCH\",\"currencyType\":\"USD\"},{\"title\":\"○  NEO - USD\",\"coinType\":\"NEO\",\"currencyType\":\"USD\"},{\"title\":\"○  OMG - USD\",\"coinType\":\"OMG\",\"currencyType\":\"USD\"},{\"title\":\"○  XMR - USD\",\"coinType\":\"XMR\",\"currencyType\":\"USD\"},{\"title\":\"○  BTG - USD\",\"coinType\":\"BTG\",\"currencyType\":\"USD\"},{\"title\":\"○  ELF - USD\",\"coinType\":\"ELF\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"}]}]";
//        //Huobi
//        String jsonListList = "[{\"name\":\"Huobi\",\"url\":\"wss://api.hadax.com/ws\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"}]}]";

        //Poloniex two coins for Test
        String jsonListList = "[{\"name\":\"Poloniex\",\"url\":\"wss://api.huobipro.com/ws\",\"data\":[{\"title\":\"○  BTC - USDT\",\"coinType\":\"BTC\",\"currencyType\":\"USDT\"},{\"title\":\"○  LTC - USDT\",\"coinType\":\"LTC\",\"currencyType\":\"USDT\"}]}]";

//        //A completed Bitfinex list
//        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"},{\"title\":\"○  BCH - USD\",\"coinType\":\"BCH\",\"currencyType\":\"USD\"},{\"title\":\"○  NEO - USD\",\"coinType\":\"NEO\",\"currencyType\":\"USD\"},{\"title\":\"○  OMG - USD\",\"coinType\":\"OMG\",\"currencyType\":\"USD\"},{\"title\":\"○  XMR - USD\",\"coinType\":\"XMR\",\"currencyType\":\"USD\"},{\"title\":\"○  BTG - USD\",\"coinType\":\"BTG\",\"currencyType\":\"USD\"},{\"title\":\"○  ELF - USD\",\"coinType\":\"ELF\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"},{\"title\":\"○  TRX - USD\",\"coinType\":\"TRX\",\"currencyType\":\"USD\"},{\"title\":\"○  SAN - USD\",\"coinType\":\"SAN\",\"currencyType\":\"USD\"},{\"title\":\"○  RCN - USD\",\"coinType\":\"RCN\",\"currencyType\":\"USD\"},{\"title\":\"○  EDO - USD\",\"coinType\":\"EDO\",\"currencyType\":\"USD\"},{\"title\":\"○  TNB - USD\",\"coinType\":\"TNB\",\"currencyType\":\"USD\"},{\"title\":\"○  ETP - USD\",\"coinType\":\"ETP\",\"currencyType\":\"USD\"},{\"title\":\"○  ZRX - USD\",\"coinType\":\"ZRX\",\"currencyType\":\"USD\"},{\"title\":\"○  SNT - USD\",\"coinType\":\"SNT\",\"currencyType\":\"USD\"},{\"title\":\"○  SPK - USD\",\"coinType\":\"SPK\",\"currencyType\":\"USD\"},{\"title\":\"○  REP - USD\",\"coinType\":\"REP\",\"currencyType\":\"USD\"},{\"title\":\"○  YYW - USD\",\"coinType\":\"YYW\",\"currencyType\":\"USD\"},{\"title\":\"○  FUN - USD\",\"coinType\":\"FUN\",\"currencyType\":\"USD\"},{\"title\":\"○  GNT - USD\",\"coinType\":\"GNT\",\"currencyType\":\"USD\"},{\"title\":\"○  AID - USD\",\"coinType\":\"AID\",\"currencyType\":\"USD\"},{\"title\":\"○  MNA - USD\",\"coinType\":\"MNA\",\"currencyType\":\"USD\"},{\"title\":\"○  RLC - USD\",\"coinType\":\"RLC\",\"currencyType\":\"USD\"},{\"title\":\"○  AVT - USD\",\"coinType\":\"AVT\",\"currencyType\":\"USD\"},{\"title\":\"○  SNG - USD\",\"coinType\":\"SNG\",\"currencyType\":\"USD\"}]}]";

//        //A completed Bitfinex list with two uncompleted ones.
//        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"},{\"title\":\"○  BCH - USD\",\"coinType\":\"BCH\",\"currencyType\":\"USD\"},{\"title\":\"○  NEO - USD\",\"coinType\":\"NEO\",\"currencyType\":\"USD\"},{\"title\":\"○  OMG - USD\",\"coinType\":\"OMG\",\"currencyType\":\"USD\"},{\"title\":\"○  XMR - USD\",\"coinType\":\"XMR\",\"currencyType\":\"USD\"},{\"title\":\"○  BTG - USD\",\"coinType\":\"BTG\",\"currencyType\":\"USD\"},{\"title\":\"○  ELF - USD\",\"coinType\":\"ELF\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"},{\"title\":\"○  TRX - USD\",\"coinType\":\"TRX\",\"currencyType\":\"USD\"},{\"title\":\"○  SAN - USD\",\"coinType\":\"SAN\",\"currencyType\":\"USD\"},{\"title\":\"○  RCN - USD\",\"coinType\":\"RCN\",\"currencyType\":\"USD\"},{\"title\":\"○  EDO - USD\",\"coinType\":\"EDO\",\"currencyType\":\"USD\"},{\"title\":\"○  TNB - USD\",\"coinType\":\"TNB\",\"currencyType\":\"USD\"},{\"title\":\"○  ETP - USD\",\"coinType\":\"ETP\",\"currencyType\":\"USD\"},{\"title\":\"○  ZRX - USD\",\"coinType\":\"ZRX\",\"currencyType\":\"USD\"},{\"title\":\"○  SNT - USD\",\"coinType\":\"SNT\",\"currencyType\":\"USD\"},{\"title\":\"○  SPK - USD\",\"coinType\":\"SPK\",\"currencyType\":\"USD\"},{\"title\":\"○  REP - USD\",\"coinType\":\"REP\",\"currencyType\":\"USD\"},{\"title\":\"○  YYW - USD\",\"coinType\":\"YYW\",\"currencyType\":\"USD\"},{\"title\":\"○  FUN - USD\",\"coinType\":\"FUN\",\"currencyType\":\"USD\"},{\"title\":\"○  GNT - USD\",\"coinType\":\"GNT\",\"currencyType\":\"USD\"},{\"title\":\"○  AID - USD\",\"coinType\":\"AID\",\"currencyType\":\"USD\"},{\"title\":\"○  MNA - USD\",\"coinType\":\"MNA\",\"currencyType\":\"USD\"},{\"title\":\"○  RLC - USD\",\"coinType\":\"RLC\",\"currencyType\":\"USD\"},{\"title\":\"○  AVT - USD\",\"coinType\":\"AVT\",\"currencyType\":\"USD\"},{\"title\":\"○  SNG - USD\",\"coinType\":\"SNG\",\"currencyType\":\"USD\"}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"}]}]";

        //A completed Bitfinex list with an uncompleted Huobi list
//        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"},{\"title\":\"○  BCH - USD\",\"coinType\":\"BCH\",\"currencyType\":\"USD\"},{\"title\":\"○  NEO - USD\",\"coinType\":\"NEO\",\"currencyType\":\"USD\"},{\"title\":\"○  OMG - USD\",\"coinType\":\"OMG\",\"currencyType\":\"USD\"},{\"title\":\"○  XMR - USD\",\"coinType\":\"XMR\",\"currencyType\":\"USD\"},{\"title\":\"○  BTG - USD\",\"coinType\":\"BTG\",\"currencyType\":\"USD\"},{\"title\":\"○  ELF - USD\",\"coinType\":\"ELF\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"},{\"title\":\"○  TRX - USD\",\"coinType\":\"TRX\",\"currencyType\":\"USD\"},{\"title\":\"○  SAN - USD\",\"coinType\":\"SAN\",\"currencyType\":\"USD\"},{\"title\":\"○  RCN - USD\",\"coinType\":\"RCN\",\"currencyType\":\"USD\"},{\"title\":\"○  EDO - USD\",\"coinType\":\"EDO\",\"currencyType\":\"USD\"},{\"title\":\"○  TNB - USD\",\"coinType\":\"TNB\",\"currencyType\":\"USD\"},{\"title\":\"○  ETP - USD\",\"coinType\":\"ETP\",\"currencyType\":\"USD\"},{\"title\":\"○  ZRX - USD\",\"coinType\":\"ZRX\",\"currencyType\":\"USD\"},{\"title\":\"○  SNT - USD\",\"coinType\":\"SNT\",\"currencyType\":\"USD\"},{\"title\":\"○  SPK - USD\",\"coinType\":\"SPK\",\"currencyType\":\"USD\"},{\"title\":\"○  REP - USD\",\"coinType\":\"REP\",\"currencyType\":\"USD\"},{\"title\":\"○  YYW - USD\",\"coinType\":\"YYW\",\"currencyType\":\"USD\"},{\"title\":\"○  FUN - USD\",\"coinType\":\"FUN\",\"currencyType\":\"USD\"},{\"title\":\"○  GNT - USD\",\"coinType\":\"GNT\",\"currencyType\":\"USD\"},{\"title\":\"○  AID - USD\",\"coinType\":\"AID\",\"currencyType\":\"USD\"},{\"title\":\"○  MNA - USD\",\"coinType\":\"MNA\",\"currencyType\":\"USD\"},{\"title\":\"○  RLC - USD\",\"coinType\":\"RLC\",\"currencyType\":\"USD\"},{\"title\":\"○  AVT - USD\",\"coinType\":\"AVT\",\"currencyType\":\"USD\"},{\"title\":\"○  SNG - USD\",\"coinType\":\"SNG\",\"currencyType\":\"USD\"}]}," +
//                "{\"name\":\"Huobi\",\"url\":\"wss://api.huobipro.com/ws\",\"data\":[{\"title\":\"○  BTC - USDT\",\"coinType\":\"BTC\",\"currencyType\":\"USDT\"},{\"title\":\"○  BCH - USDT\",\"coinType\":\"BCH\",\"currencyType\":\"USDT\"},{\"title\":\"○  ETH - USDT\",\"coinType\":\"ETH\",\"currencyType\":\"USDT\"},{\"title\":\"○  ETC - USDT\",\"coinType\":\"ETC\",\"currencyType\":\"USDT\"},{\"title\":\"○  LTC - USDT\",\"coinType\":\"LTC\",\"currencyType\":\"USDT\"},{\"title\":\"○  EOS - USDT\",\"coinType\":\"EOS\",\"currencyType\":\"USDT\"},{\"title\":\"○  XRP - USDT\",\"coinType\":\"XRP\",\"currencyType\":\"USDT\"},{\"title\":\"○  OMG - USDT\",\"coinType\":\"OMG\",\"currencyType\":\"USDT\"},{\"title\":\"○  DASH - USDT\",\"coinType\":\"DASH\",\"currencyType\":\"USDT\"},{\"title\":\"○  ZEC - USDT\",\"coinType\":\"ZEC\",\"currencyType\":\"USDT\"},{\"title\":\"○  HT - USDT\",\"coinType\":\"HT\",\"currencyType\":\"USDT\"},{\"title\":\"○  IOST - USDT\",\"coinType\":\"IOST\",\"currencyType\":\"USDT\"},{\"title\":\"○  NEO - USDT\",\"coinType\":\"NEO\",\"currencyType\":\"USDT\"},{\"title\":\"○  HSR - USDT\",\"coinType\":\"HSR\",\"currencyType\":\"USDT\"},{\"title\":\"○  QTUM - USDT\",\"coinType\":\"QTUM\",\"currencyType\":\"USDT\"},{\"title\":\"○  XEM - USDT\",\"coinType\":\"XEM\",\"currencyType\":\"USDT\"},{\"title\":\"○  TRX - USDT\",\"coinType\":\"TRX\",\"currencyType\":\"USDT\"},{\"title\":\"○  SMT - USDT\",\"coinType\":\"SMT\",\"currencyType\":\"USDT\"},{\"title\":\"○  VEN - USDT\",\"coinType\":\"VEN\",\"currencyType\":\"USDT\"},{\"title\":\"○  ELF - USDT\",\"coinType\":\"ELF\",\"currencyType\":\"USDT\"},{\"title\":\"○  SNT - USDT\",\"coinType\":\"SNT\",\"currencyType\":\"USDT\"},{\"title\":\"○  NAS - USDT\",\"coinType\":\"NAS\",\"currencyType\":\"USDT\"},{\"title\":\"○  GNT - USDT\",\"coinType\":\"GNT\",\"currencyType\":\"USDT\"}]}]";


        ArrayList<CoinMenu> total = null;
        try {
            total = ModelUtils.toObject(jsonListList, new TypeToken<ArrayList<CoinMenu>>(){});
        } catch (Exception initiateParseJsonException) {
            Log.e("Raych", "There is an exception with parsing json in mockData()");
        }
        return total;
    }

}
