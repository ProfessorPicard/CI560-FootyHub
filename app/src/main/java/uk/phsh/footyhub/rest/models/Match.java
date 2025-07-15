package uk.phsh.footyhub.rest.models;

import uk.phsh.footyhub.rest.enums.FixtureType;

/**
 * Model class for Match Json
 * @author Peter Blackburn
 */
public class Match {
    public String leagueName;
    public String leagueEmblem;
    public int leagueID;
    public int matchID;
    public FixtureType matchType;
    public Team homeTeam;
    public Team awayTeam;
    public Score halfTime;
    public Score fullTime;
    public String matchDate;
    public String matchTime;
    public String epochTime;
}
