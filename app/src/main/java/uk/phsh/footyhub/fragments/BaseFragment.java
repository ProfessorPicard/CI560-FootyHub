package uk.phsh.footyhub.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import uk.phsh.footyhub.interfaces.I_FragmentCallback;

public abstract class BaseFragment extends Fragment {

    protected final I_FragmentCallback _callBack;
    protected final Context _context;

    public abstract String getActionBarTitle();

    public BaseFragment(I_FragmentCallback callBack, Context context) {
        this._callBack = callBack;
        this._context = context;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(_context != null)
            _callBack.changeActionbarTitle(getActionBarTitle());
    }

}
