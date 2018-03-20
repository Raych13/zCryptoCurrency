package com.example.jingze.zcryptocurrency.view.market_list;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
    private static int priceGreen;
    private static int priceGrey;
    private static int priceRed;
    private static Drawable goesUp;
    private static Drawable goesDown;
    private static Drawable goesFlat;
    private static final int ITEMS_PER_PAGE = 13;

    public MarketListAdapter(@NonNull MarketListFragment marketListFragment, @NonNull List data, @NonNull LoadMoreListener loadMoreListener) {
        super(marketListFragment.getContext(), data, loadMoreListener, data.size() > ITEMS_PER_PAGE);
        this.marketListFragment = marketListFragment;
        if (goesUp == null) {
            priceGreen = getContext().getResources().getColor(R.color.price_green);
            priceGrey = getContext().getResources().getColor(R.color.price_grey);
            priceRed = getContext().getResources().getColor(R.color.price_red);
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

        String title = coin.coinType + " - " + coin.currencyType;
        String price = CoinUtils.priceWithCurrencySymbol(coin.priceUSD, coin.coinType);
        String changeRate = CoinUtils.convertToPercentage(coin.dailyChange);

        marketListViewHolder.item_title_txv.setText(title);
        marketListViewHolder.item_price_txv.setText(price);

        if (coin.dailyChange != null) {
            if (coin.dailyChange > 0.00) {
                marketListViewHolder.item_change_bg.setBackgroundColor(priceGreen);
                marketListViewHolder.item_changeSymbol_igv.setImageDrawable(goesUp);
                marketListViewHolder.item_changeRate_txv.setText(changeRate);
            } else if (coin.dailyChange < 0.00) {
                marketListViewHolder.item_change_bg.setBackgroundColor(priceRed);
                marketListViewHolder.item_changeSymbol_igv.setImageDrawable(goesDown);
                marketListViewHolder.item_changeRate_txv.setText(changeRate);
            } else {
                marketListViewHolder.item_change_bg.setBackgroundColor(priceGrey);
                marketListViewHolder.item_changeSymbol_igv.setImageDrawable(goesFlat);
                marketListViewHolder.item_changeRate_txv.setText(changeRate);
            }
        }
    }

}
