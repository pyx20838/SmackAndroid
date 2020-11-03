package com.xmpp.smackchat.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.xmpp.smackchat.R;
import com.xmpp.smackchat.base.arch.BaseActivity;
import com.xmpp.smackchat.repo.Repo;
import com.xmpp.smackchat.repo.RepoImpl;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextInputLayout inputDomain, inputServer, inputPort;
    private Button btnUpdate;
    private final Repo repo = RepoImpl.getInstance();
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnUpdate = findViewById(R.id.btnUpdateSetting);
        btnUpdate.setOnClickListener(this);

        inputDomain = findViewById(R.id.inputDomain);
        inputServer = findViewById(R.id.inputServerAddr);
        inputPort = findViewById(R.id.inputPort);
        
        inputDomain.getEditText().setText(repo.getDomain());
        inputServer.getEditText().setText(repo.getServerAddr());
        inputPort.getEditText().setText("" + repo.getPort());

    }

    private boolean isValid() {
        String domain = inputDomain.getEditText().getEditableText().toString();
        String serverAddr = inputServer.getEditText().getEditableText().toString();
        String port = inputPort.getEditText().getEditableText().toString();

        if (TextUtils.isEmpty(domain) || TextUtils.isEmpty(serverAddr) || TextUtils.isEmpty(port)) {
            Snackbar.make(btnUpdate, "You need to fill all fields", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (isValid()) {
            String domain = inputDomain.getEditText().getEditableText().toString();
            String serverAddr = inputServer.getEditText().getEditableText().toString();
            String port = inputPort.getEditText().getEditableText().toString();

            repo.saveDomain(domain);
            repo.saveServerAddr(serverAddr);
            repo.savePort(Integer.parseInt(port));

            finish();
        }
    }
}
