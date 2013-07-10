/**
 *  Copyright 2013 Robert Welin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import android.support.v4.app.Fragment;
import android.test.suitebuilder.annotation.Suppress;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends SherlockFragmentActivity {
    
    private String[] drawerItems = {"First", "Second", "Third", "Fourth"};
    private ListView drawerList;
    
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
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            setTitle(drawerItems[position]);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getActionBar().setTitle(title);
    }
    
    private FacebookActivity fbActivity = new FacebookActivity(this, statusCallback);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /* Set layout */
        setContentView(R.layout.activity_main);
        /* Initialise navigation drawer */
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, drawerItems));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
  
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
