/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.draganddrop;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.microsoft.device.display.samples.draganddrop.fragment.AdaptiveFragment;
import com.microsoft.device.display.samples.utils.ScreenHelper;

public class MainActivity extends AppCompatActivity {

    private ScreenHelper screenHelper;
    private boolean isDuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenHelper = new ScreenHelper();
        isDuo = screenHelper.initialize(this);

        setupLayout();

        final Activity self = this;
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                self.recreate();
            }
        });
    }

    private void setupLayout() {
        Bundle bundle = new Bundle();
        int rotation = ScreenHelper.getRotation(this);

        if (isDuo && screenHelper.isDualMode()) {
            switch (rotation) {
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    // Setting layout for double landscape
                    bundle.putInt(AdaptiveFragment.KEY_LAYOUT_ID, R.layout.fragment_dual_landscape);
                    break;
                default:
                    bundle.putInt(AdaptiveFragment.KEY_LAYOUT_ID, R.layout.fragment_dual_portrail);
                    break;
            }
        } else {
            bundle.putInt(AdaptiveFragment.KEY_LAYOUT_ID, R.layout.fragment_single_portrait);
        }

        Fragment adaptiveFragment = AdaptiveFragment.newInstance();
        adaptiveFragment.setArguments(bundle);
        showFragment(adaptiveFragment);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupLayout();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main, fragment)
                .commit();
    }
}
