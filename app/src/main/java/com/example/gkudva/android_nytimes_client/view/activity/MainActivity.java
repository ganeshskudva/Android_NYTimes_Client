package com.example.gkudva.android_nytimes_client.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.gkudva.android_nytimes_client.R;
import com.example.gkudva.android_nytimes_client.model.Doc;
import com.example.gkudva.android_nytimes_client.presenter.MainPresenter;
import com.example.gkudva.android_nytimes_client.util.EndlessRecyclerViewScrollListener;
import com.example.gkudva.android_nytimes_client.util.FilterOptions;
import com.example.gkudva.android_nytimes_client.util.InfoMessage;
import com.example.gkudva.android_nytimes_client.util.SpacesItemDecoration;
import com.example.gkudva.android_nytimes_client.view.MainMvpView;
import com.example.gkudva.android_nytimes_client.view.adapter.NYTAdapter;
import com.example.gkudva.android_nytimes_client.view.fragment.SearchFiltersFragment;
import com.facebook.stetho.Stetho;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class MainActivity extends AppCompatActivity implements MainMvpView,SwipeRefreshLayout.OnRefreshListener,SearchFiltersFragment.FilterOptionsUpdateListener{

    private MainPresenter presenter;
    private StaggeredGridLayoutManager layoutManager;
    private InfoMessage infoMessage;
    public Parcelable listState;
    private ACProgressFlower mLoadingDialog;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private static final String TAG_LOG = "MainActivity";
    private EditText mEtSearchText;
    private FilterOptions mFilterOptions;
    private String mQuery;

    @BindView(R.id.rvNews)
    RecyclerView mArticleRecycleView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);

        presenter = new MainPresenter();
        presenter.attachView(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        infoMessage = new InfoMessage(this);
        mFilterOptions = new FilterOptions();
        setupRecyclerView(mArticleRecycleView);

    //    swipeRefreshLayout.setOnRefreshListener(this);
        //presenter.loadArticles(mQuery, mFilterOptions);
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

        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();

        }
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
        presenter.loadArticles(mQuery, mFilterOptions);
        //swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNews(List<Doc> articleList, String newDesk) {
        NYTAdapter adapter = (NYTAdapter) mArticleRecycleView.getAdapter();
        adapter.setArticles(articleList, newDesk);
        adapter.notifyDataSetChanged();
        mArticleRecycleView.requestFocus();
        mArticleRecycleView.setVisibility(View.VISIBLE);
        hideLoadingDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_articles_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        mEtSearchText = (EditText)searchView.findViewById(searchEditId);
        mEtSearchText.setTextColor(Color.WHITE);
        mEtSearchText.setHintTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                presenter.loadArticles(mQuery, mFilterOptions);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void onFilterOptionsChanged(FilterOptions filterOptions) {
        mFilterOptions = filterOptions;
        presenter.loadArticles(mQuery, mFilterOptions);
    }

    @Override
    public void showMessage(String message) {
        hideLoadingDialog();
        mArticleRecycleView.setVisibility(View.INVISIBLE);
        infoMessage.showMessage(message);
    }

    @Override
    public void showProgressIndicator() {
        showLoadingDialog();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        NYTAdapter adapter = new NYTAdapter(this);
        adapter.setCallback(new NYTAdapter.CallbackListener() {
            @Override
            public void onItemClick(Doc article) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("Article", Parcels.wrap(article));
                startActivity(intent);
            }

        });
        recyclerView.setAdapter(adapter);
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
//                presenter.loadArticles(mQuery, mFilterOptions);
            }
        };

        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if(itemId == R.id.action_filter) {
            FragmentManager fm = getSupportFragmentManager();
            SearchFiltersFragment searchFiltersFragment = SearchFiltersFragment.newInstance("Search Filter Options");
            Bundle args = new Bundle();
            args.putParcelable(SearchFiltersFragment.FILTER_OPTIONS_KEY, mFilterOptions);
            searchFiltersFragment.setArguments(args);
            searchFiltersFragment.show(fm, "search_filters_fragment");

        }
        return super.onOptionsItemSelected(item);
    }

}
