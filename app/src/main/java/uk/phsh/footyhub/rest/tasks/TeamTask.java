package uk.phsh.footyhub.rest.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.RestResponse;
import uk.phsh.footyhub.rest.models.Team;

public class TeamTask extends BaseTask<Team> {

    private final int _teamID;

    /**
     * @param callback Generic callback to be used to receive responses
     */
    public TeamTask(int teamID, I_TaskCallback<Team> callback) {
        super(callback);
        this._teamID = teamID;
    }

    @Override
    public String getUrl() {
        return baseUrl + "teams/" + _teamID;
    }

    @Override
    public String getTag() {
        return "TeamTask";
    }

    @Override
    public void onSuccess(RestResponse response) {
        Team team = new Team();
        JsonObject baseObject = getBaseObject(response.getResponseBody());

        team.id = baseObject.get("id").getAsInt();
        team.shortName = baseObject.get("shortName").getAsString();
        team.name = baseObject.get("name").getAsString();
        team.tla = baseObject.get("tla").getAsString();
        team.crest = baseObject.get("crest").getAsString();
        team.address = baseObject.get("address").getAsString();
        team.founded = baseObject.get("founded").getAsInt();
        team.venue = baseObject.get("venue").getAsString();

        JsonObject coachObject = baseObject.getAsJsonObject("coach");
        team.coach = coachObject.get("name").getAsString();

        getCallback().onSuccess(team);
    }

}
