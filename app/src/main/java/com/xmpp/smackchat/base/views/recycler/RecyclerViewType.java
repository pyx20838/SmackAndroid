package com.xmpp.smackchat.base.views.recycler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface RecyclerViewType {
    int TYPE_INVALID = 0;
    int TYPE_CONTACT = 1;
    int TYPE_INCOMING_MESSAGE = 2;
    int TYPE_OUTGOING_MESSAGE = 3;
}
