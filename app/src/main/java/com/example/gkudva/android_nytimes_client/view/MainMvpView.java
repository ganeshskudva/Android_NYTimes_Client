package com.example.gkudva.android_nytimes_client.view;

import com.example.gkudva.android_nytimes_client.model.Doc;

import java.util.List;

/**
 * Created by gkudva on 18/09/17.
 */

public interface MainMvpView extends MvpView {

    void showNews(List<Doc> docList);

    void showMessage(String message);

    void showProgressIndicator();
}