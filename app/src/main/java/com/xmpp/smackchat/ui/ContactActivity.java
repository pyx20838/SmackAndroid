package com.xmpp.smackchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialDialogs;
import com.xmpp.smackchat.Constant;
import com.xmpp.smackchat.R;
import com.xmpp.smackchat.base.AppLog;
import com.xmpp.smackchat.base.views.recycler.BaseRecyclerAdapter;
import com.xmpp.smackchat.base.views.recycler.BaseRecyclerViewHolder;
import com.xmpp.smackchat.base.views.recycler.RecyclerActionListener;
import com.xmpp.smackchat.connection.SmackChat;
import com.xmpp.smackchat.model.Contact;
import com.xmpp.smackchat.model.User;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends XMPPActivity {

    private BaseRecyclerAdapter<Contact> contactAdapter;

    private final RecyclerActionListener contactListener = new RecyclerActionListener() {
        @Override
        public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
            Contact contact = contactAdapter.getData().get(position);
            if (bound) {
                chatService.setCurrentEntityBareJid(contact.getEntry().getJid().asEntityBareJidIfPossible());
            }

            Intent chatIntent = new Intent(ContactActivity.this, ChatActivity.class);
            startActivity(chatIntent);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        findViewById(R.id.btnLogout).setOnClickListener(this::handleLogout);
        findViewById(R.id.btnAddFriend).setOnClickListener(this::addFriend);

        RecyclerView rvContact = findViewById(R.id.rvContact);
        contactAdapter = new BaseRecyclerAdapter<>(contactListener);
        rvContact.setAdapter(contactAdapter);
    }

    private void addFriend(View view) {
        if (bound) {
            chatService.addFriend("tiendung001@" + Constant.DOMAIN, "Test 001");
        }
    }

    private void handleLogout(View view) {
        if (bound) {
            chatService.handleLogout();
        }
    }

    @Override
    public void onChatServiceConnected() {
        chatService.getUserInfo().observe(this, this::observeUserInfo);
        chatService.getConnectionState().observe(this, this::observeConnState);
        chatService.getRosterEntries().observe(this, this::observeContactList);
    }

    private void observeContactList(List<RosterEntry> rosterEntries) {
        AppLog.d("Roster : " + rosterEntries.size());
        List<Contact> contacts = new ArrayList<>();
        for (RosterEntry entry : rosterEntries) {
            contacts.add(new Contact(entry));
        }

        contactAdapter.update(contacts);
    }

    private void observeConnState(SmackChat.ConnectionState state) {
        if (state == SmackChat.ConnectionState.DISCONNECTED) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void observeUserInfo(User user) {
        TextView tvUsername = findViewById(R.id.tvUsername);
        tvUsername.setText(user.getUsername());
    }

    @Override
    public void onChatServiceDisconnected() {
        // Do nothing
    }


}
