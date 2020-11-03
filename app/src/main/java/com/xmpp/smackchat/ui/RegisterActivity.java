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

public class RegisterActivity extends XMPPActivity {

    private TextInputLayout inputUsername, inputPassword, inputConfirmPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.btnLogin).setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
        findViewById(R.id.btnRegister).setOnClickListener(view -> handleRegister());
    }

    private void handleRegister() {
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);

        if (isValid()) {
            if (bound) {
                String username = inputUsername.getEditText().getEditableText().toString();
                String password = inputPassword.getEditText().getEditableText().toString();
                chatService.register(username, password);
            }
        }
    }

    private boolean isValid() {
        String username = inputUsername.getEditText().getEditableText().toString();
        String password = inputPassword.getEditText().getEditableText().toString();
        String confirmPassword = inputConfirmPassword.getEditText().getEditableText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Snackbar.make(inputConfirmPassword, "You need to fill all fields", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (!confirmPassword.equals(password)) {
            Snackbar.make(inputConfirmPassword, "Password is not matched", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onChatServiceConnected() {

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
