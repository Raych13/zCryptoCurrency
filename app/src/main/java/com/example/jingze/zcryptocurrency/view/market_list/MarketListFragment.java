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
    public static final int ITEM_UPDATE = 100;

    private static final int ONLOADMORE_DELAY = 1300;
    private static final int DRAGGED_RESEND_DELAY = 1500;
    private static final int FLYING_RESEND_DELAY = 800;
    private int scrollStart = 0;
    private CoinMenu coinMenu;
    private Looper dataThreadLooper;
    private Handler mainThreadHandler;
    private MarketListAdapter adapter;
    private LinearLayoutManager layoutManager;
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
            swipeRefreshLayout.setEnabled(true);
            mainThreadHandler.removeCallbacks(loadMoreRunnable);
            mainThreadHandler.postDelayed(loadMoreRunnable, ONLOADMORE_DELAY);
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
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
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
        setOnScrollListener(recyclerView);
        setupSwipeRefreshLayout();
        coinMenu = getArguments().getParcelable(BUNDLE_KEY_STRING);
        setupBourseActivityManager(coinMenu);

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

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        adapter.resetPage();
                        adapter.notifyDataSetChanged();
                        mainThreadHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setEnabled(false);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 1300);
                    }
                });
    }

    private void setupBourseActivityManager(CoinMenu coinMenu) {
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
                //@param what The value indicates what the message is about.
                //@param arg1 The value indicates which position of the coin to be handled.
                //@param arg2 The value indicates how many times has this message been posted previously..
                Message msg = mainThreadHandler.obtainMessage(ITEM_UPDATE, position, 0);
                mainThreadHandler.sendMessage(msg);
//                mainThreadHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        int firstPosition = layoutManager.findFirstVisibleItemPosition();
//                        int lastPosition = layoutManager.findLastVisibleItemPosition();
//                        //Only update the item that is visible, and only when the screen is stop.
//                        if (position <= lastPosition && position >= firstPosition) {
//                            adapter.notifyItemChanged(position);
//                        }
//                    }
//                });
            }

            @Override
            public void onDataOrderChanged() {

            }
        });
    }

    private void setOnScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scrollStart = newState;
            }
        });

//        recyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//
//            }
//        });
    }

    private boolean mainThreadHandlerLogic(Message message) {
        //Message that has been resend more than 2 times will not be handled.
        if (message.what ==ITEM_UPDATE && message.arg2 < 3) {
            final int position = message.arg1;
            if (scrollStart == 0) {             //IDLE: Not scrolling action.
                final int firstPosition = layoutManager.findFirstVisibleItemPosition() - 1;
                final int lastPosition = layoutManager.findLastVisibleItemPosition() + 1;
                if (position <= lastPosition && position >= firstPosition) {
                    adapter.notifyItemChanged(position);
                }
            } else if (scrollStart == 1) {      //DRAGGING: is being dragged to scroll.
                int resendTimes = ++message.arg2;
                Message msg = mainThreadHandler.obtainMessage(ITEM_UPDATE, position, resendTimes);
                mainThreadHandler.sendMessageDelayed(msg, DRAGGED_RESEND_DELAY * resendTimes);
            } else if (scrollStart == 2) {      //FLYING: is scrolling automatically.
                int resendTimes = ++message.arg2;
                Message msg = mainThreadHandler.obtainMessage(ITEM_UPDATE, position, resendTimes);
                mainThreadHandler.sendMessageDelayed(msg, FLYING_RESEND_DELAY * resendTimes);
            }
        }
        return false;
    }
}
