package com.example.gkudva.android_nytimes_client.model.rest;

import com.example.gkudva.android_nytimes_client.model.NYTResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gkudva on 18/09/17.
 */

public interface NYTService {

    public static final String BASE_URL = "https://api.nytimes.com/svc/search/v2/";
/*
    @GET("articlesearch.json?api-key=ea46845132f340a8811a88ab9ec0f22a&fl=web_url,multimedia,headline")
    Observable<NYTResponse> getResponse(@Query("q") String query,
                                        @Query("page") int page,
                                        @Query("begin_date") String beginDate,
                                        @Query("sort") String sortOrder,
                                        @Query("fq") String fq);
*/
    @GET("articlesearch.json?api-key=ea46845132f340a8811a88ab9ec0f22a&fl=web_url,multimedia,headline")
    Call<NYTResponse> getResponse(@Query("q") String query,
                                  @Query("page") int page,
                                  @Query("begin_date") String beginDate,
                                  @Query("sort") String sortOrder,
                                  @Query("fq") String fq);



    class Factory {
        public static NYTService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            return retrofit.create(NYTService.class);
        }
    }
}
