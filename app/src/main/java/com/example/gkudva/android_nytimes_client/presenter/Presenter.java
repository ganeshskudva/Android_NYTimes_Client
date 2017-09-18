package com.example.gkudva.android_nytimes_client.presenter;

/**
 * Created by gkudva on 18/09/17.
 */

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
