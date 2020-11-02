package com.xmpp.smackchat.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.xmpp.smackchat.R;
import com.xmpp.smackchat.base.arch.BaseActivity;
import com.xmpp.smackchat.base.views.recycler.BaseRecyclerAdapter;
import com.xmpp.smackchat.model.ChatMessage;

import org.jxmpp.jid.EntityBareJid;

public class ChatActivity extends XMPPActivity {

    private BaseRecyclerAdapter<ChatMessage> messageAdapter;
    private TextInputLayout inputChat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RecyclerView rvConversation = findViewById(R.id.rvConversation);
        messageAdapter = new BaseRecyclerAdapter<>();
        rvConversation.setAdapter(messageAdapter);

        findViewById(R.id.btnSend).setOnClickListener(this::sendMessage);
        inputChat = findViewById(R.id.inputChat);
    }

    private void sendMessage(View view) {
        String body = inputChat.getEditText().getEditableText().toString();
        if (bound) {
            chatService.sendMessage(body);
            inputChat.getEditText().setText("");
        }
    }

    @Override
    public void onChatServiceConnected() {
        chatService.getChatMessage().observe(this, this::observeChatMessage);
    }

    private void observeChatMessage(ChatMessage chatMessage) {
        messageAdapter.addItem(chatMessage);
    }

    @Override
    public void onChatServiceDisconnected() {

    }
}
