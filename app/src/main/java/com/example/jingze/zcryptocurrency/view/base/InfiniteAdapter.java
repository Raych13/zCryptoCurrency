package com.example.jingze.zcryptocurrency.view.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jingze.zcryptocurrency.R;

import java.util.List;

public abstract class InfiniteAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEWTYPE_LOADING = 0;
    private static final int VIEWTYPE_ITEM = 1;

    private List<T> data;
    private boolean hasMoreData;

    private final Context context;
    private final LoadMoreListener loadMoreListener;

    public interface LoadMoreListener {
        void onLoadMore();
    }

    public InfiniteAdapter(@NonNull Context context,
                           @NonNull List<T> data,
                           @NonNull LoadMoreListener loadMoreListener,
                           boolean hasMoreData) {
        this.context = context;
        this.data = data;
        this.loadMoreListener = loadMoreListener;
        this.hasMoreData = hasMoreData;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEWTYPE_LOADING: {
                View view = LayoutInflater.from(context).inflate(R.layout.list_item_loading, parent, false);
                return new BaseViewHolder(view);
            }
            case VIEWTYPE_ITEM: {
                return onCreateItemViewHolder(parent, viewType);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (getItemViewType(position) == VIEWTYPE_ITEM) {
            onBindItemViewHolder(holder, position);
        } else {
            loadMoreListener.onLoadMore();
        }
    }

    @Override
    public int getItemCount() {
        return hasMoreData ? (data.size() + 1) : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (hasMoreData) {
            return position == data.size() ? VIEWTYPE_LOADING : VIEWTYPE_ITEM;
        } else {
            return VIEWTYPE_ITEM;
        }
    }

    public void append(@NonNull List<T> moredata) {
        data.addAll(moredata);
        notifyDataSetChanged();
    }

    public void prepend(@NonNull List<T> moredata) {
        data.addAll(0, moredata);
        notifyDataSetChanged();
    }

    public void setData(@NonNull List<T> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }

    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }

    protected Context getContext() {
        return context;
    }

    protected abstract BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindItemViewHolder(BaseViewHolder holder, int position);

}
