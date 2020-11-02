package com.xmpp.smackchat.model;

import com.xmpp.smackchat.base.views.recycler.RecyclerData;
import com.xmpp.smackchat.base.views.recycler.RecyclerViewType;

import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.EntityBareJid;

public class Contact implements RecyclerData {

    private RosterEntry entry;

    public Contact(RosterEntry entry) {
        this.entry = entry;
    }

    public RosterEntry getEntry() {
        return entry;
    }

    public String getName() {
        return entry.getJid().getLocalpartOrNull().toString();
    }

    @Override
    public int getViewType() {
        return RecyclerViewType.TYPE_CONTACT;
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
