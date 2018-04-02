package com.example.jingze.zcryptocurrency.view.base;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Jingze HUANG on Apr.01, 2018.
 */

public class RecyclerViewAnimator extends DefaultItemAnimator{
    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
        return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY);
    }
}
