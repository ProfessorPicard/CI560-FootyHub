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
        JsonObject competitionObject = baseObject.getAsJsonObject("competition");
        JsonArray matches = baseObject.getAsJsonArray("matches");

        LeagueInfo leagueInfo = new LeagueInfo();
        leagueInfo.emblem = competitionObject.get("emblem").getAsString();
        leagueInfo.name = competitionObject.get("name").getAsString();

        CompetitionFixtures fixtures = new CompetitionFixtures(leagueInfo);

        for(JsonElement element : matches) {
            JsonObject matchObj = element.getAsJsonObject();

            Match match = new Match();

            int id = matchObj.get("id").getAsInt();
            String utcDate = matchObj.get("utcDate").getAsString();
            String status = matchObj.get("status").getAsString();

            int matchday = matchObj.get("matchday").getAsInt();

            match.matchID = id;
            match.matchDate = dateTimeString(utcDate, DateTimeType.DATE);
            match.matchTime = dateTimeString(utcDate, DateTimeType.TIME);
            match.epochTime = dateTimeString(utcDate, DateTimeType.EPOCH);

            JsonObject homeTeamObj = matchObj.getAsJsonObject("homeTeam");
            Team homeTeam = new Team();
            homeTeam.tla = homeTeamObj.get("tla").getAsString();
            homeTeam.id = homeTeamObj.get("id").getAsInt();
            homeTeam.name = homeTeamObj.get("name").getAsString();
            homeTeam.shortName = homeTeamObj.get("shortName").getAsString();
            homeTeam.crest = homeTeamObj.get("crest").getAsString();
            match.homeTeam = homeTeam;

            JsonObject awayTeamObj = matchObj.getAsJsonObject("awayTeam");
            Team awayTeam = new Team();
            awayTeam.tla = awayTeamObj.get("tla").getAsString();
            awayTeam.id = awayTeamObj.get("id").getAsInt();
            awayTeam.name = awayTeamObj.get("name").getAsString();
            awayTeam.shortName = awayTeamObj.get("shortName").getAsString();
            awayTeam.crest = awayTeamObj.get("crest").getAsString();
            match.awayTeam = awayTeam;

            if(status.equals("FINISHED")) {
                match.matchType = FixtureType.FINISHED;
                JsonObject scoreObj = matchObj.getAsJsonObject("score");
                JsonObject fullTimeObj = scoreObj.getAsJsonObject("fullTime");
                int fullHomeScore = -1;
                int fullAwayScore = -1;
                if(!fullTimeObj.get("home").isJsonNull())
                    fullHomeScore = fullTimeObj.get("home").getAsInt();

                if(!fullTimeObj.get("away").isJsonNull())
                    fullAwayScore = fullTimeObj.get("away").getAsInt();

                match.fullTime = new Score(fullHomeScore, fullAwayScore);
                JsonObject halfTimeObj = scoreObj.getAsJsonObject("halfTime");
                int halfHomeScore = -1;
                int halfAwayScore = -1;
                if(!halfTimeObj.get("home").isJsonNull())
                    halfHomeScore = halfTimeObj.get("home").getAsInt();

                if(!halfTimeObj.get("away").isJsonNull())
                    halfAwayScore = halfTimeObj.get("away").getAsInt();

                match.halfTime = new Score(halfHomeScore, halfAwayScore);
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
