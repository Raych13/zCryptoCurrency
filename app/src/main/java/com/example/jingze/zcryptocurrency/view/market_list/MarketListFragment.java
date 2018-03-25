package com.example.jingze.zcryptocurrency.view.market_list;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jingze.zcryptocurrency.R;
import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.utils.DataManager;
import com.example.jingze.zcryptocurrency.view.base.InfiniteAdapter;
import com.example.jingze.zcryptocurrency.view.base.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketListFragment extends Fragment{
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public final static String BUNDLE_KEY_STRING = "total_data";

    private Looper dataThreadLooper;
    private MarketListAdapter adapter;

    public static MarketListFragment getInstance(CoinMenu coinMenu) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_STRING, coinMenu);

        MarketListFragment fragment = new MarketListFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_recycler_view, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout.setEnabled(false);
        CoinMenu coinMenu = getArguments().getParcelable(BUNDLE_KEY_STRING);
        ArrayList<Coin> subMenu = coinMenu.getData();
//        swipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//        });

        adapter = new MarketListAdapter(this, subMenu, new InfiniteAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
            }
        });
        //Add New task
        DataManager dataManager = new DataManager(getContext(), dataThreadLooper, adapter, coinMenu);
        dataManager.connectToWeb();
        coinMenu.setOnChangeListener(new CoinMenu.OnChangeListener() {
            @Override
            public void onCoinChanged(final int position) {
                Log.i("RaychTest", "onCoinChanged() is reCalled");
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemChanged(position);
                    }
                });
            }

            @Override
            public void onDataOrderChanged() {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void setDataThreadLooper(Looper dataThreadLooper) {
        this.dataThreadLooper = dataThreadLooper;
    }

}
