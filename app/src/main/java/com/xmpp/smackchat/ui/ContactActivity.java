package com.xmpp.smackchat.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.xmpp.smackchat.Constant;
import com.xmpp.smackchat.R;
import com.xmpp.smackchat.base.AppLog;
import com.xmpp.smackchat.base.views.recycler.BaseRecyclerAdapter;
import com.xmpp.smackchat.base.views.recycler.BaseRecyclerViewHolder;
import com.xmpp.smackchat.base.views.recycler.RecyclerActionListener;
import com.xmpp.smackchat.model.Contact;
import com.xmpp.smackchat.model.User;
import com.xmpp.smackchat.service.SmackChat;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactActivity extends XMPPActivity {

    private BaseRecyclerAdapter<Contact> contactAdapter;

    private final RecyclerActionListener contactListener = new RecyclerActionListener() {
        @Override
        public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
            Contact contact = contactAdapter.getData().get(position);
            Intent chatIntent = new Intent(ContactActivity.this, ChatActivity.class);
            chatIntent.putExtra("jid", contact.getAddress());
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
            TextInputEditText editText = new TextInputEditText(this);
            editText.setHint("Add your friend's id");
            new MaterialAlertDialogBuilder(this)
                    .setView(editText)
                    .setPositiveButton("Add friend", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            chatService.addFriend(editText.getEditableText().toString() + "@" + Constant.DOMAIN);
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();

        }
    }

    private void handleLogout(View view) {
        if (bound) {
            chatService.logout();
        }
    }

    @Override
    public void onChatServiceConnected() {
        chatService.getUserInfo().observe(this, this::observeUserInfo);
        chatService.getConnectionState().observe(this, this::observeConnState);
        chatService.getRosterEntries().observe(this, this::observeContactList);
        chatService.getPresence().observe(this, this::observePresence);
    }

    private void observePresence(Presence presence) {
        AppLog.d("Presence change: " + presence.getFrom() + ", " + presence.getType().name() + ", " + presence.getMode().name());
        // TODO - update status here

        List<Contact> contacts = contactAdapter.getData();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getEntry().getJid().equals(presence.getFrom())) {
                contacts.get(i).setOnline(presence.getType() == Presence.Type.available);
                contactAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    private void observeContactList(List<RosterEntry> rosterEntries) {
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
        tvUsername.setText(String.format(Locale.getDefault(), "Hi, %s", user.getUsername()));
    }

    @Override
    public void onChatServiceDisconnected() {
        // Do nothing
    }


}
