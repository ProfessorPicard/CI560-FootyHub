package uk.phsh.footyhub.fragments;

import android.content.Context;

import uk.phsh.footyhub.R;
import uk.phsh.footyhub.interfaces.I_FragmentCallback;

public class StandingsFragment extends BaseFragment {

    public StandingsFragment(I_FragmentCallback callBack, Context context) {
        super(callBack, context);
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.standings_fragment_actionbar);
    }
}
