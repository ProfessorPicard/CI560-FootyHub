package uk.phsh.footyhub.rest.models;

import java.util.TreeMap;

public class CompetitionFixtures {
    private final LeagueInfo _leagueInfo;
    private final TreeMap<Integer, MatchWeek> _matchWeeks = new TreeMap<>();

    public CompetitionFixtures(LeagueInfo leagueInfo) {
        this._leagueInfo = leagueInfo;
    }

    public void addToMatchDays(int matchWeek, Match match) {
            if(_matchWeeks.containsKey(matchWeek)) {
                MatchWeek mw = _matchWeeks.get(matchWeek);
                if(mw != null)
                    mw.addToMatchWeek(match);
                else {
                    mw = new MatchWeek(matchWeek);
                    _matchWeeks.put(matchWeek, mw);
                }
            } else {
                MatchWeek mw = new MatchWeek(matchWeek);
                mw.addToMatchWeek(match);
                _matchWeeks.put(matchWeek, mw);
            }
    }

    public LeagueInfo getLeagueInfo() { return _leagueInfo; }
    public TreeMap<Integer, MatchWeek> getMatchWeeks() { return _matchWeeks; }

}
