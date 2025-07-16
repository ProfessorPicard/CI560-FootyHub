package uk.phsh.footyhub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.adapters.StandingsListViewAdapter;
import uk.phsh.footyhub.adapters.LeagueSpinnerAdapter;
import uk.phsh.footyhub.helpers.UtilityHelper;
import uk.phsh.footyhub.interfaces.I_FragmentCallback;
import uk.phsh.footyhub.rest.RestManager;
import uk.phsh.footyhub.rest.enums.LeagueEnum;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.LeagueStanding;
import uk.phsh.footyhub.rest.models.PositionInfo;
import uk.phsh.footyhub.rest.tasks.LeagueStandingsTask;

public class StandingsFragment extends BaseFragment implements I_TaskCallback<LeagueStanding> {

    private final ArrayList<PositionInfo> _positions = new ArrayList<>();
    private StandingsListViewAdapter _adapter;
    private LeagueEnum _selectedLeague;

    public StandingsFragment() { super(); }

    public StandingsFragment(I_FragmentCallback callBack) {
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
        v = inflater.inflate(R.layout.standings_frag, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _positions.clear();

        ListView _standingsList = requireActivity().findViewById(R.id.standingsList);
        _adapter = new StandingsListViewAdapter(getActivity(), _positions);
        _standingsList.setAdapter(_adapter);

        Spinner _standingsSpinner = requireActivity().findViewById(R.id.standingsSpinner);
        LeagueSpinnerAdapter _spinnerAdapter = new LeagueSpinnerAdapter(getActivity(), LeagueEnum.values());
        _standingsSpinner.setAdapter(_spinnerAdapter);
        _standingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _selectedLeague = LeagueEnum.getLeagueEnum(position);
                refreshStandings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        _standingsSpinner.setSelection(0);
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.standings_fragment_actionbar);
    }

    private void refreshStandings() {
        _positions.clear();
        RestManager rm = RestManager.getInstance(requireActivity().getCacheDir());
        rm.asyncTask(new LeagueStandingsTask(_selectedLeague, this));
    }

    @Override
    public void onSuccess(LeagueStanding value) {
        UtilityHelper.getInstance().runOnUiThread(_context, () -> {
            _positions.addAll(value.getAllPositionInfo());

            _positions.sort((p1, p2) -> {
                int pInt1 = p1.position;
                int pInt2 = p2.position;
                return Integer.compare(pInt1, pInt2);
            });
            updateList();
        });

    }

    private void updateList() {
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String message) {
        UtilityHelper.getInstance().runOnUiThread(_context, () -> {

        });
    }
}
