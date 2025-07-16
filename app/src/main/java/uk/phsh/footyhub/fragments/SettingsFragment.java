package uk.phsh.footyhub.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.interfaces.I_FragmentCallback;

public class SettingsFragment extends PreferenceFragmentCompat {

    private I_FragmentCallback _callback = null;

    public SettingsFragment() {
        super();
    }

    public SettingsFragment(I_FragmentCallback callback) {
        this._callback = callback;
    }

    /**
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state.
     * @param rootKey            If non-null, this preference fragment should be rooted at the
     *                           {@link PreferenceScreen} with this key.
     */
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    /**
     * Called when the fragment starts
     */
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        String favTeamName = sharedPreferences.getString("favouriteTeamName", "");
        String favTeamLogo = sharedPreferences.getString("favouriteTeamLogo", "");
        int favTeamID = sharedPreferences.getInt("favouriteTeamID", -1);
        Preference teamName = findPreference("favouriteTeamName");
        Preference teamLogo = findPreference("favouriteTeamLogo");
        Preference teamID = findPreference("favouriteTeamID");

        if (teamName != null)
            teamName.setSummary(getString(R.string.prefTeamNameSummary, favTeamName));
        if (teamLogo != null)
            teamLogo.setSummary(getString(R.string.prefTeamLogoSummary, favTeamLogo));
        if (teamID != null)
            teamID.setSummary(getString(R.string.prefTeamIdSummary, favTeamID));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(_callback != null)
            _callback.changeActionbarTitle(getString(R.string.settings_fragment_actionbar));
    }

}
