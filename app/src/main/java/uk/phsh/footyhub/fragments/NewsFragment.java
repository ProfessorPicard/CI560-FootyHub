package uk.phsh.footyhub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.phsh.footyhub.R;
import uk.phsh.footyhub.interfaces.I_FragmentCallback;

public class NewsFragment extends BaseFragment {

    public NewsFragment() { super(); }

    public NewsFragment(I_FragmentCallback callBack) {
        super(callBack);
    }

//    /**
//     * @param inflater           The LayoutInflater object that can be used to inflate
//     *                           any views in the fragment,
//     * @param container          If non-null, this is the parent view that the fragment's
//     *                           UI should be attached to.  The fragment should not add the view itself,
//     *                           but this can be used to generate the LayoutParams of the view.
//     * @param savedInstanceState If non-null, this fragment is being re-constructed
//     *                           from a previous saved state as given here.
//     * @return View              The inflated view
//     */
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v;
//        v = inflater.inflate(R.layout.news_frag, container, false);
//        return v;
//    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.news_fragment_actionbar);
    }
}
