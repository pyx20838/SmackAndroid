package com.xmpp.smackchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.xmpp.smackchat.Constant;
import com.xmpp.smackchat.R;
import com.xmpp.smackchat.connection.SmackChat;

public class LoginActivity extends XMPPActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btnLogin).setOnClickListener(this::login);
        findViewById(R.id.btnExit).setOnClickListener(view -> finish());
    }

    private void login(View view) {
        if (bound) {
            chatService.handleLogin(Constant.USERNAME, Constant.PASSWORD);
        }
    }

    @Override
    public void onChatServiceConnected() {
        chatService.getConnectionState().observe(this, this::observeConnState);
    }

    private void observeConnState(SmackChat.ConnectionState connectionState) {
        if (connectionState == SmackChat.ConnectionState.AUTHENTICATED) {
            startActivity(new Intent(this, ContactActivity.class));
            finish();
        }
    }

    @Override
    public void onChatServiceDisconnected() {

    }
}
