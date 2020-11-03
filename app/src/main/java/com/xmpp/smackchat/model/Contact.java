package com.xmpp.smackchat.model;

import android.text.TextUtils;

import com.xmpp.smackchat.base.views.recycler.RecyclerData;
import com.xmpp.smackchat.base.views.recycler.RecyclerViewType;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;

public class Contact implements RecyclerData {

    private final RosterEntry entry;
    private Presence presence;

    public Contact(RosterEntry entry, Presence presence) {
        this.entry = entry;
        this.presence = presence;
    }

    public RosterEntry getEntry() {
        return entry;
    }

    public String getName() {
        String name = entry.getName();
        return TextUtils.isEmpty(name) ? entry.getJid().getLocalpartOrNull().toString() : name;
    }

    public String getAddress() {
        return entry.getJid().toString();
    }

    public boolean isAvailable() {
        return presence.getMode() == Presence.Mode.available && presence.isAvailable();
    }

    public boolean isBusy() {
        return presence.getMode() == Presence.Mode.dnd && presence.isAvailable();
    }

    public boolean isAway() {
        return presence.getMode() == Presence.Mode.away && presence.isAvailable();
    }

    public void setPresence(Presence presence) {
        this.presence = presence;
    }

    @Override
    public int getViewType() {
        return RecyclerViewType.TYPE_CONTACT;
    }

    @Override
    public boolean areItemsTheSame(RecyclerData other) {
        if (other instanceof Contact) {
            Contact contact = (Contact) other;
            return contact.entry.getJid().equals(entry.getJid())
                    && contact.presence.getMode() == presence.getMode()
                    && contact.presence.getType() == presence.getType();
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(RecyclerData other) {
        if (other instanceof Contact) {
            Contact contact = (Contact) other;
            return contact.entry.getJid().equals(entry.getJid())
                    && contact.presence.getMode() == presence.getMode()
                    && contact.presence.getType() == presence.getType();
        }
        return false;
    }
}
