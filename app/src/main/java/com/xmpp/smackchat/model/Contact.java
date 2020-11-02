package com.xmpp.smackchat.model;

import com.xmpp.smackchat.Constant;
import com.xmpp.smackchat.base.views.recycler.RecyclerData;
import com.xmpp.smackchat.base.views.recycler.RecyclerViewType;

import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.EntityBareJid;

public class Contact implements RecyclerData {

    private RosterEntry entry;

    private boolean isOnline;

    public Contact(RosterEntry entry) {
        this.entry = entry;
    }

    public RosterEntry getEntry() {
        return entry;
    }

    public String getName() {
        return entry.getJid().getLocalpartOrNull().toString();
    }

    public String getAddress() {
        return entry.getJid().toString();
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public int getViewType() {
        return RecyclerViewType.TYPE_CONTACT;
    }

    @Override
    public boolean areItemsTheSame(RecyclerData other) {
        if (other instanceof Contact) {
            Contact contact = (Contact) other;
            return contact.entry.getJid().equals(entry.getJid());
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(RecyclerData other) {
        if (other instanceof Contact) {
            Contact contact = (Contact) other;
            return contact.entry.getJid().equals(entry.getJid());
        }
        return false;
    }
}
