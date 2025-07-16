package uk.phsh.footyhub.rest;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import uk.phsh.footyhub.rest.models.RestResponse;
import uk.phsh.footyhub.rest.tasks.BaseTask;

public class RestManager {

    private final Executor _executor = Executors.newFixedThreadPool(4);
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
     * @param task Any extended AbstractTask to be executed
     */
    public void asyncTask(BaseTask<?> task ) {
        task.setupOkHTTP(_cacheDir);
        _executor.execute(() -> {
            try {
                long currentTime = System.currentTimeMillis();
                if(currentTime > rateLimitTimestamp) {
                    final RestResponse result = task.call();

                    _executor.execute(() -> {
                        switch (result.getResponseCode()) {
                            case 200:
                                task.onSuccess(result);
                                break;
                            case 429:
                                String response = result.getResponseBody();
                                String[] temp = response.substring(response.indexOf("Wait")).split(" ");
                                int secondsLeft = Integer.parseInt(temp[1].trim());
                                rateLimitTimestamp = System.currentTimeMillis() + ((long) secondsLeft * 1000);
                                task.onRateLimitReached(secondsLeft);
                                break;
                            default:
                                task.onError(result);
                        }
                    });
                } else {
                    int secondsLeft = (int)(rateLimitTimestamp - currentTime) / 1000;
                    task.onRateLimitReached(secondsLeft);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


}
