package com.xmpp.smackchat.service;

import androidx.lifecycle.MutableLiveData;

import com.xmpp.smackchat.Constant;
import com.xmpp.smackchat.base.AppLog;
import com.xmpp.smackchat.base.views.recycler.RecyclerViewType;
import com.xmpp.smackchat.model.ChatMessage;
import com.xmpp.smackchat.model.User;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.RosterLoadedListener;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SmackChat implements ConnectionListener, IncomingChatMessageListener, OutgoingChatMessageListener, RosterListener, RosterLoadedListener, SubscribeListener {

    public enum ConnectionState {
        CONNECTED, AUTHENTICATED, DISCONNECTED;
    }

    private XMPPTCPConnection connection;
    private ChatManager chatManager;
    private Roster roster;

    public final MutableLiveData<User> lvUser = new MutableLiveData<>();
    public final MutableLiveData<ConnectionState> lvConnState = new MutableLiveData<>();
    public final MutableLiveData<List<RosterEntry>> lvRosterEntries = new MutableLiveData<>();
    public final MutableLiveData<ChatMessage> lvChatMessage = new MutableLiveData<>();

    @Override
    public void connected(XMPPConnection connection) {
        lvConnState.postValue(ConnectionState.CONNECTED);
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        AppLog.d("Authenticated");
        lvConnState.postValue(ConnectionState.AUTHENTICATED);

        // Register listeners for incoming/outgoing message
        chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addIncomingListener(this);
        chatManager.addOutgoingListener(this);

        // Load roster
        roster = Roster.getInstanceFor(connection);
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
        roster.addRosterListener(this);
        roster.addSubscribeListener(this);
        if (!roster.isLoaded()) {
            try {
                roster.addRosterLoadedListener(this);
                roster.reload();
            } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
                AppLog.e("Roster reload exception: " + e.getMessage());
            }
        } else {
            onRosterLoaded(roster);
        }


        lvUser.postValue(new User(connection.getUser().getLocalpart().toString()));
    }

    @Override
    public void connectionClosed() {
        lvConnState.postValue(ConnectionState.DISCONNECTED);
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        lvConnState.postValue(ConnectionState.DISCONNECTED);
    }

    public void connect(String username, String password) throws InterruptedException, XMPPException, SmackException, IOException {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setHost(Constant.HOST);
        builder.setXmppDomain(Constant.DOMAIN);
        builder.setPort(Constant.PORT);
        builder.setUsernameAndPassword(username, password);

        connection = new XMPPTCPConnection(builder.build());
        connection.addConnectionListener(this);
        connection.connect();
        connection.login();

        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
        reconnectionManager.enableAutomaticReconnection();
    }

    public void disconnect() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        AppLog.d("Incoming message from " + from.asEntityBareJid() + ": " + message.getBody());
        lvChatMessage.postValue(new ChatMessage(RecyclerViewType.TYPE_INCOMING_MESSAGE, from, message));
    }

    @Override
    public void newOutgoingMessage(EntityBareJid to, Message message, Chat chat) {
        AppLog.d("Outgoing message to " + to.asEntityBareJid() + ": " + message.getBody());
        lvChatMessage.postValue(new ChatMessage(RecyclerViewType.TYPE_OUTGOING_MESSAGE, to, message));
    }

    public void sendMessage(EntityBareJid jid, String message) throws SmackException.NotConnectedException, InterruptedException {
        chatManager.chatWith(jid).send(message);
    }

    @Override
    public void entriesAdded(Collection<Jid> addresses) {
        for (Jid jid : addresses) {
            AppLog.d("Entry added: " + jid.asDomainBareJid());
        }

        try {
            roster.reload();
        } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void entriesUpdated(Collection<Jid> addresses) {
        for (Jid jid : addresses) {
            AppLog.d("Entry updated: " + jid.asDomainBareJid());
        }

        try {
            roster.reload();
        } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void entriesDeleted(Collection<Jid> addresses) {
        for (Jid jid : addresses) {
            AppLog.d("Entry deleted: " + jid.asDomainBareJid());
        }

        try {
            roster.reload();
        } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void presenceChanged(Presence presence) {
        AppLog.d("Presence changed " + presence.getFrom());
    }

    @Override
    public void onRosterLoaded(Roster roster) {
        this.lvRosterEntries.postValue(new ArrayList<>(roster.getEntries()));
    }

    @Override
    public void onRosterLoadingFailed(Exception exception) {
    }

    @Override
    public SubscribeAnswer processSubscribe(Jid from, Presence subscribeRequest) {
        return SubscribeAnswer.ApproveAndAlsoRequestIfRequired;
    }

    public void addFriend(String jidName) throws SmackException.NotConnectedException, InterruptedException, XmppStringprepException {
        Presence subscribe = new Presence(Presence.Type.subscribe);
        subscribe.setTo(JidCreate.bareFrom(jidName));
        connection.sendStanza(subscribe);
    }

}
