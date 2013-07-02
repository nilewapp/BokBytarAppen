package com.mooo.nilewapps.bokbytarappen;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.Toast;
import android.app.Dialog;

import com.mooo.nilewapps.androidnilewapp.FilterableListDialogFragment;

public class UniversityListDialog extends FilterableListDialogFragment
        implements LoaderManager.LoaderCallbacks<List<String>> 
{
    
    private static final String COUNTRY_KEY = "country";
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
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
