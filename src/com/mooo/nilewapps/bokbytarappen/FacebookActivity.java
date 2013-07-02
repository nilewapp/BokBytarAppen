package com.mooo.nilewapps.bokbytarappen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.Session.StatusCallback;

/**
 * Provides utility methods for handling Facebook sessions
 * @author nilewapp
 *
 */
public class FacebookActivity {
    
    private final Activity activity;
    private final StatusCallback statusCallback;

    public FacebookActivity(Activity activity, StatusCallback statusCallback) {
        this.activity = activity;
        this.statusCallback = statusCallback;
    }
    
    /**
     * Login with Facebook
     */
    public void login() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(activity).setCallback(statusCallback));
        } else {
            Session.openActiveSession(activity, true, statusCallback);
        }
    }
    
    /**
     * Logout from the current Facebook session
     */
    public void logout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }
    
    public void restoreSession(Bundle savedInstanceState) {
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(activity, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(activity);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(activity).setCallback(statusCallback));
            }
        }
    }
    
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Session.getActiveSession().onActivityResult(activity, requestCode, resultCode, data);
    }

    public void onSaveInstanceState(Bundle outState) {
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    public void onStart() {
        Session.getActiveSession().addCallback(statusCallback);
    }

    public void onStop() {
        Session.getActiveSession().removeCallback(statusCallback);
    }
    
}
