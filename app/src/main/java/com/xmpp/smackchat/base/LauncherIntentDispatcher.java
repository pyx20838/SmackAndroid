package com.xmpp.smackchat.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.IntDef;

import com.xmpp.smackchat.service.ChatService;
import com.xmpp.smackchat.ui.LoginActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LauncherIntentDispatcher {

    @IntDef({Action.ACTION_FINISH, Action.ACTION_CONTINUE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Action {
        int ACTION_FINISH = 0;
        int ACTION_CONTINUE = 1;
    }

    private Activity fromActivity;
    private Intent fromIntent;

    public LauncherIntentDispatcher(Activity fromActivity, Intent fromIntent) {
        this.fromActivity = fromActivity;
        this.fromIntent = fromIntent;
    }

    public @Action
    int dispatch() {
        if (!isServiceRunning()) {
            fromActivity.startService(new Intent(fromActivity, ChatService.class));
        }

        fromActivity.startActivity(new Intent(fromActivity, LoginActivity.class));
        return Action.ACTION_FINISH;
    }

    public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) ChatApp.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) return false;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ChatService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
