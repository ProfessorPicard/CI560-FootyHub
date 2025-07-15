package uk.phsh.footyhub.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Locale;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.adapters.NewsAdapter;
import uk.phsh.footyhub.controls.FixtureControl;
import uk.phsh.footyhub.helpers.UtilityHelper;
import uk.phsh.footyhub.interfaces.I_FragmentCallback;
import uk.phsh.footyhub.rest.RestManager;
import uk.phsh.footyhub.rest.enums.FixtureType;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.Match;
import uk.phsh.footyhub.rest.models.NewsArticle;
import uk.phsh.footyhub.rest.models.Team;
import uk.phsh.footyhub.rest.tasks.NewsSearchTask;
import uk.phsh.footyhub.rest.tasks.PrevNextMatchTask;
import uk.phsh.footyhub.rest.tasks.TeamTask;

public class HomeFragment extends BaseFragment {

    private CardView teamDetailsContainer;
    private FixtureControl nextFixtureContainer;
    private FixtureControl prevFixtureContainer;
    private LinearLayout newsContainer;
    private TextView venueTxt;
    private TextView addressTxt;
    private TextView foundedTxt;
    private TextView coachTxt;
    private RecyclerView _smallNewsRecycler;
    private ImageView teamDetailsImg;
    private NewsAdapter _articleAdapter;
    private final ArrayList<NewsArticle> _newsArticles = new ArrayList<>();


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
        newsContainer = v.findViewById(R.id.newsContainer);

        _smallNewsRecycler = v.findViewById(R.id.smallNewsRecycler);

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
        _context = context;
    }

    @Override
    public void onStart() {
        super.onStart();

        RestManager rm = RestManager.getInstance(requireActivity().getCacheDir());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        int teamID = prefs.getInt("favouriteTeamID", -1);
        String teamName = prefs.getString("favouriteTeamName", "");
        boolean showDetails = prefs.getBoolean("showDetails", false);
        boolean showPrevResult = prefs.getBoolean("showPrev", false);
        boolean showNextFixture = prefs.getBoolean("showNext", false);
        boolean showNews = prefs.getBoolean("showNews", false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        _smallNewsRecycler.setLayoutManager(layoutManager);
        _newsArticles.clear();
        _articleAdapter = new NewsAdapter(_newsArticles, getContext());
        _smallNewsRecycler.setAdapter(_articleAdapter);

        if(teamID != -1) {

            if (showDetails) {
                I_TaskCallback<Team> teamCallback = new I_TaskCallback<>() {
                    @Override
                    public void onSuccess(Team team) {
                        UtilityHelper.getInstance().runOnUiThread(_context, () -> {
                            teamDetailsContainer.setVisibility(View.VISIBLE);
                            venueTxt.setText(team.venue);
                            addressTxt.setText(team.address);
                            foundedTxt.setText(String.format(Locale.UK, "%d", team.founded));
                            coachTxt.setText(team.coach);
                            Picasso.get().load(team.crest).into(teamDetailsImg);
                        });
                    }

                    @Override
                    public void onError(String message) {
                        requireActivity().runOnUiThread(() -> teamDetailsContainer.setVisibility(View.GONE));
                    }
                };
                rm.asyncTask(new TeamTask(teamID, teamCallback));
            } else {
                teamDetailsContainer.setVisibility(View.GONE);
            }

            if (showNextFixture) {
                nextFixtureContainer.setVisibility(View.VISIBLE);
                I_TaskCallback<Match> nextCallback = new I_TaskCallback<>() {
                    @Override
                    public void onSuccess(Match match) {
                        UtilityHelper.getInstance().runOnUiThread(_context, () -> {
                            nextFixtureContainer.showError(false);
                            nextFixtureContainer.setTitle(_context.getString(R.string.nextFixtureTitle,match.homeTeam.tla ,match.awayTeam.tla));
                            nextFixtureContainer.setHomeImgSrc(match.homeTeam.crest);
                            nextFixtureContainer.setAwayImgSrc(match.awayTeam.crest);
                            nextFixtureContainer.setFixtureDate(match.matchDate);
                            nextFixtureContainer.setFixtureTime(match.matchTime);
                        });
                    }

                    @Override
                    public void onError(String message) {
                        UtilityHelper.getInstance().runOnUiThread(_context, () -> {
                            nextFixtureContainer.setErrorMessage(message);
                            nextFixtureContainer.showError(true);
                        });
                    }
                };
                rm.asyncTask(new PrevNextMatchTask(teamID, FixtureType.SCHEDULED, nextCallback));
            } else {
                nextFixtureContainer.setVisibility(View.GONE);
            }

            if (showPrevResult) {
                prevFixtureContainer.setVisibility(View.VISIBLE);
                I_TaskCallback<Match> prevCallback = new I_TaskCallback<>() {
                    @Override
                    public void onSuccess(Match match) {
                        UtilityHelper.getInstance().runOnUiThread(_context, () -> {
                            prevFixtureContainer.showError(false);
                            prevFixtureContainer.setTitle(_context.getString(R.string.prevFixtureTitle, match.homeTeam.tla, match.awayTeam.tla));
                            prevFixtureContainer.setHomeImgSrc(match.homeTeam.crest);
                            prevFixtureContainer.setAwayImgSrc(match.awayTeam.crest);
                            prevFixtureContainer.setHomeScore(match.fullTime.homeScore);
                            prevFixtureContainer.setAwayScore(match.fullTime.awayScore);
                            prevFixtureContainer.setFixtureDate(match.matchDate);
                            prevFixtureContainer.setFixtureTime(match.matchTime);
                        });
                    }

                    @Override
                    public void onError(String message) {
                        UtilityHelper.getInstance().runOnUiThread(_context, () -> {
                            prevFixtureContainer.setErrorMessage(message);
                            prevFixtureContainer.showError(true);
                        });
                    }
                };
                rm.asyncTask(new PrevNextMatchTask(teamID, FixtureType.FINISHED, prevCallback));
            } else {
                prevFixtureContainer.setVisibility(View.GONE);
            }

            if (showNews) {
                newsContainer.setVisibility(View.VISIBLE);
                I_TaskCallback<ArrayList<NewsArticle>> newsCallback = new I_TaskCallback<>() {

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(ArrayList<NewsArticle> articles) {
                        UtilityHelper.getInstance().runOnUiThread(_context, () -> {
                            _newsArticles.clear();
                            _articleAdapter.notifyDataSetChanged();
                            _newsArticles.addAll(articles);
                            _articleAdapter.notifyDataSetChanged();
                        });
                    }

                    @Override
                    public void onError(String message) {
                        UtilityHelper.getInstance().runOnUiThread(_context, () -> {
                            System.out.println(message);
                        });
                    }
                };
                rm.asyncTask(new NewsSearchTask(teamName, newsCallback, 5));
            } else {
                newsContainer.setVisibility(View.GONE);
            }
        } else {
            teamDetailsContainer.setVisibility(View.GONE);
            nextFixtureContainer.setVisibility(View.GONE);
            prevFixtureContainer.setVisibility(View.GONE);
            newsContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.home_fragment_actionbar);
    }


}
