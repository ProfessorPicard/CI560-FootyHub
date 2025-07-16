package uk.phsh.footyhub.rest.enums;

/**
 * The type of league used in searches. Contains league name and code.
 * @author Peter Blackburn
 */
public enum LeagueEnum {
    PREMIER_LEAGUE ("Premier League", "PL"), CHAMPIONSHIP ("Championship", "ELC");

    private final String leagueCode;
    private final String readableName;
    LeagueEnum(String readableName, String code) {
        this.leagueCode = code;
        this.readableName = readableName;
    }

    /**
     * Gets the specified LeagueEnum code
     * @return String The code for the specified League
     */
    public String getLeagueCode() { return leagueCode; }

    /**
     * Gets the specified LeagueEnum readable name
     * @return String The readable for the specified League
     */
    public String getReadableName() { return readableName; }

    /**
     * Retrieves a LeagueEnum from the specified index
     * @param position The index of the league to be returned
     * @return LeagueEnum The requested LeagueEnum at a position
     */
    public static LeagueEnum getLeagueEnum(int position) {
        return values()[position];
    }

    /**
     * Overrides toString to return the Readable name
     * @return String The readable name
     */
    @Override
    public String toString() {
        return getReadableName();
    }
}
