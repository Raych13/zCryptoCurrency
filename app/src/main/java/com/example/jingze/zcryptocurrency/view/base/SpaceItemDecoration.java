package com.example.jingze.zcryptocurrency.view.base;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space_horizontal;
    private int space_vertical;

    public SpaceItemDecoration(int space_vertical, int space_horizontal) {
        this.space_vertical = space_vertical;
        this.space_horizontal = space_horizontal;
    }

    public SpaceItemDecoration() {
        super();

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space_horizontal;
        outRect.right = space_horizontal;
        outRect.bottom = space_vertical;


        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space_horizontal;
        }
    }
}
