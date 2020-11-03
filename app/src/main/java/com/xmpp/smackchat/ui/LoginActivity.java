package com.xmpp.smackchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.xmpp.smackchat.R;
import com.xmpp.smackchat.service.SmackChat;

public class LoginActivity extends XMPPActivity {

    private TextInputLayout inputUsername, inputPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);

        findViewById(R.id.btnLogin).setOnClickListener(this::login);
        findViewById(R.id.btnRegister).setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void login(View view) {
        if (bound) {
            if (isValid()) {
                String username = inputUsername.getEditText().getEditableText().toString();
                String password = inputPassword.getEditText().getEditableText().toString();
                chatService.login(username, password);
            }
        }
    }


    private boolean isValid() {
        String username = inputUsername.getEditText().getEditableText().toString();
        String password = inputPassword.getEditText().getEditableText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Snackbar.make(inputUsername, "You need to fill all fields", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_setting) {
            startActivity(new Intent(this, SettingActivity.class));
        }
        return true;
    }
}
