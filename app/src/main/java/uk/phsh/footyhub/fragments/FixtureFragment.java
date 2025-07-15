package uk.phsh.footyhub.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.TreeMap;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.adapters.FixtureAdapter;
import uk.phsh.footyhub.helpers.UtilityHelper;
import uk.phsh.footyhub.interfaces.I_FragmentCallback;
import uk.phsh.footyhub.rest.RestManager;
import uk.phsh.footyhub.rest.enums.LeagueEnum;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.CompetitionFixtures;
import uk.phsh.footyhub.rest.models.MatchWeek;
import uk.phsh.footyhub.rest.tasks.FixturesTask;

public class FixtureFragment extends BaseFragment implements I_TaskCallback<CompetitionFixtures> {

    private final ArrayList<MatchWeek> _matchWeeks = new ArrayList<>();
    private FixtureAdapter _adapter;
    private RecyclerView _fixtureRecycler;


    public FixtureFragment() { super(); }

    public FixtureFragment(I_FragmentCallback callBack) {
        super(callBack);
    }

    /**
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return View              The inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fixture_frag, container, false);
        _fixtureRecycler = v.findViewById(R.id.fixtureRecycler);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        RestManager rm = RestManager.getInstance(requireActivity().getCacheDir());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        _adapter = new FixtureAdapter(_matchWeeks, getContext());
        _fixtureRecycler.setLayoutManager(layoutManager);
        _matchWeeks.clear();
        _fixtureRecycler.setAdapter(_adapter);

        rm.asyncTask(new FixturesTask(this, LeagueEnum.PREMIER_LEAGUE, 0, 0));
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.fixture_fragment_actionbar);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccess(CompetitionFixtures fixtures) {
        UtilityHelper.getInstance().runOnUiThread(_context, () -> {
            TreeMap<Integer, MatchWeek> matchWeeks = fixtures.getMatchWeeks();
            _matchWeeks.clear();
            _adapter.notifyDataSetChanged();
            _matchWeeks.addAll(matchWeeks.values());
            _adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onError(String message) {

    }
}
