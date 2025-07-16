package uk.phsh.footyhub.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import java.util.ArrayList;
import uk.phsh.footyhub.rest.enums.FixtureType;
import uk.phsh.footyhub.rest.enums.LeagueEnum;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.LeagueStanding;
import uk.phsh.footyhub.rest.models.Match;
import uk.phsh.footyhub.rest.models.NewsArticle;
import uk.phsh.footyhub.rest.models.Team;
import uk.phsh.footyhub.rest.tasks.LeagueStandingsTask;
import uk.phsh.footyhub.rest.tasks.LeagueTeamsTask;
import uk.phsh.footyhub.rest.tasks.NewsSearchTask;
import uk.phsh.footyhub.rest.tasks.PrevNextMatchTask;

public class RestTaskInstrumentedTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    RestManager rpm = RestManager.getInstance(appContext.getCacheDir());
    @Test
    public void getStandings() {
        LeagueEnum league = LeagueEnum.PREMIER_LEAGUE;
        I_TaskCallback<LeagueStanding> callback = new I_TaskCallback<>() {
            @Override
            public void onSuccess(LeagueStanding value) {
                assertNotNull(value);
                assertEquals(league.getReadableName(), value.getLeagueInfo().name);
            }

            @Override
            public void onError(String message) {
                assertNotNull(message);
            }
        };
        rpm.asyncTask(new LeagueStandingsTask(league, callback));
    }

    @Test
    public void getTeamsByLeague() {
        LeagueEnum league = LeagueEnum.PREMIER_LEAGUE;
        I_TaskCallback<ArrayList<Team>> callback = new I_TaskCallback<>() {
            @Override
            public void onSuccess(ArrayList<Team> teams) {
                assertNotNull(teams);
                assertEquals(20, teams.size());
            }

            @Override
            public void onError(String message) {
                assertNotNull(message);
            }
        };
        rpm.asyncTask(new LeagueTeamsTask(league, callback));
    }

    @Test
    public void getFavouriteNextFixture() {
        int teamID = 392;
        I_TaskCallback<Match> callback = new I_TaskCallback<>() {
            @Override
            public void onSuccess(Match match) {
                assertNotNull(match);
            }

            @Override
            public void onError(String message) {
                assertNotNull(message);
            }
        };
        rpm.asyncTask(new PrevNextMatchTask(teamID, FixtureType.SCHEDULED, callback));
    }

    @Test
    public void getFavouritePrevResult() {
        int teamID = 392;
        I_TaskCallback<Match> callback = new I_TaskCallback<>() {
            @Override
            public void onSuccess(Match match) {
                assertNotNull(match);
            }

            @Override
            public void onError(String message) {
                assertNotNull(message);
            }
        };
        rpm.asyncTask(new PrevNextMatchTask(teamID, FixtureType.FINISHED, callback));
    }

    @Test
    public void getNewsForClub() {
        String teamName = "Liverpool FC";
        int maxResults = 10;
        I_TaskCallback<ArrayList<NewsArticle>> callback = new I_TaskCallback<>() {
            @Override
            public void onSuccess(ArrayList<NewsArticle> articles) {
                assertNotNull(articles);
                assertNotEquals(0, articles.size());
            }

            @Override
            public void onError(String message) {
                assertNotNull(message);
            }
        };
        rpm.asyncTask(new NewsSearchTask(teamName, callback, maxResults));
    }

}
