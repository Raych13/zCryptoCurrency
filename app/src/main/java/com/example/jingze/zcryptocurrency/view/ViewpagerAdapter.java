package com.example.jingze.zcryptocurrency.view;

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

    private ArrayList<CoinMenu> mainMenu;
    private TabLayout tabLayout;

    public ViewpagerAdapter(FragmentManager fm,
                            ArrayList<CoinMenu> mainMenu,
                            TabLayout tabLayout) {
        super(fm);
        this.mainMenu = mainMenu;
        this.tabLayout = tabLayout;
    }

    @Override
    public Fragment getItem(int pagePosition) {
        CoinMenu subMenu = mainMenu.get(pagePosition);
        return (Fragment) MarketListFragment.getInstance(subMenu);
    }

    @Override
    public int getCount() {
        return mainMenu.size();
    }

    

    public void setupTabLayout() {
        tabLayout.setBackgroundColor(
                tabLayout.getContext().getResources().getColor(R.color.main_bg));
        tabLayout.setSelectedTabIndicatorColor(
                tabLayout.getContext().getResources().getColor(R.color.golden));
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_favorite_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_bitfinexlogo);
    }
}
