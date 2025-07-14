package uk.phsh.footyhub.rest.interfaces;

public interface I_TaskCallback<T> {

    void onSuccess(T value);
    void onRateLimitReached(int secondsRemaining);
    void onError(String message);

}
