package com.xmpp.smackchat.base.views.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import com.xmpp.smackchat.R;
import com.xmpp.smackchat.holder.ContactViewHolder;
import com.xmpp.smackchat.holder.MessageViewHolder;

public class ViewHolderFactory {

    public static BaseRecyclerViewHolder createViewHolder(@RecyclerViewType int viewType, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        switch (viewType) {
            case RecyclerViewType.TYPE_INVALID:
                return new InvalidViewHolder(view);
            case RecyclerViewType.TYPE_CONTACT:
                return new ContactViewHolder(view);
            case RecyclerViewType.TYPE_INCOMING_MESSAGE:
            case RecyclerViewType.TYPE_OUTGOING_MESSAGE:
                return new MessageViewHolder(view);
        }
        return new InvalidViewHolder(view);
    }

    @LayoutRes
    private static int getLayoutId(int viewType) {
        switch (viewType) {
            case RecyclerViewType.TYPE_INVALID:
                return 0;
            case RecyclerViewType.TYPE_CONTACT:
                return R.layout.holder_contact;
            case RecyclerViewType.TYPE_INCOMING_MESSAGE:
                return R.layout.holder_incoming_message;
            case RecyclerViewType.TYPE_OUTGOING_MESSAGE:
                return R.layout.holder_outgoing_message;
        }
        return 0;
    }

}
