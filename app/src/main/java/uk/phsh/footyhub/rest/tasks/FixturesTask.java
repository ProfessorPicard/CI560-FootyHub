package uk.phsh.footyhub.rest.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import uk.phsh.footyhub.rest.enums.DateTimeType;
import uk.phsh.footyhub.rest.enums.FixtureType;
import uk.phsh.footyhub.rest.enums.LeagueEnum;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.CompetitionFixtures;
import uk.phsh.footyhub.rest.models.LeagueInfo;
import uk.phsh.footyhub.rest.models.Match;
import uk.phsh.footyhub.rest.models.RestResponse;
import uk.phsh.footyhub.rest.models.Score;
import uk.phsh.footyhub.rest.models.Team;

public class FixturesTask extends BaseTask<CompetitionFixtures> {

    private final LeagueEnum _league;
    private final int _matchWeek;
    /**
     * @param callback Generic callback to be used to receive responses
     */
    public FixturesTask(I_TaskCallback<CompetitionFixtures> callback, LeagueEnum league, int matchWeek) {
        super(callback);
        this._league = league;
        this._matchWeek = matchWeek;
    }

    @Override
    public String getUrl() {
        return baseUrl + "competitions/" + _league.getLeagueCode() + "/matches?" + ((_matchWeek <= 0) ? "" : "matchday=" + _matchWeek);
    }

    @Override
    public String getTag() {
        return "fixturesTask";
    }

    @Override
    public void onSuccess(RestResponse response) {
        JsonObject baseObject = getBaseObject(response.getResponseBody());
        JsonObject competitionObject = returnDefaultNullObj(baseObject.get("competition"));
        JsonArray matches = returnDefaultNullArray(baseObject.get("matches"));

        LeagueInfo leagueInfo = new LeagueInfo();
        leagueInfo.emblem = returnDefaultNullString(competitionObject.get("emblem"));
        leagueInfo.name = returnDefaultNullString( competitionObject.get("name"));

        CompetitionFixtures fixtures = new CompetitionFixtures(leagueInfo);

        for(JsonElement element : matches) {
            JsonObject matchObj = returnDefaultNullObj(element);

            Match match = new Match();

            int id = returnDefaultNullInt(matchObj.get("id"));
            String utcDate = returnDefaultNullString(matchObj.get("utcDate"));
            String status = returnDefaultNullString(matchObj.get("status"));

            int matchday = returnDefaultNullInt(matchObj.get("matchday"));

            match.matchID = id;
            match.matchDate = dateTimeString(utcDate, DateTimeType.DATE);
            match.matchTime = dateTimeString(utcDate, DateTimeType.TIME);
            match.epochTime = dateTimeString(utcDate, DateTimeType.EPOCH);

            JsonObject homeTeamObj = returnDefaultNullObj(matchObj.get("homeTeam"));
            Team homeTeam = new Team();
            homeTeam.tla = returnDefaultNullString(homeTeamObj.get("tla"));
            homeTeam.id = returnDefaultNullInt(homeTeamObj.get("id"));
            homeTeam.name = returnDefaultNullString(homeTeamObj.get("name"));
            homeTeam.shortName = returnDefaultNullString(homeTeamObj.get("shortName"));
            homeTeam.crest = returnDefaultNullString(homeTeamObj.get("crest"));
            match.homeTeam = homeTeam;

            JsonObject awayTeamObj = returnDefaultNullObj(matchObj.get("awayTeam"));
            Team awayTeam = new Team();
            awayTeam.tla = returnDefaultNullString(awayTeamObj.get("tla"));
            awayTeam.id = returnDefaultNullInt(awayTeamObj.get("id"));
            awayTeam.name = returnDefaultNullString(awayTeamObj.get("name"));
            awayTeam.shortName = returnDefaultNullString(awayTeamObj.get("shortName"));
            awayTeam.crest = returnDefaultNullString(awayTeamObj.get("crest"));
            match.awayTeam = awayTeam;

            if(status.equals("FINISHED")) {
                match.matchType = FixtureType.FINISHED;
                JsonObject scoreObj = returnDefaultNullObj(matchObj.get("score"));

                JsonObject fullTimeObj = returnDefaultNullObj(scoreObj.get("fullTime"));
                match.fullTime = new Score(returnDefaultNullInt(fullTimeObj.get("home")),
                        returnDefaultNullInt(fullTimeObj.get("away")));

                JsonObject halfTimeObj = returnDefaultNullObj(scoreObj.get("halfTime"));
                match.halfTime = new Score(returnDefaultNullInt(halfTimeObj.get("home")),
                        returnDefaultNullInt(halfTimeObj.get("away")));
            } else {
                match.matchType = FixtureType.SCHEDULED;
                match.fullTime = null;
                match.halfTime = null;
            }
            fixtures.addToMatchDays(matchday, match);
        }
        getCallback().onSuccess(fixtures);
    }
}
