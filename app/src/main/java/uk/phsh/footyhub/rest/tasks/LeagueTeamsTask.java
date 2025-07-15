package uk.phsh.footyhub.rest.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import uk.phsh.footyhub.rest.enums.LeagueEnum;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.RestResponse;
import uk.phsh.footyhub.rest.models.Team;

/**
 * Retrieves the teams in a specified LeagueEnum
 * @author Peter Blackburn
 */
public class LeagueTeamsTask extends BaseTask<ArrayList<Team>> {

    private final LeagueEnum selectedLeague;

    /**
     * @param league The LeagueEnum to retrieve teams for
     */
    public LeagueTeamsTask(LeagueEnum league, I_TaskCallback<ArrayList<Team>> callback) {
        super(callback);
        this.selectedLeague = league;
    }

    /**
     * @return String The url of the http request
     */
    @Override
    public String getUrl() {
        return baseUrl + "competitions/" + selectedLeague.getLeagueCode() + "/teams";
    }

    /**
     * @return String The tag for this class
     */
    @Override
    public String getTag() {
        return "LeagueTeamsTask";
    }

    @Override
    public void onSuccess(RestResponse response) {
        ArrayList<Team> teams = new ArrayList<>();
        JsonObject baseObject = getBaseObject(response.getResponseBody());
        JsonArray teamsArray = baseObject.getAsJsonArray("teams");

        for(JsonElement teamElement : teamsArray) {
            JsonObject teamObject = teamElement.getAsJsonObject();
            Team team = new Team();
            team.id = teamObject.get("id").getAsInt();
            team.shortName = teamObject.get("shortName").getAsString();
            team.name = teamObject.get("name").getAsString();
            team.tla = teamObject.get("tla").getAsString();
            team.crest = teamObject.get("crest").getAsString();
            team.address = teamObject.get("address").getAsString();
            team.founded = teamObject.get("founded").getAsInt();
            team.venue = teamObject.get("venue").getAsString();
            teams.add(team);
        }
        getCallback().onSuccess(teams);
    }

}
