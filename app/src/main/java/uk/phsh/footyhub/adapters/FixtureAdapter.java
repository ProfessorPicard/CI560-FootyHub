package uk.phsh.footyhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.rest.models.Match;
import uk.phsh.footyhub.rest.models.MatchWeek;

public class FixtureAdapter extends RecyclerView.Adapter<FixtureAdapter.FixtureViewHolder> {

    private final List<MatchWeek> _matchWeeks;
    private final Context _context;

    public FixtureAdapter(List<MatchWeek> matchWeeks, Context context){
        this._matchWeeks = matchWeeks;
        this._context = context;
    }

    @NonNull
    @Override
    public FixtureViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.match_week_layout, viewGroup, false);
        return new FixtureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FixtureViewHolder viewHolder, int i) {
        MatchWeek matchWeek = _matchWeeks.get(i);

        viewHolder.matchWeekHeader.setText(_context.getString(R.string.fixtureMatchweekHeader, matchWeek.getMatchWeek()));

        TreeMap<String, ArrayList<Match>> matchDates = matchWeek.getAllMatches();
        viewHolder.matchWeekDateContainer.removeAllViews();

        for(ArrayList<Match> matches : matchDates.values()) {
            View child = LayoutInflater.from(_context).inflate(R.layout.match_date_layout, viewHolder.matchWeekDateContainer, false);
            viewHolder.matchWeekDateContainer.addView(child);
            TextView dateTitle = child.findViewById(R.id.matchWeekDateHeader);
            LinearLayoutCompat matchWeekDateMatchContainer = child.findViewById(R.id.matchWeekDateMatchContainer);

            dateTitle.setText(matches.get(0).matchDate);

            for(Match m : matches) {
                View childMatch = LayoutInflater.from(_context).inflate(R.layout.match_date_fixture_layout, matchWeekDateMatchContainer, false);
                matchWeekDateMatchContainer.addView(childMatch);
                TextView homeTeam = childMatch.findViewById(R.id.matchDateFixtureHomeTxt);
                TextView awayTeam = childMatch.findViewById(R.id.matchDateFixtureAwayTxt);
                homeTeam.setText(m.homeTeam.shortName);
                awayTeam.setText(m.awayTeam.shortName);

                ImageView homeImg = childMatch.findViewById(R.id.matchDateFixtureHomeImg);
                ImageView awayImg = childMatch.findViewById(R.id.matchDateFixtureAwayImg);
                Picasso.get().load(m.homeTeam.crest).into(homeImg);
                Picasso.get().load(m.awayTeam.crest).into(awayImg);

                TextView score = childMatch.findViewById(R.id.matchDateFixtureScore);
                TextView time = childMatch.findViewById(R.id.matchDateFixtureTime);

                switch (m.matchType) {
                    case FINISHED:
                        score.setVisibility(View.VISIBLE);
                        score.setText(_context.getString(R.string.fixtureScoreString, m.fullTime.homeScore, m.fullTime.awayScore));
                        time.setVisibility(View.GONE);
                        break;
                    case SCHEDULED:
                        score.setVisibility(View.GONE);
                        time.setVisibility(View.VISIBLE);
                        time.setText(m.matchTime);
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return _matchWeeks.size();
    }

    public static class FixtureViewHolder extends RecyclerView.ViewHolder{

        protected LinearLayoutCompat matchWeekDateContainer;
        protected TextView matchWeekHeader;

        public FixtureViewHolder(View itemView) {
            super(itemView);

            matchWeekHeader = itemView.findViewById(R.id.matchWeekHeaderTxt);
            matchWeekDateContainer = itemView.findViewById(R.id.matchWeekDateContainer);
        }
    }
}


