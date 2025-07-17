package uk.phsh.footyhub.rest.cache;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * okHttp CacheInterceptor used to control the cache duration for http requests
 * @author Peter Blackburn
 */
public class CacheInterceptor implements Interceptor {

    /**
     * @param chain okHttp request Chain
     * @return Response The modified okHttp Response
     * @throws IOException IoException thrown on error
     */
    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(30, TimeUnit.MINUTES)
                .build();


        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl.toString())
                .build();
    }
}
