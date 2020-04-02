/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.orientationandspanning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Insets;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.WindowInsets;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.microsoft.device.dualscreen.layout.ScreenHelper;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView mainText, secondaryText,insetsText,metricsText,configuredText;
    Button showKeyboard,showSettings;
    static final long DEFAULT_DURATION=1000;
    HashMap<Integer, Pair<PointF,Float>> currentPos = new HashMap<>();
    ScreenConfig activeScreenConfig=ScreenConfig.UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View rootView = getWindow().getDecorView().getRootView();

        mainText = findViewById(R.id.mainText);
        secondaryText = findViewById(R.id.secondaryText);
        insetsText = findViewById(R.id.insetsText);
        showKeyboard = findViewById(R.id.showKeyBoard);
        metricsText = findViewById(R.id.metricsText);
        configuredText = findViewById(R.id.configuredText);
        showSettings = findViewById(R.id.openSettings);

        showKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the keyboard
                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        showSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the settings activity
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            }
        });

        // Initialize the layout
        redoConfig(getResources().getConfiguration());

        // Listen to layout changes, this fires when a user moves the activity between 2 screens
        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                redoConfig(getResources().getConfiguration());
            }
        });

        // Listen to Insets changes, this will fire when the soft keyboard pops (also other insets
        rootView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
           @Override
           public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
               if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                   Insets stableInsets;
                   stableInsets = insets.getStableInsets();
                   Insets systemWindowInsets = insets.getSystemWindowInsets();
                   insetsText.setText(String.format(Locale.getDefault(),getString(R.string.insets), stableInsets.top, systemWindowInsets.bottom));
               }
               return insets;
           }
        });

    }

    @Override
    public void onConfigurationChanged (Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        redoConfig(newConfig);
    }

    enum ScreenConfig {
        LEFT_PORTRAIT,
        RIGHT_PORTRAIT,
        TOP_LANDSCAPE,
        BOTTOM_LANDSCAPE,
        SPANNED_PORTRAIT,
        SPANNED_LANDSCAPE,
        UNKNOWN
    }

    private ScreenConfig getScreenConfig(Configuration newConfig){
        ScreenConfig currentScreenConfig = ScreenConfig.UNKNOWN;

        boolean isSpanned = ScreenHelper.isDualMode(MainActivity.this);

        int[] screenPosition = new int[2];
        MainActivity.this.getWindow().getDecorView().getLocationOnScreen(screenPosition);

        if (isSpanned && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ){
            currentScreenConfig = ScreenConfig.SPANNED_LANDSCAPE;
        }
        else if (isSpanned && newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ) {
            currentScreenConfig = ScreenConfig.SPANNED_PORTRAIT;
        }
        else if (!isSpanned && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ){
            if (screenPosition[1]>0 ){
                currentScreenConfig = ScreenConfig.BOTTOM_LANDSCAPE;
            }
            else {
                currentScreenConfig = ScreenConfig.TOP_LANDSCAPE;
            }
        }
        else if (!isSpanned && newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ) {
            if (screenPosition[0]>0 ){
                currentScreenConfig = ScreenConfig.RIGHT_PORTRAIT;
            }
            else {
                currentScreenConfig = ScreenConfig.LEFT_PORTRAIT;
            }
        }
        return currentScreenConfig;
    }

    private void redoConfig(Configuration newConfig){
        DisplayMetrics metrics = new DisplayMetrics();
        Display disp = MainActivity.this.getWindow().getDecorView().getDisplay();
        if (disp != null)
        {
            disp.getRealMetrics(metrics);
            metricsText.setText(String.format(Locale.getDefault(),getString(R.string.real_metrics), metrics.widthPixels, metrics.heightPixels));
        }

        String screenPositionName="";

        float screenWidth = convertDpToPx(newConfig.screenWidthDp);
        float screenHeight =  convertDpToPx(newConfig.screenHeightDp);
        configuredText.setText(String.format(Locale.getDefault(),getString(R.string.configured_size), screenWidth, screenHeight));
        ScreenConfig config =getScreenConfig(newConfig);

        if( config == activeScreenConfig)
        {
            return;
        }

        activeScreenConfig = config;
        switch (activeScreenConfig){

            case LEFT_PORTRAIT:
                animateView(secondaryText,0.0f,0.0f,DEFAULT_DURATION,0.0f);
                animateView(mainText,0.0f,0.0f,DEFAULT_DURATION,1.0f);
                screenPositionName=getString(R.string.left_portrait);
                break;
            case RIGHT_PORTRAIT:
                animateView(secondaryText,0.0f,0.0f,DEFAULT_DURATION,0.0f);
                animateView(mainText,0.0f,0.0f,DEFAULT_DURATION,1.0f);
                screenPositionName=getString(R.string.right_portrait);
                break;
            case TOP_LANDSCAPE:
                animateView(secondaryText,screenWidth/4.0f ,screenHeight/4.0f,DEFAULT_DURATION,1.0f);
                animateView(mainText,-1.0f*screenWidth/4.0f,-1.0f*screenHeight/4.0f,DEFAULT_DURATION,1.0f);
                screenPositionName=getString(R.string.top_landscape);
                break;
            case BOTTOM_LANDSCAPE:
                animateView(secondaryText,screenWidth/4.0f ,screenHeight/4.0f,DEFAULT_DURATION,1.0f);
                animateView(mainText,-1.0f*screenWidth/4.0f,-1.0f*screenHeight/4.0f,DEFAULT_DURATION,1.0f);
                screenPositionName=getString(R.string.bottom_landscape);
                break;
            case SPANNED_PORTRAIT:
                animateView(secondaryText,0.0f ,screenHeight/4.0f,DEFAULT_DURATION,1.0f);
                animateView(mainText,0.0f,-1.0f*screenHeight/4.0f,DEFAULT_DURATION,1.0f);
                screenPositionName=getString(R.string.spanned_portrait);
                break;
            case SPANNED_LANDSCAPE:
                animateView(secondaryText,screenWidth/4.0f ,0.0f,DEFAULT_DURATION,1.0f);
                animateView(mainText,-1.0f*screenWidth/4.0f,0.0f,DEFAULT_DURATION,1.0f);
                screenPositionName=getString(R.string.spanned_landscape);
                break;
            default:
                break;
        }
        mainText.setText(screenPositionName);
    }


    public void animateView(View view, float newX,float newY,long duration,float alpha){
        Pair<PointF, Float> from = currentPos.getOrDefault(view.hashCode(), new Pair<>(new PointF(0.0f, 0.0f), 1.0f));

        currentPos.put(view.hashCode(), new Pair<>(new PointF(newX, newY), alpha));

        AnimationSet aSet = new AnimationSet(true);

        assert from != null;
        Animation translateAnim = new TranslateAnimation(from.first.x, newX,from.first.y, newY);
        // Set the animation's parameters
        translateAnim.setDuration(duration);               // duration in ms
        translateAnim.setRepeatCount(0);                // -1 = infinite repeated
        translateAnim.setRepeatMode(Animation.ABSOLUTE); // reverses each repeat
        translateAnim.setFillAfter(true);               // keep rotation after animation

        Animation alphaAnim = new AlphaAnimation(from.second,alpha );
        alphaAnim.setDuration(duration);               // duration in ms
        alphaAnim.setRepeatCount(0);                // -1 = infinite repeated
        alphaAnim.setRepeatMode(Animation.ABSOLUTE); // reverses each repeat
        alphaAnim.setFillAfter(true);               // keep rotation after animation

        aSet.addAnimation(translateAnim);
        aSet.addAnimation(alphaAnim);
        aSet.setDuration(duration);               // duration in ms
        aSet.setRepeatCount(0);                // -1 = infinite repeated
        aSet.setRepeatMode(Animation.ABSOLUTE); // reverses each repeat
        aSet.setFillAfter(true);               // keep rotation after animation

        view.setAnimation(aSet);
    }
    public float convertDpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
}
