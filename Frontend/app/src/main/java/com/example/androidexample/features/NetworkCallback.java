package com.example.androidexample.features;

public interface NetworkCallback<T> {
    void onSuccess(T response);
    void onError(String errorMessage);
}
