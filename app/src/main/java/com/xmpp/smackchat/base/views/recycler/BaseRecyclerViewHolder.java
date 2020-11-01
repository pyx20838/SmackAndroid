package com.xmpp.smackchat.base.views.recycler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindViewHolder(RecyclerData data);
    public abstract void setupClickableViews(RecyclerActionListener actionListener);
}
