package uk.phsh.footyhub.rest.models;

/**
 * Model class for Score Json
 * @author Peter Blackburn
 */
public class Score {
    public int homeScore;
    public int awayScore;

    /**
     * @param homeScore Home team score as int
     * @param awayScore Away team score as int
     */
    public Score(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }
}
