package uk.phsh.footyhub.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import com.squareup.picasso.Picasso;
import java.util.Locale;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.controls.FixtureControl;
import uk.phsh.footyhub.interfaces.I_FragmentCallback;
import uk.phsh.footyhub.rest.RestManager;
import uk.phsh.footyhub.rest.enums.FixtureType;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.Match;
import uk.phsh.footyhub.rest.models.Team;
import uk.phsh.footyhub.rest.tasks.PrevNextMatchTask;
import uk.phsh.footyhub.rest.tasks.TeamTask;

public class HomeFragment extends BaseFragment {

    private CardView teamDetailsContainer;
    private FixtureControl nextFixtureContainer;
    private FixtureControl prevFixtureContainer;

    private TextView venueTxt;
    private TextView addressTxt;
    private TextView foundedTxt;
    private TextView coachTxt;

//    private TextView nextFixtureTitle;
//    private TextView nextFixtureDate;
//    private TextView nextFixtureTime;
//    private TextView prevFixtureTitle;
//    private TextView prevFixtureDate;
//    private TextView prevFixtureTime;
//    private TextView prevFixtureHomeScore;
//    private TextView prevFixtureAwayScore;

//    private ImageView nextFixtureHomeImg;
//    private ImageView nextFixtureAwayImg;
//    private ImageView prevFixtureHomeImg;
//    private ImageView prevFixtureAwayImg;
    private ImageView teamDetailsImg;

    public HomeFragment() { super(); }

    public HomeFragment(I_FragmentCallback callBack) {
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
        v = inflater.inflate(R.layout.home_frag, container, false);

        teamDetailsContainer = v.findViewById(R.id.teamDetailsContainer);
        nextFixtureContainer = v.findViewById(R.id.nextFixtureContainer);
        prevFixtureContainer = v.findViewById(R.id.previousFixtureContainer);

        venueTxt = v.findViewById(R.id.venueTxt);
        addressTxt = v.findViewById(R.id.addressTxt);
        foundedTxt = v.findViewById(R.id.foundedTxt);
        coachTxt = v.findViewById(R.id.coachTxt);
        teamDetailsImg = v.findViewById(R.id.teamDetailsImg);

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();
        RestManager rm = RestManager.getInstance(requireActivity().getCacheDir());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        int teamID = prefs.getInt("favouriteTeamID", -1);
        boolean showDetails = prefs.getBoolean("showDetails", false);
        boolean showPrevResult = prefs.getBoolean("showPrev", false);
        boolean showNextFixture = prefs.getBoolean("showNext", false);
//        boolean showNews = prefs.getBoolean("showNews", false);

        if(teamID != -1) {

            if (showDetails) {
                I_TaskCallback<Team> teamCallback = new I_TaskCallback<>() {
                    @Override
                    public void onSuccess(Team value) {
                        teamDetailsContainer.setVisibility(View.VISIBLE);
                        venueTxt.setText(value.venue);
                        addressTxt.setText(value.address);
                        foundedTxt.setText(String.format(Locale.UK, "%d", value.founded));
                        coachTxt.setText(value.coach);
                        Picasso.get().load(value.crest).into(teamDetailsImg);
                    }

                    @Override
                    public void onError(String message) {
                        teamDetailsContainer.setVisibility(View.GONE);
                    }
                };
                rm.asyncTask(new TeamTask(teamID, teamCallback, getContext()));
            } else {
                teamDetailsContainer.setVisibility(View.GONE);
            }

            if (showNextFixture) {
                nextFixtureContainer.setVisibility(View.VISIBLE);
                I_TaskCallback<Match> nextCallback = new I_TaskCallback<>() {
                    @Override
                    public void onSuccess(Match value) {
                        nextFixtureContainer.showError(false);
                        nextFixtureContainer.setTitle(getString(R.string.nextFixtureTitle,value.homeTeam.tla ,value.awayTeam.tla));
                        nextFixtureContainer.setHomeImgSrc(value.homeTeam.crest);
                        nextFixtureContainer.setAwayImgSrc(value.awayTeam.crest);
                        nextFixtureContainer.setFixtureDate(value.matchDate);
                        nextFixtureContainer.setFixtureTime(value.matchTime);
                    }

                    @Override
                    public void onError(String message) {
                        nextFixtureContainer.setErrorMessage(message);
                        nextFixtureContainer.showError(true);
                    }
                };
                rm.asyncTask(new PrevNextMatchTask(teamID, FixtureType.SCHEDULED, nextCallback, getContext()));
            } else {
                nextFixtureContainer.setVisibility(View.GONE);
            }

            if (showPrevResult) {
                prevFixtureContainer.setVisibility(View.VISIBLE);
                I_TaskCallback<Match> prevCallback = new I_TaskCallback<>() {
                    @Override
                    public void onSuccess(Match value) {
                        prevFixtureContainer.showError(false);
                        prevFixtureContainer.setTitle(getString(R.string.prevFixtureTitle,value.homeTeam.tla ,value.awayTeam.tla));
                        prevFixtureContainer.setHomeImgSrc(value.homeTeam.crest);
                        prevFixtureContainer.setAwayImgSrc(value.awayTeam.crest);
                        prevFixtureContainer.setHomeScore(value.fullTime.homeScore);
                        prevFixtureContainer.setAwayScore(value.fullTime.awayScore);
                        prevFixtureContainer.setFixtureDate(value.matchDate);
                        prevFixtureContainer.setFixtureTime(value.matchTime);
                    }

                    @Override
                    public void onError(String message) {
                        prevFixtureContainer.setErrorMessage(message);
                        prevFixtureContainer.showError(true);
                    }
                };
                rm.asyncTask(new PrevNextMatchTask(teamID, FixtureType.FINISHED, prevCallback, getContext()));
            } else {
                prevFixtureContainer.setVisibility(View.GONE);
            }

//            if (showNews) {
//                // Do Stuff
//            } else {
//                // Dont Do Stuff
//            }
        }
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.home_fragment_actionbar);
    }

}
