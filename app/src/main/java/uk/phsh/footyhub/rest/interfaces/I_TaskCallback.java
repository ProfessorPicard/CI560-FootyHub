package uk.phsh.footyhub.rest.interfaces;

public interface I_TaskCallback<T> {

    void onSuccess(T value);
    void onError(String message);

}
