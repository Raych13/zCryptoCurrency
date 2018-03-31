package com.example.jingze.zcryptocurrency.view.market_list;

import android.graphics.drawable.Drawable;
import android.os.Build;
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
    private static Drawable priceGreen;
    private static Drawable priceGrey;
    private static Drawable priceRed;
    private static Drawable goesUp;
    private static Drawable goesDown;
    private static Drawable goesFlat;
    private static Integer priceGreenColor;
    private static Integer priceGreyColor;
    private static Integer priceRedColor;
    private final static String NOT_AVAILABLE = "N / A    ";

    public MarketListAdapter(@NonNull MarketListFragment marketListFragment, @NonNull List data, @NonNull LoadMoreListener loadMoreListener) {
        super(marketListFragment.getContext(), data, loadMoreListener);
        this.marketListFragment = marketListFragment;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (priceGreen == null) {
                priceGreen = getContext().getResources().getDrawable(R.drawable.item_roundframe_green, null);
                priceGrey = getContext().getResources().getDrawable(R.drawable.item_roundframe_grey, null);
                priceRed = getContext().getResources().getDrawable(R.drawable.item_roundframe_red, null);
            }
        }
        if (priceGreenColor == null) {
            priceGreenColor = getContext().getResources().getColor(R.color.price_green);
            priceGreyColor = getContext().getResources().getColor(R.color.price_grey);
            priceRedColor = getContext().getResources().getColor(R.color.price_red);
        }
        goesUp = getContext().getResources().getDrawable(R.drawable.ic_trending_up_white_24dp);
        goesDown = getContext().getResources().getDrawable(R.drawable.ic_trending_down_white_24dp);
        goesFlat = getContext().getResources().getDrawable(R.drawable.ic_trending_flat_white_24dp);
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

        if (marketListViewHolder.item_title_txv.getTag() == null || marketListViewHolder.item_title_txv.getTag() != coin.getTitle()){
            marketListViewHolder.item_title_txv.setText(coin.getTitle());
            marketListViewHolder.item_title_txv.setTag(coin.getTitle());
        }
        marketListViewHolder.item_price_txv.setText(price);

        if (coin.getDailyChangeRate() != null) {
            if (coin.getDailyChangeRate() > 0.00) {
                setBackgroundColorAndChangeSymbol(marketListViewHolder, priceGreen, priceGreenColor, goesUp, true);
                marketListViewHolder.item_changeRate_txv.setText(changeRate);
            } else if (coin.getDailyChangeRate() < 0.00) {
                setBackgroundColorAndChangeSymbol(marketListViewHolder, priceRed, priceRedColor, goesDown, true);
                marketListViewHolder.item_changeRate_txv.setText(changeRate);
            } else {
                setBackgroundColorAndChangeSymbol(marketListViewHolder, priceGrey, priceGreyColor, goesFlat, true);
                marketListViewHolder.item_changeRate_txv.setText(changeRate);
            }
        } else {
            setBackgroundColorAndChangeSymbol(marketListViewHolder, priceGrey, priceGreyColor, goesFlat, false);
            marketListViewHolder.item_changeRate_txv.setText(NOT_AVAILABLE);
        }
    }

    @Override
    protected void onBindItemViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        onBindItemViewHolder(holder, position);
    }

    private void setBackgroundColorAndChangeSymbol(MarketListViewHolder marketListViewHolder, Drawable newBG, int newColor, Drawable newSymbol, boolean showSymbol) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (marketListViewHolder.item_change_bg.getTag() == null || marketListViewHolder.item_change_bg.getTag() != newBG) {
                marketListViewHolder.item_change_bg.setBackground(newBG);
                marketListViewHolder.item_change_bg.setTag(newBG);
            }
        } else {
            marketListViewHolder.item_change_bg.setBackgroundColor(newColor);
        }
        if (showSymbol) {
            marketListViewHolder.item_changeSymbol_igv.setVisibility(View.VISIBLE);
            if (marketListViewHolder.item_changeSymbol_igv.getTag() == null || marketListViewHolder.item_changeSymbol_igv.getTag() != newSymbol) {
                marketListViewHolder.item_changeSymbol_igv.setImageDrawable(newSymbol);
                marketListViewHolder.item_changeSymbol_igv.setTag(newSymbol);
            }
        } else {
            marketListViewHolder.item_changeSymbol_igv.setVisibility(View.GONE);
        }
    }
}
