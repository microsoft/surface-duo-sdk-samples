/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.extend;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.device.display.samples.utils.ScreenHelper;

public class MainActivity extends AppCompatActivity {
    private ScreenHelper screenHelper;
    private WebView webView;
    private EditText searchBar;
    private String placeToGo = "";
    private boolean isDuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenHelper = new ScreenHelper();
        isDuo = screenHelper.initialize(this);
        useSingleMode(ScreenHelper.getRotation(this));
        setupWebView();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            startSearch();
        }
        return super.dispatchKeyEvent(event);
    }

    private void setupWebView() {
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        // Injects the supplied Java object into WebView
        webView.addJavascriptInterface(MainActivity.this, "AndroidFunction");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/googlemapsearch.html");
    }

    private void startSearch() {
        placeToGo = searchBar.getText().toString();
        setupWebView();
        hideKeyboard();
    }

    private void useSingleMode(int orientation) {
        if (searchBar == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = searchBar.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        searchBar.setLayoutParams(layoutParams);
    }

    private void useDualMode(int orientation) {
        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            useSingleMode(orientation);
            return;
        }

        if (searchBar == null) {
            return;
        }
        // Don't let search bar across the hinge
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) searchBar.getLayoutParams();
        Rect leftPane = new Rect();
        Rect rightPane = new Rect();
        screenHelper.getScreenRects(leftPane, rightPane, orientation);
        layoutParams.width = leftPane.width() - layoutParams.getMarginStart() - layoutParams.getMarginEnd();
        searchBar.setLayoutParams(layoutParams);
    }

    private void setupLayout() {
        int rotation = ScreenHelper.getRotation(this);
        if(isDuo) {
            if (screenHelper.isDualMode()) {
                useDualMode(rotation);
            } else {
                useSingleMode(rotation);
            }
        } else {
            useSingleMode(rotation);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupLayout();
    }

    // This callback is for assets/googlemapsearch.html
    @JavascriptInterface
    public String placeToGo() {
        return placeToGo;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();
        searchView.onActionViewExpanded();
        searchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // If Search icon is not null
                    if (searchBar.getCompoundDrawables()[2] != null) {
                        // Simulating click event for search icon
                        if (event.getX() >= (searchBar.getRight() - searchBar.getLeft() - searchBar.getCompoundDrawables()[2].getBounds().width())) {
                            placeToGo = searchBar.getText().toString();
                            setupWebView();
                            hideKeyboard();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        setupWebView();
        return true;
    }
}
