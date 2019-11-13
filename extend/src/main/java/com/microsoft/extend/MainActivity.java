package com.microsoft.extend;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements DualScreenDetectionListener {

    private static final String TAG = "MainActivity";
    private DualScreenHelper mDualScreenHelper = new DualScreenHelper();

    private WebView mWebView;
    private View mSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDualScreenHelper.addListener(this);
        mDualScreenHelper.initialize(this, getWindow().getDecorView().getRootView());

        mSearchBar = findViewById(R.id.search_bar);

        setupWebView();
    }

    private void setupWebView() {
        mWebView = findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.loadUrl("file:///android_asset/googlemap.html");
    }

    @Override
    public void useSingleScreenMode(int rotation) {
        Log.d(TAG, "useSingleScreenMode rotation: " + rotation);

        if (mSearchBar == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = mSearchBar.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mSearchBar.setLayoutParams(layoutParams);
    }

    @Override
    public void useDualScreenMode(int rotation, Rect screenRect1, Rect screenRect2) {
        Log.d(TAG, "useDualScreenMode rotation: " + rotation);
        if (rotation == 1 || rotation == 3) {
            useSingleScreenMode(rotation);
            return;
        }

        if (mSearchBar == null) {
            return;
        }

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)mSearchBar.getLayoutParams();
        layoutParams.width = screenRect1.width() - layoutParams.getMarginStart() - layoutParams.getMarginEnd();
        mSearchBar.setLayoutParams(layoutParams);
    }
}
