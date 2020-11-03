package com.xmpp.smackchat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.xmpp.smackchat.base.AppLog;
import com.xmpp.smackchat.model.ChatMessage;
import com.xmpp.smackchat.model.Contact;
import com.xmpp.smackchat.model.User;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatService extends Service {

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private SmackChat smackChat;
    private Executor executor;

    @Override
    public void onCreate() {
        super.onCreate();
        executor = Executors.newSingleThreadExecutor();
        smackChat = new SmackChat();
    }

    public void login(String username, String password) {
        executor.execute(() -> {
            try {
                smackChat.connect(username, password);
            } catch (InterruptedException | XMPPException | SmackException | IOException e) {
                AppLog.e("Connect failed: " + e.getMessage());
            }
        });
    }

    public void register(String username, String password) {
        executor.execute(() -> smackChat.register(username, password));
    }

    public void logout() {
        executor.execute(() -> smackChat.disconnect());
    }

    public LiveData<User> getUserInfo() {
        return smackChat.lvUser;
    }

    public LiveData<SmackChat.ConnectionState> getConnectionState() {
        return smackChat.lvConnState;
    }

    public LiveData<List<Contact>> getContacts() {
        return smackChat.lvContacts;
    }

    public LiveData<ChatMessage> getChatMessage() {
        return smackChat.lvChatMessage;
    }

    public LiveData<Presence> getPresence() {
        return smackChat.lvPresence;
    }

    public void sendMessage(EntityBareJid jid, String body) {
        executor.execute(() -> {
            try {
                smackChat.sendMessage(jid, body);
            } catch (SmackException.NotConnectedException | InterruptedException e) {
                AppLog.e("Send message failed: " + e.getMessage());
            }
        });
    }

    public void addFriend(String jidName) {
        executor.execute(() -> {
            try {
                smackChat.addFriend(jidName);
            } catch (SmackException.NotConnectedException | InterruptedException | XmppStringprepException e) {
                AppLog.e("Add friend error: " + e.getMessage());
            }
        });
    }

    public void clearChat() {
        smackChat.clearChat();
    }
}
