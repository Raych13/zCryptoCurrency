package com.example.jingze.zcryptocurrency.view.market_list;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jingze.zcryptocurrency.R;
import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.utils.CoinUtils;
import com.example.jingze.zcryptocurrency.view.base.BaseViewHolder;
import com.example.jingze.zcryptocurrency.view.base.InfiniteAdapter;

import java.util.List;

public class MarketListAdapter extends InfiniteAdapter<Coin>{

    private final MarketListFragment marketListFragment; //Leave for Starting new Activity
    private static Drawable priceGreen;
    private static Drawable priceGrey;
    private static Drawable priceRed;
    private static Drawable goesUp;
    private static Drawable goesDown;
    private static Drawable goesFlat;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MarketListAdapter(@NonNull MarketListFragment marketListFragment, @NonNull List data, @NonNull LoadMoreListener loadMoreListener) {
        super(marketListFragment.getContext(), data, loadMoreListener);
        this.marketListFragment = marketListFragment;
        if (goesUp == null) {
            priceGreen = getContext().getResources().getDrawable(R.drawable.item_roundframe_green, null);
            priceGrey = getContext().getResources().getDrawable(R.drawable.item_roundframe_grey, null);
            priceRed = getContext().getResources().getDrawable(R.drawable.item_roundframe_red, null);
            goesUp = getContext().getResources().getDrawable(R.drawable.ic_trending_up_white_24dp);
            goesDown = getContext().getResources().getDrawable(R.drawable.ic_trending_down_white_24dp);
            goesFlat = getContext().getResources().getDrawable(R.drawable.ic_trending_flat_white_24dp);
        }
    }

    @Override
    protected BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.marketlist_item, parent, false);
        return new MarketListViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(BaseViewHolder holder, int position) {
        //This part is to setText for each item
        MarketListViewHolder marketListViewHolder = (MarketListViewHolder) holder;
        final Coin coin = getData().get(position);

//        String title = "○  " + coin.getCoinType() + " - "+ coin.getCurrencyType();
        String price = CoinUtils.priceWithCurrencySymbol(coin.getPrice(), coin.getCurrencyType());
        String changeRate = CoinUtils.convertToPercentage(coin.getDailyChangeRate());

        marketListViewHolder.item_title_txv.setText(coin.getTitle());
        marketListViewHolder.item_price_txv.setText(price);

        if (coin.getDailyChangeRate() != null) {
            if (coin.getDailyChangeRate() > 0.00) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    marketListViewHolder.item_change_bg.setBackground(priceGreen);
                } else {
                    marketListViewHolder.item_change_bg.setBackgroundColor(getContext().getResources().getColor(R.color.price_green));
                }
                marketListViewHolder.item_changeSymbol_igv.setImageDrawable(goesUp);
                marketListViewHolder.item_changeRate_txv.setText(changeRate);
            } else if (coin.getDailyChangeRate() < 0.00) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    marketListViewHolder.item_change_bg.setBackground(priceRed);
                } else {
                    marketListViewHolder.item_change_bg.setBackgroundColor(getContext().getResources().getColor(R.color.price_red));
                }
                marketListViewHolder.item_changeSymbol_igv.setImageDrawable(goesDown);
                marketListViewHolder.item_changeRate_txv.setText(changeRate);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    marketListViewHolder.item_change_bg.setBackground(priceGrey);
                } else {
                    marketListViewHolder.item_change_bg.setBackgroundColor(getContext().getResources().getColor(R.color.price_grey));
                }
                marketListViewHolder.item_changeSymbol_igv.setImageDrawable(goesFlat);
                marketListViewHolder.item_changeRate_txv.setText(changeRate);
            }
        }
    }
}
