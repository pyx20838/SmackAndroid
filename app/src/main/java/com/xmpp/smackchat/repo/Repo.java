package com.xmpp.smackchat.repo;

public interface Repo {

    void saveDomain(String domain);
    void saveServerAddr(String serverAddr);
    void savePort(int port);

    String getDomain();
    String getServerAddr();
    int getPort();
}
