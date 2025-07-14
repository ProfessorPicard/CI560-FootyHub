package uk.phsh.footyhub.rest.interfaces;

import uk.phsh.footyhub.rest.models.RestResponse;

/**
 * RestResponse interface used to retrieve http requests from the request manager
 * @author Peter Blackburn
 */
public interface I_RestResponse {
    void onError(RestResponse response);
    void onSuccess(RestResponse response);
    void onRateLimitReached(int secondsRemaining);
}
