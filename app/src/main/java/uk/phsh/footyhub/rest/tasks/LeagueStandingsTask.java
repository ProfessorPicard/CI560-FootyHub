package uk.phsh.footyhub.rest.tasks;

import android.content.Context;

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
    public LeagueStandingsTask(LeagueEnum league, I_TaskCallback<LeagueStanding> callback, Context context) {
        super(callback, context);
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

        JsonArray standings = baseObject.getAsJsonArray("standings");
        JsonObject season = baseObject.getAsJsonObject("season");
        JsonObject comp = baseObject.getAsJsonObject("competition");

        LeagueInfo info = new LeagueInfo();
        info.emblem = comp.get("emblem").getAsString();
        info.name = comp.get("name").getAsString();
        info.startDate = season.get("startDate").getAsString();
        info.endDate = season.get("endDate").getAsString();
        info.matchDay= season.get("currentMatchday").getAsInt();

        leagueStandings.setLeagueInfo(info);

        JsonArray table = standings.get(0).getAsJsonObject().getAsJsonArray("table");

        for(JsonElement teamElement : table) {
            PositionInfo positionInfo = getGson().fromJson(teamElement.toString(), PositionInfo.class);
            leagueStandings.addPosition(positionInfo);
        }

        getCallback().onSuccess(leagueStandings);
    }

}
