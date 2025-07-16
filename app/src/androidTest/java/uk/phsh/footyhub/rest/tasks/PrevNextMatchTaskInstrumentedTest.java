package uk.phsh.footyhub.rest.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import okhttp3.Headers;
import uk.phsh.footyhub.rest.enums.FixtureType;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.Match;
import uk.phsh.footyhub.rest.models.RestResponse;

public class PrevNextMatchTaskInstrumentedTest {
    PrevNextMatchTask pnmt = new PrevNextMatchTask(397, FixtureType.FINISHED, new I_TaskCallback<>() {
        @Override
        public void onSuccess(Match match) { }

        @Override
        public void onError(String message) { }
    });

    @Test
    public void getUrl() {
        assertEquals("https://api.football-data.org/v4/teams/397/matches?status=FINISHED&limit=1", pnmt.getUrl());
    }

    @Test
    public void getHeaders() {
        Headers headers = pnmt.getHeaders();
        assertNotNull(headers);
        assertTrue(headers.names().contains("X-Auth-Token"));
    }

    @Test
    public void call() {
        try {
            RestResponse response = pnmt.call();
            assertNotNull(response);
            assertEquals(200, response.getResponseCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
