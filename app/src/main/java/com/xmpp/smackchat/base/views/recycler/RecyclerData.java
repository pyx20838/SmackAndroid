package com.xmpp.smackchat.base.views.recycler;

public interface RecyclerData {

    @RecyclerViewType
    int getViewType();
    boolean areItemsTheSame(RecyclerData other);
    boolean areContentsTheSame(RecyclerData other);
}
