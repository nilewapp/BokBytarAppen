package com.mooo.nilewapps.bokbytarappen;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

import com.facebook.Session;
import com.facebook.SessionState;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class MainActivity extends SherlockFragmentActivity {
    
    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            /* Switch to login activity */
            if (session.isClosed()) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }
    };
    
    private FacebookActivity fbActivity = new FacebookActivity(this, statusCallback);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /* Set layout */
        setContentView(R.layout.activity_main);
        
        /* Add tabs */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        Tab tab = actionBar.newTab()
                .setText(R.string.want)
                .setTabListener(new TabListener<MainActivity, WantFragment>(
                        this, null, WantFragment.class));
        actionBar.addTab(tab);
        
        tab = actionBar.newTab()
                .setText(R.string.have)
                .setTabListener(new TabListener<MainActivity, HaveFragment>(
                        this, null, HaveFragment.class));
        actionBar.addTab(tab);
        
        /* Restore Facebook session */
        fbActivity.restoreSession(savedInstanceState);
        
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbActivity.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fbActivity.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        fbActivity.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        fbActivity.onStop();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.global, menu);
        final Resources res = getResources();
        
        menu.add(res.getString(R.string.search))
            .setIcon(android.R.drawable.ic_menu_search)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        
        Drawable profileIcon = res.getDrawable(R.drawable.ic_action_user);
        
        menu.add(res.getString(R.string.profile))
            .setIcon(profileIcon)
            .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    /* Switch to the profile activity */
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    return true;
                }
            })
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case R.id.logout:
            fbActivity.logout();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    

}