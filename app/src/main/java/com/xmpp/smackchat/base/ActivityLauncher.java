package com.xmpp.smackchat.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.xmpp.smackchat.base.arch.BaseActivity;

public class ActivityLauncher extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LauncherIntentDispatcher dispatcher = new LauncherIntentDispatcher(this, getIntent());

        @LauncherIntentDispatcher.Action int dispatchAction = dispatcher.dispatch();
        switch (dispatchAction) {
            case LauncherIntentDispatcher.Action.ACTION_FINISH:
                finish();
                break;
            case LauncherIntentDispatcher.Action.ACTION_CONTINUE:
                break;
            default:
                finish();
                break;
        }
    }
}
