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

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;


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
