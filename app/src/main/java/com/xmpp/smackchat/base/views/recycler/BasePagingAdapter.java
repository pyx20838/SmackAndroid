package com.xmpp.smackchat.base.views.recycler;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;

public class BasePagingAdapter<T extends RecyclerData> extends PagedListAdapter<T, BaseRecyclerViewHolder> {

    private RecyclerActionListener actionListener;

    public BasePagingAdapter(RecyclerActionListener actionListener) {
        super(new BaseDiffItemCallback<T>());
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @RecyclerViewType int viewType) {
        BaseRecyclerViewHolder viewHolder = ViewHolderFactory.createViewHolder(viewType, parent);
        viewHolder.setupClickableViews(actionListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder vh, int position) {
        RecyclerData item = getItem(position);
        vh.bindViewHolder(item);
    }

    @Override
    public int getItemViewType(int position) {
        T item = getItem(position);
        return item != null ? item.getViewType() : RecyclerViewType.TYPE_INVALID;
    }

    public void setActionListener(RecyclerActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public T getItem(int position) {
        return super.getItem(position);
    }
}
