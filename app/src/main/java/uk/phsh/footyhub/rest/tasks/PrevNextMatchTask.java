package uk.phsh.footyhub.rest.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;

import uk.phsh.footyhub.enums.DateTimeType;
import uk.phsh.footyhub.helpers.UtilityHelper;
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
        JsonArray matches = baseObject.getAsJsonArray("matches");
        Match m = new Match();
        if(!matches.isEmpty()) {
            JsonObject matchObject = matches.get(0).getAsJsonObject();
            JsonObject compObject = matchObject.getAsJsonObject("competition");
            m.leagueID = compObject.get("id").getAsInt();
            m.leagueEmblem = compObject.get("emblem").getAsString();
            m.leagueName = compObject.get("name").getAsString();

            m.matchID = matchObject.get("id").getAsInt();

            m.matchDate = UtilityHelper.getInstance().DateTimeString(matchObject.get("utcDate").getAsString(), DateTimeType.DATE);
            m.matchTime = UtilityHelper.getInstance().DateTimeString(matchObject.get("utcDate").getAsString(), DateTimeType.TIME);

            m.matchType = type;

            JsonObject homeObject = matchObject.getAsJsonObject("homeTeam");
            m.homeTeam = getGson().fromJson(homeObject.toString(), Team.class);
            JsonObject awayObject = matchObject.getAsJsonObject("awayTeam");
            m.awayTeam = getGson().fromJson(awayObject.toString(), Team.class);

            if(type == FixtureType.FINISHED) {
                JsonObject score = matchObject.getAsJsonObject("score");
                JsonObject halfTime = score.getAsJsonObject("halfTime");
                Score halfScore = new Score(halfTime.get("home").getAsInt(), halfTime.get("away").getAsInt());
                JsonObject fullTime = score.getAsJsonObject("fullTime");
                Score fullScore = new Score(fullTime.get("home").getAsInt(), fullTime.get("away").getAsInt());
                m.halfTime = halfScore;
                m.fullTime = fullScore;
            }
        } else {
            return null;
        }
        return m;
    }
}
