package uk.phsh.footyhub.rest.models;

import java.util.ArrayList;

/**
 * Model class for a LeagueStanding rest response
 * @author Peter Blackburn
 */
public class LeagueStanding {

    private final ArrayList<PositionInfo> _positions = new ArrayList<>();
    private LeagueInfo _leagueInfo;

    /**
     * @param leagueInfo The LeagueInfo belonging to the league
     */
    public LeagueStanding(LeagueInfo leagueInfo) {
        this._leagueInfo = leagueInfo;
    }

    public LeagueStanding() { }

    /**
     * Adds a PositionInfo to an ArrayList of PositionInfo's
     * @param positionInfo Add a PositionInfo to the list
     */
    public void addPosition(PositionInfo positionInfo) {
        if(!_positions.contains(positionInfo))
            _positions.add(positionInfo);
    }

    /**
     * Get the PositionInfo at a certain index
     * @param position Get the PositionInfo at the specified list position
     */
    public PositionInfo getPositionInfoByPosition(int position) {
        for(PositionInfo p : _positions) {
            if(p.position == position)
                return p;
        }
        return null;
    }

    /**
     * Get the PositionInfo for a certain team
     * @param team The team model to get the PositionInfo for
     */
    public PositionInfo getPositionInfoByTeam(Team team) {
        for(PositionInfo p : _positions) {
            if(team.id == p.team.id)
                return p;
        }
        return null;
    }


    /**
     * Returns all PositionInfo as an ArrayList
     * @return ArrayList<PositionInfo> Returns a list of PositionInfo
     */
    public ArrayList<PositionInfo> getAllPositionInfo() { return _positions; }

    /**
     * Attach a specified LeagueInfo to the standings
     * @param leagueInfo The LeagueInfo to attach to the standings
     */
    public void setLeagueInfo(LeagueInfo leagueInfo) {
        this._leagueInfo = leagueInfo;
    }

    /**
     * Gets the LeagueInfo attached to the LeagueStandings
     * @return LeagueInfo Get the LeagueInfo attached to the standings
     */
    public LeagueInfo getLeagueInfo() { return _leagueInfo; }

}
