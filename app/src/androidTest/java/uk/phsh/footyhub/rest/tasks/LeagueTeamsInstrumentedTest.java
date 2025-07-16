package uk.phsh.footyhub.rest.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.ArrayList;
import okhttp3.Headers;
import uk.phsh.footyhub.rest.enums.LeagueEnum;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.RestResponse;
import uk.phsh.footyhub.rest.models.Team;

public class LeagueTeamsInstrumentedTest {
    LeagueTeamsTask ltt = new LeagueTeamsTask(new I_TaskCallback<>() {
        @Override
        public void onSuccess(ArrayList<Team> teams) { }

        @Override
        public void onError(String message) { }
    }, LeagueEnum.PREMIER_LEAGUE);

    @Test
    public void getUrl() {
        assertEquals("https://api.football-data.org/v4/competitions/PL/teams", ltt.getUrl());
    }

    @Test
    public void getHeaders() {
        Headers headers = ltt.getHeaders();
        assertNotNull(headers);
        assertTrue(headers.names().contains("X-Auth-Token"));
    }

    @Test
    public void call() {
        try {
            RestResponse response = ltt.call();
            assertNotNull(response);
            assertEquals(200, response.getResponseCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
