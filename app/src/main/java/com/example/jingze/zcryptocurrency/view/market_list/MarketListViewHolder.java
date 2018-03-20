package com.example.jingze.zcryptocurrency.view.market_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jingze.zcryptocurrency.R;
import com.example.jingze.zcryptocurrency.view.base.BaseViewHolder;

import butterknife.BindView;

public class MarketListViewHolder extends BaseViewHolder{
    @BindView(R.id.marketList_item_title_txv) TextView item_title_txv;
    @BindView(R.id.marketList_item_price_txv) TextView item_price_txv;
    @BindView(R.id.marketList_item_change_bg) LinearLayout item_change_bg;
    @BindView(R.id.marketList_item_changeSymbol_igv) ImageView item_changeSymbol_igv;
    @BindView(R.id.marketList_item_changeRate_txv) TextView item_changeRate_txv;

    public MarketListViewHolder(View itemView) {
        super(itemView);
    }
}
