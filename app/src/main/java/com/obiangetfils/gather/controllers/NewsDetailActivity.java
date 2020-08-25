package com.obiangetfils.gather.controllers;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.obiangetfils.gather.R;

public class NewsDetailActivity extends AppCompatActivity {

    private String url = "";
    private ActionBar actionBar;
    private FrameLayout frameLayout;
    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webView);

        url = getIntent().getStringExtra("url");

        webView.setWebViewClient(new HelpClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);

                // While page loading
                actionBar.setTitle("Loading");
                if (newProgress == 100) {
                    frameLayout.setVisibility(View.GONE);
                    actionBar.setTitle(view.getTitle());
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.loadUrl(url); // web page is loading
        progressBar.setProgress(0);


    }

    private class HelpClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String pUrl) {
            view.loadUrl(pUrl);
            frameLayout.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Toast.makeText(NewsDetailActivity.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // check if the key event was the back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.canGoBack();
            return true;
        }

        // If it wasn't the back key or no web page history bubble up to the default system behaviour ex: previous activity
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            webView.reload();
        } else if (id == R.id.action_copy) {
            copyUrl();
        } else if (id == R.id.action_share) {
            shareUrl();
        }
        return super.onOptionsItemSelected(item);
    }

    private void copyUrl() {
        String urlToCopy = webView.getUrl();
        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(urlToCopy);

        Toast.makeText(this, "URL copied ...", Toast.LENGTH_SHORT).show();
    }

    private void shareUrl() {
        String urlToShare = webView.getUrl();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, urlToShare);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

}