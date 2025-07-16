package uk.phsh.footyhub.rest.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import uk.phsh.footyhub.rest.enums.LeagueEnum;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.LeagueInfo;
import uk.phsh.footyhub.rest.models.LeagueStanding;
import uk.phsh.footyhub.rest.models.PositionInfo;
import uk.phsh.footyhub.rest.models.RestResponse;

/**
 * Retrieves the League Standings for a specified LeagueEnum
 * @author Peter Blackburn
 */
public class LeagueStandingsTask extends BaseTask<LeagueStanding> {

    private final LeagueEnum selectedLeague;

    /**
     * @param league The LeagueEnum to retrieve standings for
     */
    public LeagueStandingsTask(LeagueEnum league, I_TaskCallback<LeagueStanding> callback) {
        super(callback);
        this.selectedLeague = league;
    }

    /**
     * @return String The url of the http request
     */
    @Override
    public String getUrl() {
        return baseUrl + "competitions/" + selectedLeague.getLeagueCode() + "/standings";
    }

    /**
     * @return String The tag for this class
     */
    @Override
    public String getTag() {
        return "LeagueStandingsTask";
    }

    @Override
    public void onSuccess(RestResponse response) {
        LeagueStanding leagueStandings = new LeagueStanding();
        JsonObject baseObject = getBaseObject(response.getResponseBody());

        JsonArray standings = returnDefaultNullArray(baseObject.get("standings"));
        JsonObject season = returnDefaultNullObj(baseObject.get("season"));
        JsonObject comp = returnDefaultNullObj(baseObject.get("competition"));

        LeagueInfo info = new LeagueInfo();
        info.emblem = returnDefaultNullString(comp.get("emblem"));
        info.name = returnDefaultNullString(comp.get("name"));
        info.startDate = returnDefaultNullString(season.get("startDate"));
        info.endDate = returnDefaultNullString(season.get("endDate"));
        info.matchDay= returnDefaultNullInt(season.get("currentMatchday"));

        leagueStandings.setLeagueInfo(info);

        JsonArray table = returnDefaultNullArray(returnDefaultNullObj(standings.get(0)).get("table"));

        for(JsonElement teamElement : table) {
            PositionInfo positionInfo = getGson().fromJson(teamElement.toString(), PositionInfo.class);
            leagueStandings.addPosition(positionInfo);
        }

        getCallback().onSuccess(leagueStandings);
    }

}
