package com.example.jingze.zcryptocurrency.view.market_list;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jingze.zcryptocurrency.R;
import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.net.BitfinexManager;
import com.example.jingze.zcryptocurrency.net.base.BourseActivityManager;
import com.example.jingze.zcryptocurrency.net.HuobiManager;
import com.example.jingze.zcryptocurrency.net.handler.UIHandler;
import com.example.jingze.zcryptocurrency.utils.RunningTimeUtils;
import com.example.jingze.zcryptocurrency.view.base.InfiniteAdapter;
import com.example.jingze.zcryptocurrency.view.base.SpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketListFragment extends Fragment{
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public final static String BUNDLE_KEY_STRING = "menu";
    public static ViewPager viewPager;

    private CoinMenu coinMenu;
    private UIHandler mainThreadHandler;
    private Handler dataThreadHandler;
    private MarketListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private BourseActivityManager bourseActivityManager;

    private int currentPagePosition;
    private int viewPagerScrollState = 0;
    private int pagePosition;
    private int recyclerViewScrollStart = 0;
    private boolean hasStarted = false;
    private View view;

    private InfiniteAdapter.LoadMoreListener onLoadMore = new InfiniteAdapter.LoadMoreListener(){
        @Override
        public void onLoadMore() {
//            swipeRefreshLayout.setEnabled(true);
            mainThreadHandler.removeMessages(UIHandler.ONLOADMORE_UPDATE);
            mainThreadHandler.sendLoadmoreMessage();
        }
    };

    public static MarketListFragment getInstance(CoinMenu coinMenu) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_STRING, coinMenu);

        MarketListFragment fragment = new MarketListFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        Log.i("RaychTest", this.toString() + ":\t" + " onAttach()");
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        Log.i("RaychTest", this.toString() + ":\t" + " onCreate() The savedInstanceState is null? " + (savedInstanceState == null));
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        Log.i("RaychTest", this.toString() + ":\t" + " onCreateView() The savedInstanceState is null? " + (savedInstanceState == null));
        if (container == null) {
            Log.e("Raych", "Position: " + this + ".\t contain is null.");
            return view;
        }
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_swipe_recycler_view, container, false);
            ButterKnife.bind(this, view);

            layoutManager = new LinearLayoutManager(container.getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                    getResources().getDimensionPixelSize(R.dimen.spacing_small)));
            hasStarted = false;
        }
        String fragmentTag = this.getTag();
        pagePosition = Integer.parseInt(fragmentTag.substring(fragmentTag.length() - 1));

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        Log.i("RaychTest", this.toString() + ":\t" + " onViewCreated() The savedInstanceState is null? " + (savedInstanceState == null));
        if (!hasStarted) {
            coinMenu = getArguments().getParcelable(BUNDLE_KEY_STRING);
            if (coinMenu != null) {
                ArrayList<Coin> coinList = coinMenu.getData();
                adapter = new MarketListAdapter(this, coinList, onLoadMore);
                recyclerView.setAdapter(adapter);
            } else {
                Log.e("Raych", "Position: " + this + ".\tUnable to load coinList");
            }
        }
        RunningTimeUtils.end("MainActivity start to Fragment onViewCreated() ends");
//        Log.i("RaychTest", this.toString() + ":\t" + " onViewCreated()");
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
////        Log.i("RaychTest", this.toString() + ":\t" + " onActivityCreated() The savedInstanceState is null? " + (savedInstanceState == null));
////        Log.i("RaychTest", this.toString() + ":\t" + " onActivityCreated()");
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
////        Log.i("RaychTest", this.toString() + ":\t" + " onViewStateRestored() The savedInstanceState is null? " + (savedInstanceState == null));
////        Log.i("RaychTest", this.toString() + ":\t" + " onViewStateRestored()");
//    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStart() {
        super.onStart();
