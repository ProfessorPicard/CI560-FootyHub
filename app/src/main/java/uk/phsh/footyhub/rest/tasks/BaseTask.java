package uk.phsh.footyhub.rest.tasks;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.util.concurrent.Callable;
import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import uk.phsh.footyhub.BuildConfig;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.rest.cache.CacheInterceptor;
import uk.phsh.footyhub.rest.interfaces.I_RestResponse;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.RestResponse;

/**
 * Abstract class for creating http request tasks
 * @author Peter Blackburn
 */
public abstract class BaseTask<T> implements Callable<RestResponse>, I_RestResponse {

    public static final String baseUrl = "https://api.football-data.org/v4/";
    private static OkHttpClient client;
    private final I_TaskCallback<T> _callback;
    private final Gson _gson;
    private final Context _context;

    /**
     * @return String The url of the http request
     */
    public abstract String getUrl();
    public abstract String getTag();

    /**
     * @param callback Generic callback to be used to receive responses
     */
    public BaseTask(I_TaskCallback<T> callback, Context context) {
        _callback = callback;
        _gson = new Gson();
        _context = context;
    }

    /**
     * Perform setup of the OkHTTP instance if it hasn't already been done
     * @param cacheDir Location of the android cacheDirectory
     */
    public void setupOkHTTP(File cacheDir) {
        if(client == null) {
            long cacheSize = (50 * 1024 * 1024);
            Cache cache = new Cache(cacheDir, cacheSize);
            client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new CacheInterceptor())
                    .cache(cache)
                    .build();
        }
    }

    /**
     * @return Gson Gson instance for this task
     */
    protected Gson getGson() { return _gson; }

    /**
     * @return I_TaskCallback<T> Callback to send responses to
     */
    protected I_TaskCallback<T> getCallback() { return _callback; }

    /**
     * @return Headers The okHttp Headers for the http request
     */
    public Headers getHeaders() {
        return new Headers.Builder()
                .add("X-Auth-Token", BuildConfig.API_KEY)
                .build();
    }

    /**
     * Called when executing the http request
     * @return RestResponse Containing the response code and the response body
     * @throws Exception Throws an exception when something is wrong
     */
    public RestResponse call() throws Exception {

        Request request = new Request.Builder()
                .url(getUrl())
                .headers(getHeaders())
                .build();

        String responseStr;
        int responseCode;
        try (okhttp3.Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            responseStr = response.body().string();
            responseCode = response.code();
        }

        return new RestResponse(responseStr, responseCode);
    }

    @Override
    public void onError(RestResponse response) {
        Log.e("RestParsingManager : " + getTag(), "Error Code: " + response.getResponseCode() + " | Message: " + response.getResponseBody());
        _callback.onError(response.getResponseBody());
    }

    @Override
    public void onRateLimitReached(int secondsRemaining) {
        _callback.onError(_context.getString(R.string.rateLimitReached, secondsRemaining));
    }

    protected JsonObject getBaseObject(String json) {
        JsonElement element = JsonParser.parseString(json);
        return element.getAsJsonObject();
    }

}
