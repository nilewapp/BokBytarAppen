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
package com.mooo.nilewapps.bokbytarappen.view;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.Toast;
import android.app.Dialog;

import com.mooo.nilewapps.androidnilewapp.FilterableListDialogFragment;
import com.mooo.nilewapps.bokbytarappen.R;
import com.mooo.nilewapps.bokbytarappen.UniversityListLoader;

public class UniversityListDialogFragment extends FilterableListDialogFragment
        implements LoaderManager.LoaderCallbacks<List<String>> 
{
    
    private static final String COUNTRY_KEY = "country";
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Bundle args = new Bundle();
        args.putInt(COUNTRY_KEY, 752);
        getLoaderManager().initLoader(0, args, this);
        dialog.setTitle(getResources().getString(R.string.choose_university));
        
        return dialog;
    }
    
    public void show() {
        show(getTargetFragment().getFragmentManager(), "university_dialog");
    }
    
    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new UniversityListLoader(getActivity(), args.getInt(COUNTRY_KEY));
    }
    
    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        if (data == null) {
            Toast error = Toast.makeText(
                    getActivity(), 
                    getResources().getString(R.string.university_request_error_message),
                    Toast.LENGTH_SHORT);
            error.show();
        }
        setData(data);
    }
    
    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        setData(null);
    }


}
