package com.example.jingze.zcryptocurrency.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.view.market_list.MarketListFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ViewpagerAdapter extends FragmentPagerAdapter{

    private ArrayList<ArrayList<Coin>> mainMenu;

    public ViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ViewpagerAdapter(FragmentManager fm, ArrayList<ArrayList<Coin>> mainMenu) {
        super(fm);
        this.mainMenu = mainMenu;
    }

    @Override
    public Fragment getItem(int position) {
        ArrayList<Coin> subMenu = mainMenu.get(position);
        return (Fragment) MarketListFragment.getInstance(subMenu);
    }

    @Override
    public int getCount() {
        return mainMenu.size();
    }
}
