package com.xmpp.smackchat.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xmpp.smackchat.R;
import com.xmpp.smackchat.base.views.recycler.BaseRecyclerViewHolder;
import com.xmpp.smackchat.base.views.recycler.RecyclerActionListener;
import com.xmpp.smackchat.base.views.recycler.RecyclerData;
import com.xmpp.smackchat.model.ChatMessage;

public class MessageViewHolder extends BaseRecyclerViewHolder {

    private TextView tvMessage;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMessage = itemView.findViewById(R.id.tvMessage);
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if (data instanceof ChatMessage) {
            ChatMessage message = (ChatMessage) data;
            tvMessage.setText(message.getMessage().getBody());
        }
    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {

    }
}
