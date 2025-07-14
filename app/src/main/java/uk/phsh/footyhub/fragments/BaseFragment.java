package uk.phsh.footyhub.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import uk.phsh.footyhub.interfaces.I_FragmentCallback;

public abstract class BaseFragment extends Fragment {

    protected I_FragmentCallback _callBack = null;

    public abstract String getActionBarTitle();

    public BaseFragment() { }

    public BaseFragment(I_FragmentCallback callBack) {
        this._callBack = callBack;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(_callBack != null)
            _callBack.changeActionbarTitle(getActionBarTitle());
    }

}
