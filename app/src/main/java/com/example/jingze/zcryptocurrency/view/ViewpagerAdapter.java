package com.example.jingze.zcryptocurrency.view;

import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jingze.zcryptocurrency.R;
import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.view.market_list.MarketListFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ViewpagerAdapter extends FragmentPagerAdapter{
    private TabLayout tabLayout;
    private Handler dataThreadHandler;
    private ArrayList<CoinMenu> mainMenu;

    public ViewpagerAdapter(FragmentManager fm,
                            TabLayout tabLayout,
                            Handler dataThreadHandler,
                            ArrayList<CoinMenu> mainMenu
                            ) {
        super(fm);
        this.tabLayout = tabLayout;
        this.dataThreadHandler = dataThreadHandler;
        this.mainMenu = mainMenu;
    }

    @Override
    public Fragment getItem(int pagePosition) {
        CoinMenu coinMenu = mainMenu.get(pagePosition);
        MarketListFragment newFragment = MarketListFragment.getInstance(coinMenu);
        newFragment.setDataThreadLooper(dataThreadHandler.getLooper());
        return (Fragment) newFragment;
    }

    @Override
    public int getCount() {
        return mainMenu.size();
    }

    public void setupTabLayout(ArrayList<CoinMenu> mainMenu) {
        tabLayout.setBackgroundColor(
                tabLayout.getContext().getResources().getColor(R.color.main_bg));
        tabLayout.setSelectedTabIndicatorColor(
                tabLayout.getContext().getResources().getColor(R.color.golden));
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_favorite_white_24dp);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_bitfinexlogo);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_huobilogo);
    }
}
