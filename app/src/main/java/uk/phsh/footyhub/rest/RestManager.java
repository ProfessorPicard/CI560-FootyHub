package uk.phsh.footyhub.rest;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import uk.phsh.footyhub.rest.models.RestResponse;
import uk.phsh.footyhub.rest.tasks.BaseTask;

public class RestManager {

    private final ExecutorService _executor = Executors.newFixedThreadPool(4);
    private long rateLimitTimestamp = 0;
    private final File _cacheDir;
    private static RestManager _instance;

    private RestManager(File cacheDir) {
        this._cacheDir = cacheDir;
    }

    public static RestManager getInstance(File cacheDir) {
        if(_instance == null)
            _instance = new RestManager(cacheDir);

        return _instance;
    }

    /**
     * Sends Http Requests on a separate thread and forwards the response
     * @param task Any extended BaseTask<?> to be executed
     */
    public void submitTask(BaseTask<?> task ) {
        task.setupOkHTTP(_cacheDir);
        long currentTime = System.currentTimeMillis();
        if (currentTime > rateLimitTimestamp) {

            CompletableFuture<RestResponse> _future = CompletableFuture.supplyAsync(task, _executor);
            _future.thenApply((response) -> {
                switch (response.getResponseCode()) {
                    case 200:
                        task.onSuccess(response);
                        break;
                    case 429:
                        String responseBody = response.getResponseBody();
                        String[] temp = responseBody.substring(responseBody.indexOf("Wait")).split(" ");
                        int secondsLeft = Integer.parseInt(temp[1].trim());
                        rateLimitTimestamp = System.currentTimeMillis() + ((long) secondsLeft * 1000);
                        task.onRateLimitReached(secondsLeft);
                        break;
                    default:
                        task.onError(response);
                }
                return response;
            });

        } else {
            int secondsLeft = (int) (rateLimitTimestamp - currentTime) / 1000;
            task.onRateLimitReached(secondsLeft);
        }
    }


}
