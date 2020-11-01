package com.xmpp.smackchat.connection;

import com.xmpp.smackchat.Constant;
import com.xmpp.smackchat.base.AppLog;
import com.xmpp.smackchat.model.User;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

public class ChatConnection implements ConnectionListener {

    public enum ConnectionState {
        CONNECTED, AUTHENTICATED, CONNECTING, DISCONNECTING, DISCONNECTED;
    }

    public enum LoggedInState {
        LOGGED_IN, LOGGED_OUT;
    }

    public ChatConnection(User user, String host) {
        this.user = user;
        this.host = host;
    }

    private final User user;
    private final String host;
    private ConnectionState connectionState;
    private XMPPTCPConnection connection;

    @Override
    public void connected(XMPPConnection connection) {
        AppLog.d("Connected");
        connectionState = ConnectionState.CONNECTED;
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        AppLog.d("Authenticated");
        connectionState = ConnectionState.CONNECTED;
    }

    @Override
    public void connectionClosed() {
        AppLog.d("Closed");
        connectionState = ConnectionState.DISCONNECTED;
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        connectionState = ConnectionState.DISCONNECTED;
        AppLog.e("connectionClosedOnError: " + e.getMessage());
    }

    public void connect() throws InterruptedException, XMPPException, SmackException, IOException {
        SmackConfiguration.DEBUG = true;
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setHost(host);
        builder.setXmppDomain(Constant.DOMAIN);
        builder.setPort(5222);
        builder.setUsernameAndPassword(user.getUsername(), user.getPassword());

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
}
