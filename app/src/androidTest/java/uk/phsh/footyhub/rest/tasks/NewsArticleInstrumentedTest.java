package uk.phsh.footyhub.rest.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.ArrayList;
import okhttp3.Headers;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.NewsArticle;
import uk.phsh.footyhub.rest.models.RestResponse;

public class NewsArticleInstrumentedTest {
    NewsSearchTask nst = new NewsSearchTask("Liverpool FC", new I_TaskCallback<>() {
        @Override
        public void onSuccess(ArrayList<NewsArticle> articles) { }

        @Override
        public void onError(String message) { }
    }, 10);

    @Test
    public void getUrl() {
        assertEquals("https://www.newsnow.co.uk/h/?search=Liverpool FC&lang=en&searchheadlines=1", nst.getUrl());
    }

    @Test
    public void getHeaders() {
        Headers headers = nst.getHeaders();
        assertNotNull(headers);
        assertTrue(headers.names().contains("X-Auth-Token"));
    }

    @Test
    public void call() {
        try {
            RestResponse response = nst.call();
            assertNotNull(response);
            assertEquals(200, response.getResponseCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
