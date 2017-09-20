package com.example.gkudva.android_nytimes_client.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.gkudva.android_nytimes_client.R;
import com.example.gkudva.android_nytimes_client.model.Doc;
import com.example.gkudva.android_nytimes_client.presenter.MainPresenter;
import com.example.gkudva.android_nytimes_client.util.EndlessRecyclerViewScrollListener;
import com.example.gkudva.android_nytimes_client.util.InfoMessage;
import com.example.gkudva.android_nytimes_client.view.MainMvpView;
import com.facebook.stetho.Stetho;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class MainActivity extends AppCompatActivity implements MainMvpView,SwipeRefreshLayout.OnRefreshListener{

    private MainPresenter presenter;
    private LinearLayoutManager layoutManager;
    private InfoMessage infoMessage;
    public Parcelable listState;
    private ACProgressFlower mLoadingDialog;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private static final String TAG_LOG = "MainActivity";

    @BindView(R.id.rvNews)
    RecyclerView flicksRecycleView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);

        presenter = new MainPresenter();
        presenter.attachView(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        infoMessage = new InfoMessage(this);
        setupRecyclerView(flicksRecycleView);

        swipeRefreshLayout.setOnRefreshListener(this);
        presenter.loadFlicks();
    }

    public void showLoadingDialog()
    {
        if (mLoadingDialog == null )
        {
            mLoadingDialog = new ACProgressFlower.Builder(this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.RED)
                    .text(getResources().getString(R.string.loading))
                    .fadeColor(Color.DKGRAY).build();
        }

        if (!mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    public void hideLoadingDialog()
    {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onRefresh() {
        presenter.loadFlicks();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNews(List<Doc> docList) {
        FlickAdapter adapter = (FlickAdapter) flicksRecycleView.getAdapter();
        adapter.setFlicks(flickList);
        adapter.notifyDataSetChanged();
        flicksRecycleView.requestFocus();
        flicksRecycleView.setVisibility(View.VISIBLE);
        hideLoadingDialog();
    }
}
