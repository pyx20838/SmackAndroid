package com.xmpp.smackchat.service;


import androidx.lifecycle.MutableLiveData;

import com.xmpp.smackchat.base.AppLog;
import com.xmpp.smackchat.base.views.recycler.RecyclerViewType;
import com.xmpp.smackchat.model.ChatMessage;
import com.xmpp.smackchat.model.Contact;
import com.xmpp.smackchat.model.User;
import com.xmpp.smackchat.repo.Repo;
import com.xmpp.smackchat.repo.RepoImpl;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
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
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SmackChat implements ConnectionListener, IncomingChatMessageListener, OutgoingChatMessageListener, RosterListener, RosterLoadedListener, SubscribeListener {

    public enum ConnectionState {
        CONNECTED, AUTHENTICATED, DISCONNECTED
    }

    private XMPPTCPConnection connection;
    private ChatManager chatManager;
    private Roster roster;
    private final Repo repo = RepoImpl.getInstance();

    public final MutableLiveData<User> lvUser = new MutableLiveData<>();
    public final MutableLiveData<ConnectionState> lvConnState = new MutableLiveData<>();
    public final MutableLiveData<List<Contact>> lvContacts = new MutableLiveData<>();
    public final MutableLiveData<ChatMessage> lvChatMessage = new MutableLiveData<>();
    public final MutableLiveData<Presence> lvPresence = new MutableLiveData<>();

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
        builder.setHost(repo.getServerAddr());
        builder.setXmppDomain(repo.getDomain());
        builder.setPort(repo.getPort());
        builder.setUsernameAndPassword(username, password);
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        connection = new XMPPTCPConnection(builder.build());
        connection.addConnectionListener(this);
        connection.connect();
        connection.login();

        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
        reconnectionManager.enableAutomaticReconnection();
    }


    public void register(String username, String password) {
        try {
            XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
            builder.setHost(repo.getServerAddr());
            builder.setXmppDomain(repo.getDomain());
            builder.setPort(repo.getPort());
            builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

            AbstractXMPPConnection xmppConnection = new XMPPTCPConnection(builder.build());
            xmppConnection.addConnectionListener(new ConnectionListener() {
                @Override
                public void connected(XMPPConnection connection) {
                    AccountManager accountManager = AccountManager.getInstance(connection);
                    try {
                        if (accountManager.supportsAccountCreation()) {
                            accountManager.sensitiveOperationOverInsecureConnection(true);
                            accountManager.createAccount(Localpart.from(username), password);

                            connect(username, password);
                            xmppConnection.disconnect();
                        }
                    } catch (InterruptedException | XMPPException | IOException | SmackException e) {
                        AppLog.e("Register failed: " + e.getMessage());
                    }
                }

                @Override
                public void authenticated(XMPPConnection connection, boolean resumed) {

                }

                @Override
                public void connectionClosed() {

                }

                @Override
                public void connectionClosedOnError(Exception e) {

                }
            });
            xmppConnection.connect();
        } catch (InterruptedException | XMPPException | IOException | SmackException e) {
            AppLog.e("Register failed: " + e.getMessage());
        }
    }

    public void disconnect() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }

        if (roster != null) {
            roster.addRosterListener(this);
            roster.addSubscribeListener(this);
            roster.addRosterLoadedListener(this);
            roster = null;
        }

        if (chatManager != null) {
            chatManager.addIncomingListener(this);
            chatManager.addOutgoingListener(this);
            chatManager = null;
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
        try {
            roster.reload();
        } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void entriesUpdated(Collection<Jid> addresses) {
        try {
            roster.reload();
        } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void entriesDeleted(Collection<Jid> addresses) {
        try {
            roster.reload();
        } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void presenceChanged(Presence presence) {
        AppLog.d("Presence changed " + presence);
        lvPresence.postValue(presence);
    }

    @Override
    public void onRosterLoaded(Roster roster) {
        List<Contact> contacts = new ArrayList<>();
        Set<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            Presence presence = roster.getPresence(entry.getJid());
            Contact contact = new Contact(entry, presence);
            contacts.add(contact);
        }
        this.lvContacts.postValue(contacts);
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

    public void clearChat() {
        lvChatMessage.postValue(null);
    }
}
