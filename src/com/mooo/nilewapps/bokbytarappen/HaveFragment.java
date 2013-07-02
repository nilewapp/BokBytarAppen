package com.mooo.nilewapps.bokbytarappen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;

public class HaveFragment extends SherlockFragment {
    
    @Override
    public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState) {
        
        final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_have, null, false);
        
        return view;
        
    }
}
