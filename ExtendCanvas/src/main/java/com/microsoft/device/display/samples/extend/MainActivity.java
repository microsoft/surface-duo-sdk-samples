/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.extend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String placeToGo = "";
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        // Injects the supplied Java object into WebView
        webView.addJavascriptInterface(MainActivity.this, "AndroidFunction");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/googlemapsearch.html");
    }

    private void startSearch() {
        placeToGo = searchView.getQuery().toString();
        setupWebView();
        hideKeyboard();
    }

    // This callback is for assets/googlemapsearch.html
    @SuppressWarnings("unused")
    @JavascriptInterface
    public String placeToGo() {
        return placeToGo;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchManager != null && searchManager.getSearchableInfo(getComponentName()) != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();
        searchView.onActionViewExpanded();
        searchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startSearch();
                    return true;
                }
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                startSearch();
                return false;
            }
        });
        this.searchView = searchView;
        setupWebView();
        return true;
    }
}
