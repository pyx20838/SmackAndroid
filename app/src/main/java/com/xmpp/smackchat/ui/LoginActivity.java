package com.xmpp.smackchat.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.xmpp.smackchat.Constant;
import com.xmpp.smackchat.R;
import com.xmpp.smackchat.base.AppLog;
import com.xmpp.smackchat.base.arch.BaseActivity;
import com.xmpp.smackchat.connection.ChatConnection;
import com.xmpp.smackchat.model.User;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends BaseActivity {

    private Executor executor = Executors.newSingleThreadExecutor();
    private ChatConnection connection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connection = new ChatConnection(new User(Constant.USERNAME, Constant.PASSWORD), Constant.HOST);
        findViewById(R.id.btnLogin).setOnClickListener(this::login);
    }

    private void login(View view) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    connection.connect();
                } catch (Exception e) {
                    AppLog.e("Connect failed: " + e.getMessage());
                }
            }
        });
    }
}
