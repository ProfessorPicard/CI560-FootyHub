package uk.phsh.footyhub.fragments;

import android.content.Context;

import uk.phsh.footyhub.R;
import uk.phsh.footyhub.interfaces.I_FragmentCallback;

public class NewsFragment extends BaseFragment {

    public NewsFragment(I_FragmentCallback callBack, Context context) {
        super(callBack, context);
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.news_fragment_actionbar);
    }
}
