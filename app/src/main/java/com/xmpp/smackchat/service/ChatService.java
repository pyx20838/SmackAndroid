package com.xmpp.smackchat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.xmpp.smackchat.base.AppLog;
import com.xmpp.smackchat.connection.SmackChat;
import com.xmpp.smackchat.model.ChatMessage;
import com.xmpp.smackchat.model.User;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
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

    private SmackChat chat;
    private Executor executor;

    @Override
    public void onCreate() {
        super.onCreate();
        executor = Executors.newSingleThreadExecutor();
        chat = new SmackChat();
    }

    public void handleLogin(String username, String password) {
        executor.execute(() -> {
            try {
                chat.connect(username, password);
            } catch (InterruptedException | XMPPException | SmackException | IOException e) {
                AppLog.e("Connect failed: " + e.getMessage());
            }
        });
    }

    public void handleLogout() {
        executor.execute(() -> chat.disconnect());
    }

    public LiveData<User> getUserInfo() {
        return chat.lvUser;
    }

    public LiveData<SmackChat.ConnectionState> getConnectionState() {
        return chat.lvConnState;
    }

    public LiveData<List<RosterEntry>> getRosterEntries() {
        return chat.lvRosterEntries;
    }

    public LiveData<ChatMessage> getChatMessage() {
        return chat.lvChatMessage;
    }

    public void sendMessage(EntityBareJid jid, String body) {
        executor.execute(() -> {
            Message message = new Message();
            message.setBody(body);
            try {
                chat.chat(jid, message);
            } catch (SmackException.NotConnectedException | InterruptedException e) {
                AppLog.e("Send message failed: " + e.getMessage());
            }
        });
    }

    public void sendMessage(String body) {
        executor.execute(() -> {
            Message message = new Message();
            message.setBody(body);
            try {
                chat.chat(chat.currentEntityBareJid, message);
            } catch (SmackException.NotConnectedException | InterruptedException e) {
                AppLog.e("Send message failed: " + e.getMessage());
            }
        });
    }

    public void addFriend(String jidName, String name) {
        executor.execute(() -> {
            try {
                chat.addFriend(jidName, name);
            } catch (SmackException.NotLoggedInException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | InterruptedException | SmackException.NoResponseException | XmppStringprepException e) {
                AppLog.e("Add friend error: " + e.getMessage());
            }
        });
    }

    public void setCurrentEntityBareJid(EntityBareJid jid) {
        chat.setCurrentEntityBareJid(jid);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
