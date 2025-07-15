package uk.phsh.footyhub.rest.models;

import java.util.ArrayList;
import java.util.TreeMap;

public class MatchWeek {

    private final TreeMap<String, ArrayList<Match>> _matches;
    private final int _matchWeek;

    public MatchWeek(int matchWeek) {
        this._matchWeek = matchWeek;
        _matches = new TreeMap<>();
    }

    public void addToMatchWeek(Match match) {
        if(_matches.containsKey(match.epochTime)) {
            ArrayList<Match> matches = _matches.get(match.epochTime);
            if(matches != null) {
                if(!matches.contains(match))
                    matches.add(match);
            } else {
                matches = new ArrayList<>();
                matches.add(match);
            }
        } else {
            ArrayList<Match> matches = new ArrayList<>();
            matches.add(match);
            _matches.put(match.epochTime, matches);
        }

    }

    public Match getMatchByID(int matchID) {
        for(ArrayList<Match> ma : _matches.values()) {
            for(Match m : ma) {
                if(m.matchID == matchID)
                    return m;
            }
        }
        return null;
    }

    public TreeMap<String, ArrayList<Match>> getAllMatches() {
        return _matches;
    }

    public ArrayList<Match> getMatchesForDate(String date) {
        if(_matches.containsKey(date))
            return _matches.get(date);
        return null;
    }

    public int getMatchWeek() { return _matchWeek; }

}
