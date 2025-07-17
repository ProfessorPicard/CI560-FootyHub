package uk.phsh.footyhub.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import androidx.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.adapters.TeamGridViewAdapter;
import uk.phsh.footyhub.helpers.UtilityHelper;
import uk.phsh.footyhub.interfaces.I_FragmentCallback;
import uk.phsh.footyhub.rest.RestManager;
import uk.phsh.footyhub.rest.enums.LeagueEnum;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.Team;
import uk.phsh.footyhub.rest.tasks.LeagueTeamsTask;

public class SelectTeamFragment extends BaseFragment implements I_TaskCallback<ArrayList<Team>> {

    private final ArrayList<Team> availableTeams = new ArrayList<>();
    private TeamGridViewAdapter _teamGridViewAdapter;
    private Team _favouriteTeam = null;
    private SharedPreferences.Editor editor;


    public SelectTeamFragment() { super(); }

    public SelectTeamFragment(I_FragmentCallback callBack) {
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
        v = inflater.inflate(R.layout.select_team_frag, container, false);
        return v;
    }

    /**
     * Called when the fragment starts
     */
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        editor = sharedPref.edit();
        availableTeams.clear();
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    editor.putString("favouriteTeamName", _favouriteTeam.shortName);
                    editor.putString("favouriteTeamNameLong", _favouriteTeam.name);
                    editor.putString("favouriteTeamLogo", _favouriteTeam.crest);
                    editor.putBoolean("favouriteTeamSelected", true);
                    editor.putInt("favouriteTeamID", _favouriteTeam.id);
                    editor.apply();
                    requireActivity().recreate();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    _favouriteTeam = null;
                    dialog.dismiss();
            }
        };

        GridView _teamGridView = requireActivity().findViewById(R.id.teamGridView);
        _teamGridViewAdapter = new TeamGridViewAdapter(getActivity(), availableTeams);
        _teamGridView.setAdapter(_teamGridViewAdapter);
        _teamGridView.setOnItemClickListener((parent, view, position, id) -> {
            _favouriteTeam = availableTeams.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
            AlertDialog dialog = builder.setMessage(getString(R.string.selectFavTeam,_favouriteTeam.shortName))
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setTextColor(getResources().getColor(R.color.primary_on, null));

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setTextColor(getResources().getColor(R.color.primary_on, null));

        });

        String teamsStr = PreferenceManager.getDefaultSharedPreferences(requireActivity()).getString("availableTeams", "");

        if(teamsStr.isEmpty()) {
            RestManager rm = RestManager.getInstance(requireActivity().getCacheDir());
            rm.submitTask(new LeagueTeamsTask(this, LeagueEnum.PREMIER_LEAGUE));
            rm.submitTask(new LeagueTeamsTask(this, LeagueEnum.CHAMPIONSHIP));
        } else {
            Gson gson = new Gson();
            JsonElement element = JsonParser.parseString(teamsStr);
            JsonArray teams = element.getAsJsonArray();
            for(JsonElement e : teams) {
                Team team = gson.fromJson(e.toString(), Team.class);
                availableTeams.add(team);
            }
            updateTeams();
        }
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.select_fragment_actionbar);
    }

    private void updateTeams() {
        _teamGridViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(ArrayList<Team> value) {
        UtilityHelper.getInstance().runOnUiThread(_context, () -> {
            availableTeams.addAll(value);
            availableTeams.sort((t1, t2) -> {
                String s1 = t1.shortName;
                String s2 = t2.shortName;
                return s1.compareToIgnoreCase(s2);
            });
            Gson gson = new Gson();
            String json = gson.toJson(availableTeams);
            editor.putString("availableTeams", json);
            editor.apply();
            updateTeams();
        });

    }

    @Override
    public void onError(String message) {
        UtilityHelper.getInstance().runOnUiThread(_context, () -> {

        });
    }
}
