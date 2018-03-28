package com.example.jingze.zcryptocurrency.view.market_list;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import com.example.jingze.zcryptocurrency.net.BitfinexManager;
import com.example.jingze.zcryptocurrency.net.BourseActivityManager;
import com.example.jingze.zcryptocurrency.net.HuobiManager;
import com.example.jingze.zcryptocurrency.view.base.InfiniteAdapter;
import com.example.jingze.zcryptocurrency.view.base.SpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketListFragment extends Fragment{
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public final static String BUNDLE_KEY_STRING = "total_data";
    public static final int VIEW_UPDATE = 100;
    private CoinMenu coinMenu;
    private Looper dataThreadLooper;
    private Handler mainThreadHandler;
    private LinearLayoutManager layoutManager;
    private MarketListAdapter adapter;
    private BourseActivityManager bourseActivityManager;

    private Runnable loadMoreRunnable = new Runnable() {
        @Override
        public void run() {
            adapter.addOnePage();
            adapter.notifyDataSetChanged();
        }
    };

    private InfiniteAdapter.LoadMoreListener onLoadMore = new InfiniteAdapter.LoadMoreListener(){
        @Override
        public void onLoadMore() {
            mainThreadHandler.removeCallbacks(loadMoreRunnable);
            mainThreadHandler.postDelayed(loadMoreRunnable, 1300);
        }
    };

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

        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_medium),
                getResources().getDimensionPixelSize(R.dimen.spacing_small)));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mainThreadHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                return mainThreadHandlerLogic(message);
            }
        });
        swipeRefreshLayout.setEnabled(false);
        coinMenu = getArguments().getParcelable(BUNDLE_KEY_STRING);
        startupBourseActivityManager(coinMenu);
//        swipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//        });
        ArrayList<Coin> coinList = coinMenu.getData();
        adapter = new MarketListAdapter(this, coinList, onLoadMore);
        setCoinMenuOnChangeListener();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("RaychTest", "onStop() is called.");
        bourseActivityManager.stop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("RaychTest", "onDetach() is called.");
    }

    public void setDataThreadLooper(Looper dataThreadLooper) {
        this.dataThreadLooper = dataThreadLooper;
    }

    private void startupBourseActivityManager(CoinMenu coinMenu) {
        switch (coinMenu.getName()) {
            case "Bitfinex": {
                bourseActivityManager = new BitfinexManager(getContext(), dataThreadLooper, coinMenu);
                bourseActivityManager.startConnection();
                break;
            }
            case "Huobi": {
                bourseActivityManager = new HuobiManager(getContext(), dataThreadLooper, coinMenu);
                bourseActivityManager.startConnection();
                break;
            }
            default: {
                Log.e("Raych",
                        "MarketListFragment.startupBourseActivityManager() failed to create BourseActivityManager");
            }
        }

    }

    private void setCoinMenuOnChangeListener() {
        coinMenu.setOnChangeListener(new CoinMenu.OnChangeListener() {
            @Override
            public void onCoinChanged(final int position) {
//                Log.i("RaychTest", "onCoinChanged() is reCalled");
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        int firstPosition = layoutManager.findFirstVisibleItemPosition();
                        int lastPosition = layoutManager.findLastVisibleItemPosition();
                        //Only update the item that is visible, and only when the screen is stop.
                        if (position >= firstPosition && position <= lastPosition) {
                            adapter.notifyItemChanged(position);
                        }
                    }
                });
            }

            @Override
            public void onDataOrderChanged() {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setOnScroll(RecyclerView recyclerView) {
        recyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

            }
        });
    }

    private boolean mainThreadHandlerLogic(Message message) {
        return false;
    }
}
