package uk.phsh.footyhub.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import com.squareup.picasso.Picasso;
import java.util.Locale;
import uk.phsh.footyhub.R;
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
    private CardView nextFixtureContainer;
    private CardView prevFixtureContainer;

    private TextView venueTxt;
    private TextView addressTxt;
    private TextView foundedTxt;
    private TextView coachTxt;
    private TextView nextFixtureTitle;
    private TextView nextFixtureDate;
    private TextView nextFixtureTime;
    private TextView prevFixtureTitle;
    private TextView prevFixtureDate;
    private TextView prevFixtureTime;
    private TextView prevFixtureHomeScore;
    private TextView prevFixtureAwayScore;

    private ImageView nextFixtureHomeImg;
    private ImageView nextFixtureAwayImg;
    private ImageView prevFixtureHomeImg;
    private ImageView prevFixtureAwayImg;
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

        nextFixtureTitle = v.findViewById(R.id.nextFixtureTitle);
        nextFixtureDate = v.findViewById(R.id.nextFixtureDate);
        nextFixtureTime = v.findViewById(R.id.nextFixtureTime);

        prevFixtureTitle = v.findViewById(R.id.prevFixtureTitle);
        prevFixtureHomeScore = v.findViewById(R.id.prevFixtureHomeScore);
        prevFixtureDate = v.findViewById(R.id.prevFixtureDate);
        prevFixtureTime = v.findViewById(R.id.prevFixtureTime);
        prevFixtureAwayScore = v.findViewById(R.id.prevFixtureAwayScore);

        nextFixtureHomeImg = v.findViewById(R.id.nextFixtureHomeImg);
        nextFixtureAwayImg = v.findViewById(R.id.nextFixtureAwayImg);
        prevFixtureHomeImg = v.findViewById(R.id.prevFixtureHomeImg);
        prevFixtureAwayImg = v.findViewById(R.id.prevFixtureAwayImg);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        RestManager rm = RestManager.getInstance(requireActivity().getCacheDir());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        int teamID = prefs.getInt("favouriteTeamID", -1);

        if(teamID != -1) {

            if (prefs.getBoolean("showDetails", false)) {
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
                    public void onRateLimitReached(int secondsRemaining) {

                    }

                    @Override
                    public void onError(String message) {
                        teamDetailsContainer.setVisibility(View.GONE);
                    }
                };
                rm.asyncTask(new TeamTask(teamID, teamCallback));
            } else {
                teamDetailsContainer.setVisibility(View.GONE);
            }

            if (prefs.getBoolean("showNext", false)) {
                I_TaskCallback<Match> nextCallback = new I_TaskCallback<>() {
                    @Override
                    public void onSuccess(Match value) {
                        nextFixtureContainer.setVisibility(View.VISIBLE);
                        nextFixtureTitle.setText(getString(R.string.nextFixtureTitle,value.homeTeam.tla ,value.awayTeam.tla));
                        Picasso.get().load(value.homeTeam.crest).into(nextFixtureHomeImg);
                        Picasso.get().load(value.awayTeam.crest).into(nextFixtureAwayImg);

                        nextFixtureDate.setText(value.matchDate);
                        nextFixtureTime.setText(value.matchTime);
                    }

                    @Override
                    public void onRateLimitReached(int secondsRemaining) {

                    }

                    @Override
                    public void onError(String message) {
                        nextFixtureContainer.setVisibility(View.GONE);
                    }
                };
                rm.asyncTask(new PrevNextMatchTask(teamID, FixtureType.SCHEDULED, nextCallback));
            } else {
                nextFixtureContainer.setVisibility(View.GONE);
            }

            if (prefs.getBoolean("showPrev", false)) {
                I_TaskCallback<Match> prevCallback = new I_TaskCallback<>() {
                    @Override
                    public void onSuccess(Match value) {
                        prevFixtureContainer.setVisibility(View.VISIBLE);
                        prevFixtureTitle.setText(String.format(String.valueOf(R.string.prevFixtureTitle), value.homeTeam.tla ,value.awayTeam.tla));
                        Picasso.get().load(value.homeTeam.crest).into(prevFixtureHomeImg);
                        Picasso.get().load(value.awayTeam.crest).into(prevFixtureAwayImg);

                        prevFixtureHomeScore.setText(String.format(Locale.UK, "%d", value.fullTime.homeScore));
                        prevFixtureAwayScore.setText(String.format(Locale.UK,"%d", value.fullTime.awayScore));

                        prevFixtureDate.setText(value.matchDate);
                        prevFixtureTime.setText(value.matchTime);
                    }

                    @Override
                    public void onRateLimitReached(int secondsRemaining) {

                    }

                    @Override
                    public void onError(String message) {
                        prevFixtureContainer.setVisibility(View.GONE);
                    }
                };
                rm.asyncTask(new PrevNextMatchTask(teamID, FixtureType.FINISHED, prevCallback));
            } else {
                prevFixtureContainer.setVisibility(View.GONE);
            }

//            if (prefs.getBoolean("showNews", false)) {
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
