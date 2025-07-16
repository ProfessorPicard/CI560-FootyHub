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
    public LeagueTeamsTask(I_TaskCallback<ArrayList<Team>> callback, LeagueEnum league) {
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
        JsonArray teamsArray = returnDefaultNullArray(baseObject.get("teams"));

        for(JsonElement teamElement : teamsArray) {
            JsonObject teamObject = returnDefaultNullObj(teamElement);
            Team team = new Team();
            team.id = returnDefaultNullInt(teamObject.get("id"));
            team.shortName = returnDefaultNullString(teamObject.get("shortName"));
            team.name = returnDefaultNullString(teamObject.get("name"));
            team.tla = returnDefaultNullString(teamObject.get("tla"));
            team.crest = returnDefaultNullString(teamObject.get("crest"));
            team.address = returnDefaultNullString(teamObject.get("address"));
            team.founded = returnDefaultNullInt(teamObject.get("founded"));
            team.venue = returnDefaultNullString(teamObject.get("venue"));
            teams.add(team);
        }
        getCallback().onSuccess(teams);
    }

}
