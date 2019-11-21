package com.microsoft.device.display.samples.extend;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.device.display.samples.utils.DualScreenDetectionListener;
import com.microsoft.device.display.samples.utils.DualScreenHelper;

public class MainActivity extends AppCompatActivity implements DualScreenDetectionListener {

    private static final String TAG = "MainActivity";
    private static final String KEY_PLACE = "place";
    private DualScreenHelper mDualScreenHelper = new DualScreenHelper();

    private WebView mWebView;
    private EditText mSearchBar;
    private String placeToGo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null) {
            placeToGo = savedInstanceState.getString(KEY_PLACE, "");
        }

        if(mDualScreenHelper.initialize(this, getWindow().getDecorView().getRootView())) {
            mDualScreenHelper.addListener(this);
        }

        mSearchBar = findViewById(R.id.search_bar);
        mSearchBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(mSearchBar.getCompoundDrawables()[2] != null){
                        if(event.getX() >= (mSearchBar.getRight() - mSearchBar.getLeft() - mSearchBar.getCompoundDrawables()[2].getBounds().width())) {
                            placeToGo = mSearchBar.getText().toString();
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
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(KEY_PLACE, placeToGo);
        super.onSaveInstanceState(outState);
    }

    private void setupWebView() {
        mWebView = findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(MainActivity.this, "AndroidFunction");
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("file:///android_asset/googlemapsearch.html");
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
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
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
}
