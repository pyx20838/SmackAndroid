package com.xmpp.smackchat.repo;

import com.xmpp.smackchat.Constant;
import com.xmpp.smackchat.base.prefs.BasePrefs;

public class RepoImpl implements Repo {

    private static final String PREF_DOMAIN = "pref_domain";
    private static final String PREF_SERVER = "pref_server";
    private static final String PREF_PORT = "pref_port";

    private static Repo repo = null;

    private RepoImpl() {
    }

    public static Repo getInstance() {
        if (repo == null) {
            repo = new RepoImpl();
        }
        return repo;
    }

    @Override
    public void saveDomain(String domain) {
        BasePrefs.writeString(PREF_DOMAIN, domain);
    }

    @Override
    public void saveServerAddr(String serverAddr) {
        BasePrefs.writeString(PREF_SERVER, serverAddr);
    }

    @Override
    public void savePort(int port) {
        BasePrefs.writeInt(PREF_PORT, port);
    }

    @Override
    public String getDomain() {
        return BasePrefs.readString(PREF_DOMAIN, Constant.DOMAIN);
    }

    @Override
    public String getServerAddr() {
        return BasePrefs.readString(PREF_SERVER, Constant.HOST);
    }

    @Override
    public int getPort() {
        return BasePrefs.readInt(PREF_PORT, Constant.PORT);
    }
}
