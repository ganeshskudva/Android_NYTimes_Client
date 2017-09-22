package com.example.gkudva.android_nytimes_client.view.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.gkudva.android_nytimes_client.R;
import com.example.gkudva.android_nytimes_client.model.Doc;
import com.example.gkudva.android_nytimes_client.util.Util;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    public static final String URL_KEY = "Article";
    private ShareActionProvider miShareAction;
    private String mUrl;
    //private MenuItem miProgressBarItem;
    private Doc mArticle;

    @BindView(R.id.clWebView)
    ConstraintLayout mRlWebView;
    @BindView(R.id.webview)
    WebView mWebView;
    @BindView(R.id.miActionProgress)
    MenuItem miProgressBarItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        configureWebView(mWebView);
        mArticle = (Doc) Parcels.unwrap(getIntent().getParcelableExtra(URL_KEY));
        mUrl = mArticle.getWebUrl();
        invalidateOptionsMenu();
        final ProgressBar v = (ProgressBar)MenuItemCompat.getActionView(miProgressBarItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mWebView.getUrl());
        miShareAction.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Util.isNetworkAvailable(this) || !Util.isOnline()){
            Util.showSnackBar(mRlWebView, this);
        }else if(mUrl != null && !mUrl.isEmpty()) {
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    //    miProgressBarItem = menu.findItem(R.id.miActionProgress);
    //    final ProgressBar v = (ProgressBar)MenuItemCompat.getActionView(miProgressBarItem);
        return super.onPrepareOptionsMenu(menu);
    }

    private void configureWebView(WebView webView){
        // Configure related browser settings
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        webView.setWebViewClient(new MyBrowser());
    }

    private class MyBrowser extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideProgressBar();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showProgressBar();
            super.onPageStarted(view, url, favicon);
        }
    }


    private void showProgressBar() {
        miProgressBarItem.setVisible(true);
    }

    private void hideProgressBar() {
        miProgressBarItem.setVisible(false);
    }
}
