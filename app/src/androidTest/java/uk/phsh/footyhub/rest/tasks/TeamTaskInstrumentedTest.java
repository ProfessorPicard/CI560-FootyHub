package uk.phsh.footyhub.rest.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import okhttp3.Headers;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.RestResponse;
import uk.phsh.footyhub.rest.models.Team;

public class TeamTaskInstrumentedTest {
    TeamTask tt = new TeamTask(64, new I_TaskCallback<>() {
        @Override
        public void onSuccess(Team team) { }

        @Override
        public void onError(String message) { }
    });

    @Test
    public void getUrl() {
        assertEquals("https://api.football-data.org/v4/teams/64", tt.getUrl());
    }

    @Test
    public void getHeaders() {
        Headers headers = tt.getHeaders();
        assertNotNull(headers);
        assertTrue(headers.names().contains("X-Auth-Token"));
    }

    @Test
    public void call() {
        try {
            RestResponse response = tt.call();
            assertNotNull(response);
            assertEquals(200, response.getResponseCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
