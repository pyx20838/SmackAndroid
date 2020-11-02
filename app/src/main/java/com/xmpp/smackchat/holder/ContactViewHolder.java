package com.xmpp.smackchat.holder;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xmpp.smackchat.Constant;
import com.xmpp.smackchat.R;
import com.xmpp.smackchat.base.views.recycler.BaseRecyclerViewHolder;
import com.xmpp.smackchat.base.views.recycler.RecyclerActionListener;
import com.xmpp.smackchat.base.views.recycler.RecyclerData;
import com.xmpp.smackchat.model.Contact;

import java.util.Random;

public class ContactViewHolder extends BaseRecyclerViewHolder {

    private TextView tvName;
    private View iconStatus;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);
        iconStatus = itemView.findViewById(R.id.iconStatus);
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if (data instanceof Contact) {
            Contact contact = (Contact) data;
            tvName.setText(contact.getName());
            setVisibility(new Random().nextBoolean());
        }
    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        itemView.setOnClickListener(view -> {
            if (actionListener != null) {
                actionListener.onViewClick(getAdapterPosition(), itemView, ContactViewHolder.this);
            }
        });
    }

    private void setVisibility(boolean available) {
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.OVAL);
        background.setColor(available ? Color.GREEN : Color.GRAY);
        iconStatus.setBackground(background);
    }
}
