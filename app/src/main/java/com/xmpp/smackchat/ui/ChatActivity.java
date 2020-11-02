package com.xmpp.smackchat.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.xmpp.smackchat.R;
import com.xmpp.smackchat.base.views.SpaceItemDecoration;
import com.xmpp.smackchat.base.views.recycler.BaseRecyclerAdapter;
import com.xmpp.smackchat.model.ChatMessage;

import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

public class ChatActivity extends XMPPActivity {

    private BaseRecyclerAdapter<ChatMessage> messageAdapter;
    private TextInputLayout inputChat;
    private EntityBareJid entityBareJid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RecyclerView rvConversation = findViewById(R.id.rvConversation);
        rvConversation.addItemDecoration(new SpaceItemDecoration(0, 0, 0, 10));
        messageAdapter = new BaseRecyclerAdapter<>();
        rvConversation.setAdapter(messageAdapter);

        findViewById(R.id.btnSend).setOnClickListener(this::sendMessage);
        inputChat = findViewById(R.id.inputChat);

        String bareJid = getIntent().getStringExtra("jid");
        try {
            setTitle(bareJid);
            entityBareJid = JidCreate.entityBareFrom(bareJid);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }


    private void sendMessage(View view) {
        String body = inputChat.getEditText().getEditableText().toString();
        if (bound) {
            chatService.sendMessage(entityBareJid, body);
            inputChat.getEditText().setText("");
        }
    }

    @Override
    public void onChatServiceConnected() {
        chatService.clearChat();
        chatService.getChatMessage().observe(this, this::observeChatMessage);
    }

    private void observeChatMessage(ChatMessage chatMessage) {
        if (chatMessage != null) {
            messageAdapter.addItem(chatMessage);
        }
    }

    @Override
    public void onChatServiceDisconnected() {

    }
}
