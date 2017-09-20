package com.example.gkudva.android_nytimes_client.presenter;

import android.util.Log;

import com.example.gkudva.android_nytimes_client.model.Doc;
import com.example.gkudva.android_nytimes_client.model.NYTResponse;
import com.example.gkudva.android_nytimes_client.model.NYTService;
import com.example.gkudva.android_nytimes_client.view.MainMvpView;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gkudva on 18/09/17.
 */

public class MainPresenter implements Presenter<MainMvpView>{

    private MainMvpView mainMvpView;
    private Subscription subscription;
    private NYTService mNYTService;
    private List<Doc> mDocList;
    private static final String TAG = "MainPresenter";

    @Override
    public void attachView(MainMvpView view) {
        this.mainMvpView = view;
    }

    @Override
    public void detachView() {
        this.mainMvpView = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadFlicks(String beginDate, String sortType)
    {
        mainMvpView.showProgressIndicator();
        if (subscription != null) subscription.unsubscribe();

        if (mNYTService == null)
            mNYTService = NYTService.Factory.create();

        subscription = mNYTService.getResponse(beginDate, sortType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<NYTResponse>() {
                    @Override
                    public void onCompleted() {
                        if (!mDocList.isEmpty()) {
                            mainMvpView.showNews(mDocList);
                        } else {
                            mainMvpView.showMessage("No Flicks found");
                        }


                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error loading flicks ", error);
                        if (isHttp404(error)) {
                            mainMvpView.showMessage("HTTP Error");
                        } else {
                            mainMvpView.showMessage(error.getMessage());
                        }
                    }

                    @Override
                    public void onNext(NYTResponse response) {
                        if (mDocList == null)
                            MainPresenter.this.mDocList = response.getResponse().getDocs();
                        else
                            MainPresenter.this.mDocList.addAll(response.getResponse().getDocs());
                    }
                });
    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }

}
