package com.example.gkudva.android_nytimes_client.presenter;

import android.util.Log;

import com.example.gkudva.android_nytimes_client.model.Doc;
import com.example.gkudva.android_nytimes_client.model.NYTResponse;
import com.example.gkudva.android_nytimes_client.model.rest.NYTService;
import com.example.gkudva.android_nytimes_client.util.FilterOptions;
import com.example.gkudva.android_nytimes_client.util.Util;
import com.example.gkudva.android_nytimes_client.view.MainMvpView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;

/**
 * Created by gkudva on 18/09/17.
 */

public class MainPresenter implements Presenter<MainMvpView>{

    private MainMvpView mainMvpView;
    private Subscription subscription;
    private NYTService mNYTService;
    private List<Doc> mDocList;
    private static final String TAG = "MainPresenter";
    private static int pageNum = 0;

    @Override
    public void attachView(MainMvpView view) {
        this.mainMvpView = view;

    }

    @Override
    public void detachView() {
        pageNum = 0;
        this.mainMvpView = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadArticles(String query, FilterOptions filterOptions)
    {
        String beginDate = filterOptions.getDateWithoutSeparator();
        String sortOrder = filterOptions.getSortOrder() != null ?
                filterOptions.getSortOrder().name().toLowerCase() : null;
        final String newDeskValues = filterOptions.getNewDeskValues();
        String fq = null;
        if (newDeskValues != null )
                fq = String.format("news_desk:(%s)", newDeskValues);
        pageNum++;

        if(Util.isNetworkAvailable(mainMvpView.getContext()) && Util.isOnline()) {
            mainMvpView.showProgressIndicator();
            if (subscription != null) subscription.unsubscribe();

            if (mNYTService == null)
                mNYTService = NYTService.Factory.create();

/*
            subscription = mNYTService.getResponse(query, pageNum, beginDate, sortOrder, fq)
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
                            Log.e(TAG, "Error loading NYT Articles ", error);
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
    */
         Log.d("Ganesh_URL", mNYTService.getResponse(query, pageNum, beginDate, sortOrder, fq).request().url().toString());
            mNYTService.getResponse(query, pageNum, beginDate, sortOrder, fq).enqueue(new Callback<NYTResponse>() {
                @Override
                public void onResponse(Call<NYTResponse> call, Response<NYTResponse> response) {
                    if (response.isSuccessful())
                    {
                        mainMvpView.showNews(response.body().getResponse().getDocs(), newDeskValues);
                    }
                    else
                    {
                        Log.d("Ganesh statusCode", Integer.toString(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<NYTResponse> call, Throwable t) {
                    Log.d("Ganesh", "Network call failed");
                }
            });
        }
    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }

}