//        Log.i("RaychTest", this.toString() + ":\t" + " onStart())");
        if (mainThreadHandler == null) {
            mainThreadHandler = new UIHandler(new UICallbackLogic());
        }
        if (!hasStarted) {
            setupBourseActivityManager(coinMenu);
            setCoinMenuOnChangeListener();
            setupSwipeRefreshLayout();
            setOnScrollListener(recyclerView);
            addOnPageChangeListener();

            hasStarted = true;
        }
        RunningTimeUtils.end("MainActivity start to onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.i("RaychTest", this.toString() + ":\t" + " onResume()");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.i("RaychTest", this.toString() + ":\t" + " onSaveInstanceState())");
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.i("RaychTest", this.toString() + ":\t" + " onPause()" + "\tVisible: " + isVisible());
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.i("RaychTest", this.toString() + ":\t" + " onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Log.i("RaychTest", this.toString() + ":\t" + " onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.i("RaychTest", this.toString() + ":\t" + " onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("RaychTest", this.toString() + ":\t" + " onDetach()");
        bourseActivityManager.stop();
        viewPager = null;
    }

    public void setDataThreadHandler(Handler dataThreadHandler) {
        this.dataThreadHandler = dataThreadHandler;
    }


    private void setupBourseActivityManager(final CoinMenu coinMenu) {
        final int DELAY = 1000;
        int setupDelay = (currentPagePosition == pagePosition ? 0 : DELAY);
        switch (coinMenu.getName()) {
            case "Bitfinex": {
                dataThreadHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bourseActivityManager = new BitfinexManager(dataThreadHandler.getLooper(), coinMenu);
                        bourseActivityManager.startConnection();
                    }
                }, setupDelay);
                break;
            }
            case "Huobi": {
                dataThreadHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bourseActivityManager = new HuobiManager(dataThreadHandler.getLooper(), coinMenu);
                        bourseActivityManager.startConnection();
                    }
                }, setupDelay);

                break;
            }
            default: {
                Log.e("Raych",
                        "MarketListFragment.startupBourseActivityManager() failed to create BourseActivityManager");
//                dataThreadHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, setupDelay);
            }
        }
    }

    private void setCoinMenuOnChangeListener() {
        coinMenu.setOnChangeListener(new CoinMenu.OnChangeListener() {
            @Override
            public void onCoinChanged(final int position) {
//                Log.i("RaychTest", "Position: " + position);
                //@param what The value indicates what the message is about.
                //@param arg1 The value indicates which position of the coin to be handled.
                //@param arg2 The value indicates how many times has this message been posted previously..
                if (pagePosition == currentPagePosition) {
                    mainThreadHandler.sendItemUpdateMessage(position, 0);
                }
            }

            @Override
            public void onDataOrderChanged() {

            }
        });
    }

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
//                        mainThreadHandler.sendPageUpdateMessageDelay(0, UIHandler.PAGE_UPDATE_DELAY);
                        mainThreadHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.resetPage();
                                swipeRefreshLayout.setRefreshing(false);
                                swipeRefreshLayout.setEnabled(true);
                            }
                        }, 1300);
                    }
                });
        swipeRefreshLayout.setEnabled(true);
    }

    private void setOnScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                recyclerViewScrollStart = newState;
            }
        });

//        recyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//
//            }
//        });
    }

    private void addOnPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPagePosition = position;
                if (pagePosition == position && hasStarted) {
                    mainThreadHandler.sendPageUpdateMessageDelay(0, UIHandler.PAGE_UPDATE_DELAY);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                viewPagerScrollState = state;
            }
        });
    }

    private class UICallbackLogic implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            //Message that has been resend more than 2 times will not be handled.
            int event = message.what;
            int attempts = message.arg2;

            if (event == UIHandler.ITEM_UPDATE && attempts < 3) {
                int preloadNumber = 5;
                final int position = message.arg1;

                if (recyclerViewScrollStart == 0) {             //IDLE: Not scrolling action.
                    final int firstPosition = layoutManager.findFirstVisibleItemPosition() - preloadNumber;
                    final int lastPosition = layoutManager.findLastVisibleItemPosition() + preloadNumber;
                    if (position < lastPosition  && position > firstPosition) {
                        adapter.notifyItemChanged(position, "PART");
                    }
                } else if (recyclerViewScrollStart == 1) {      //DRAGGING: is being dragged to scroll.
                    mainThreadHandler.sendItemUpdateMessageDelay(position, ++attempts, UIHandler.RV_DRAGGED_RESEND_DELAY * attempts);
                } else if (recyclerViewScrollStart == 2) {      //FLYING: is scrolling automatically.
                    mainThreadHandler.sendItemUpdateMessageDelay(position, ++attempts, UIHandler.RV_FLYING_RESEND_DELAY * attempts);
                }
            } else if (event == UIHandler.PAGE_UPDATE && currentPagePosition == pagePosition) {
                if (viewPagerScrollState == 0) {
                    adapter.notifyItemRangeChanged(0, adapter.getItemCount(), "PART");
                }else {
                    mainThreadHandler.sendPageUpdateMessageDelay(0, UIHandler.VP_DRAGGED_RESEND_DELAY);
                }
            } else if (event == UIHandler.ONLOADMORE_UPDATE) {
                adapter.addOnePage();
            }
            return false;
        }
    }
}
