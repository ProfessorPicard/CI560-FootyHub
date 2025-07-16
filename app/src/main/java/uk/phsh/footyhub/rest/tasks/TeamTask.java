package uk.phsh.footyhub.rest.tasks;

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

        team.id = returnDefaultNullInt(baseObject.get("id"));
        team.shortName = returnDefaultNullString(baseObject.get("shortName"));
        team.name = returnDefaultNullString(baseObject.get("name"));
        team.tla = returnDefaultNullString(baseObject.get("tla"));
        team.crest = returnDefaultNullString(baseObject.get("crest"));
        team.address = returnDefaultNullString(baseObject.get("address"));
        team.founded = returnDefaultNullInt(baseObject.get("founded"));
        team.venue = returnDefaultNullString(baseObject.get("venue"));

        JsonObject coachObject = returnDefaultNullObj(baseObject.get("coach"));
        team.coach = returnDefaultNullString(coachObject.get("name"));

        getCallback().onSuccess(team);
    }

}
