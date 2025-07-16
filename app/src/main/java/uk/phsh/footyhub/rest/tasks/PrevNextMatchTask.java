package uk.phsh.footyhub.rest.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import uk.phsh.footyhub.rest.enums.DateTimeType;
import uk.phsh.footyhub.rest.enums.FixtureType;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.Match;
import uk.phsh.footyhub.rest.models.RestResponse;
import uk.phsh.footyhub.rest.models.Score;
import uk.phsh.footyhub.rest.models.Team;

/**
 * Retrieves either the next or previous fixture for a specified team
 * @author Peter Blackburn
 */
public class PrevNextMatchTask extends BaseTask<Match> {

    private final int _teamID;
    private final FixtureType _type;

    /**
     * @param teamID The teamId for the requested fixture
     * @param type The FixtureType to be retrieved
     */
    public PrevNextMatchTask(int teamID, FixtureType type, I_TaskCallback<Match> callback) {
        super(callback);
        this._teamID = teamID;
        this._type = type;
    }

    /**
     * @return String The url of the http request
     */
    @Override
    public String getUrl() {
        return baseUrl + "teams/" + _teamID + "/matches?status=" + _type + "&limit=1";
    }

    /**
     * @return String The tag for this class
     */
    @Override
    public String getTag() {
        return "PrevNextMatchTask";
    }

    @Override
    public void onSuccess(RestResponse response) {
        if(parseMatch(response.getResponseBody(), _type) != null)
            getCallback().onSuccess(parseMatch(response.getResponseBody(), _type));
        else
            getCallback().onError("No matches found");
    }

    /**
     * Parses the JSON response from requests to the API for previous and next matches
     * @param json The JSON result returned from the API
     * @param type FINISHED fixture would be the previous fixture, SCHEDULED would be the next fixture
     */
    private Match parseMatch(String json, FixtureType type) {
        JsonObject baseObject = getBaseObject(json);
        JsonArray matches = returnDefaultNullArray(baseObject.get("matches"));
        Match m = new Match();
        if(!matches.isEmpty()) {
            JsonObject matchObject = returnDefaultNullObj(matches.get(0));
            JsonObject compObject = returnDefaultNullObj(matchObject.get("competition"));
            m.leagueID = returnDefaultNullInt(compObject.get("id"));
            m.leagueEmblem = returnDefaultNullString(compObject.get("emblem"));
            m.leagueName = returnDefaultNullString(compObject.get("name"));

            m.matchID = returnDefaultNullInt(matchObject.get("id"));

            String utcDate = returnDefaultNullString(matchObject.get("utcDate"));
            m.matchDate = dateTimeString(utcDate, DateTimeType.DATE);
            m.matchTime = dateTimeString(utcDate, DateTimeType.TIME);
            m.epochTime = dateTimeString(utcDate, DateTimeType.EPOCH);

            m.matchType = type;

            JsonObject homeObject = returnDefaultNullObj(matchObject.get("homeTeam"));
            m.homeTeam = getGson().fromJson(homeObject.toString(), Team.class);
            JsonObject awayObject = returnDefaultNullObj(matchObject.get("awayTeam"));
            m.awayTeam = getGson().fromJson(awayObject.toString(), Team.class);

            if(type == FixtureType.FINISHED) {
                JsonObject score = returnDefaultNullObj(matchObject.get("score"));
                JsonObject halfTime = returnDefaultNullObj(score.get("halfTime"));
                Score halfScore = new Score(returnDefaultNullInt(halfTime.get("home")),
                        returnDefaultNullInt(halfTime.get("away")));
                JsonObject fullTime = returnDefaultNullObj(score.get("fullTime"));
                Score fullScore = new Score(returnDefaultNullInt(fullTime.get("home")),
                        returnDefaultNullInt(fullTime.get("away")));
                m.halfTime = halfScore;
                m.fullTime = fullScore;
            }
        } else {
            return null;
        }
        return m;
    }
}
