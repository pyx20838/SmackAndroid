package com.xmpp.smackchat.model;

import com.xmpp.smackchat.base.views.recycler.RecyclerData;

import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

public class ChatMessage implements RecyclerData {

    private int viewType;
    private EntityBareJid jid;
    private Message message;

    public ChatMessage(int viewType, EntityBareJid jid, Message message) {
        this.viewType = viewType;
        this.message = message;
        this.jid = jid;
    }

    public EntityBareJid getJid() {
        return jid;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public int getViewType() {
        return viewType;
    }

    @Override
    public boolean areItemsTheSame(RecyclerData other) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(RecyclerData other) {
        return false;
    }
}
