package com.xmpp.smackchat.holder;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xmpp.smackchat.R;
import com.xmpp.smackchat.base.views.recycler.BaseRecyclerViewHolder;
import com.xmpp.smackchat.base.views.recycler.RecyclerActionListener;
import com.xmpp.smackchat.base.views.recycler.RecyclerData;
import com.xmpp.smackchat.model.Contact;

public class ContactViewHolder extends BaseRecyclerViewHolder {

    private final TextView tvName;
    private final View iconStatus;

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
            setVisibility(contact);
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

    private void setVisibility(Contact contact) {
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.OVAL);
        background.setColor(getColorByStatus(contact));
        iconStatus.setBackground(background);
    }

    private int getColorByStatus(Contact contact) {
        if (contact.isAvailable()) return Color.parseColor("#2EB67D");
        else if (contact.isBusy()) return Color.parseColor("#C2185B");
        else if (contact.isAway()) return Color.parseColor("#D69209");
        else return Color.GRAY;
    }
}
