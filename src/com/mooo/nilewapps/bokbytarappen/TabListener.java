package com.mooo.nilewapps.bokbytarappen;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class TabListener<S extends Activity, T extends Fragment> 
        implements ActionBar.TabListener {
    
    private Fragment fragment;
    private final S activity;
    private final String tag;
    private final Class<T> cls;
    
    public TabListener(S activity, String tag, Class<T> cls) {
        this.activity = activity;
        this.tag = tag;
        this.cls = cls;
    }
    
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        if (fragment == null) {
            /* Instantiate fragment */
            fragment  = Fragment.instantiate(activity, cls.getName());
            ft.add(android.R.id.content, fragment, tag);
        } else {
            /* Use already existing fragment */
            ft.attach(fragment);
        }
    }
    
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        if (fragment != null) {
            ft.detach(fragment);
        }
    }
    
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        /* Do nothing */
    }
    
}
