package uk.phsh.footyhub.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.TreeMap;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.adapters.FixtureAdapter;
import uk.phsh.footyhub.adapters.LeagueSpinnerAdapter;
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
    private FixtureAdapter _fixtureAdapter;
    private RecyclerView _fixtureRecycler;
    private Spinner _leagueSpinner;
    private EditText _matchWeekTxt;
    private Button _filterButton;
    private SharedPreferences.Editor editor;
    private boolean _updating = false;

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
        _leagueSpinner = v.findViewById(R.id.fixtureFilterLeague);
        _filterButton = v.findViewById(R.id.fixtureFilterButton);
        _matchWeekTxt = v.findViewById(R.id.fixtureFilterMatchweek);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        editor = sharedPref.edit();

        int filterLeaguePosition = sharedPref.getInt("fixtureFilterLeaguePosition",0);
        int filterMatchWeek = sharedPref.getInt("fixtureFilterMatchWeek",-1);

        LeagueSpinnerAdapter _spinnerAdapter = new LeagueSpinnerAdapter(getActivity(), LeagueEnum.values());
        _leagueSpinner.setAdapter(_spinnerAdapter);
        _leagueSpinner.setSelection(filterLeaguePosition);
        _matchWeekTxt.setText((filterMatchWeek == -1) ? "" : "" + filterMatchWeek);

        LeagueEnum league = (LeagueEnum) _leagueSpinner.getSelectedItem();

        _filterButton.setOnClickListener(v -> {
            int matchWeek = -1;
            if(_matchWeekTxt.getText().length() > 0)
                matchWeek = Integer.parseInt(_matchWeekTxt.getText().toString());
            LeagueEnum league1 = (LeagueEnum) _leagueSpinner.getSelectedItem();
            int leaguePosition = _leagueSpinner.getSelectedItemPosition();
            editor.putInt("fixtureFilterLeaguePosition", leaguePosition);
            editor.putInt("fixtureFilterMatchWeek", matchWeek);
            editor.apply();
            _matchWeekTxt.clearFocus();
            updateMatches(league1, matchWeek);
        });

        _matchWeekTxt.setOnFocusChangeListener((view, focused) -> {
            InputMethodManager keyboard = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (focused)
                keyboard.showSoftInput(_matchWeekTxt, 0);
            else
                keyboard.hideSoftInputFromWindow(_matchWeekTxt.getWindowToken(), 0);
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        _fixtureAdapter = new FixtureAdapter(_matchWeeks, getContext());
        _fixtureRecycler.setLayoutManager(layoutManager);
        _matchWeeks.clear();
        _fixtureRecycler.setAdapter(_fixtureAdapter);

        updateMatches(league, filterMatchWeek);
    }

    private void updateMatches(LeagueEnum league, int matchWeek) {
        if(!_updating) {
            _updating = true;
            RestManager rm = RestManager.getInstance(requireActivity().getCacheDir());
            rm.submitTask(new FixturesTask(this, league, matchWeek));
        }
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
            _fixtureAdapter.notifyDataSetChanged();
            _matchWeeks.addAll(matchWeeks.values());
            _fixtureAdapter.notifyDataSetChanged();

            _updating = false;
        });
    }

    @Override
    public void onError(String message) {

        _updating = false;
    }
}
