package com.xmpp.smackchat.base.views.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;

public class ViewHolderFactory {

    public static BaseRecyclerViewHolder createViewHolder(@RecyclerViewType int viewType, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        switch (viewType) {
            case RecyclerViewType.TYPE_INVALID:
                return new InvalidViewHolder(view);
        }
        return new InvalidViewHolder(view);
    }

    @LayoutRes
    private static int getLayoutId(int viewType) {
        switch (viewType) {
            case RecyclerViewType.TYPE_INVALID:
                return 0;
        }
        return 0;
    }

}
