package com.xmpp.smackchat.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.xmpp.smackchat.base.arch.BaseActivity;
import com.xmpp.smackchat.service.ChatService;

public abstract class XMPPActivity extends BaseActivity {
    protected boolean bound = false;
    protected ChatService chatService;

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ChatService.LocalBinder binder = (ChatService.LocalBinder) service;
            chatService = binder.getService();
            bound = true;

            onChatServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
            onChatServiceDisconnected();
        }
    };

    public abstract void onChatServiceConnected();
    public abstract void onChatServiceDisconnected();

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
        bound = false;
    }

}
