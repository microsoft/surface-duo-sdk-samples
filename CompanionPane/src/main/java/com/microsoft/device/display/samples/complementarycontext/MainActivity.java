/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.complementarycontext;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.microsoft.device.display.samples.complementarycontext.fragment.BaseFragment;
import com.microsoft.device.display.samples.complementarycontext.fragment.DualLandscape;
import com.microsoft.device.display.samples.complementarycontext.fragment.DualPortrait;
import com.microsoft.device.display.samples.complementarycontext.fragment.SinglePortrait;
import com.microsoft.device.display.samples.utils.ScreenHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnItemSelectedListener {
    private SinglePortrait singlePortrait;
    private DualPortrait dualPortrait;
    private DualLandscape dualLandscape;
    private ScreenHelper screenHelper;
    private boolean isDuo;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenHelper = new ScreenHelper();
        isDuo = screenHelper.initialize(this);
        ArrayList<Slide> slides = Slide.getSlides();
        currentPosition = 0;
        singlePortrait = SinglePortrait.newInstance(slides);
        singlePortrait.registerOnItemSelectedListener(this);
        dualPortrait = DualPortrait.newInstance(slides);
        dualPortrait.registerOnItemSelectedListener(this);
        dualLandscape = DualLandscape.newInstance(slides);
        dualLandscape.registerOnItemSelectedListener(this);
        setupLayout();
    }

    private void showFragment(Fragment fragment) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.activity_main, fragment);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    private void hideFragment(Fragment fragment) {
        if (fragment.isAdded()) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupLayout();
    }

    private void useDualMode(int rotation) {
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            dualLandscape.setCurrentPosition(currentPosition);
            showFragment(dualLandscape);
            hideFragment(dualPortrait);
            hideFragment(singlePortrait);
        } else {
            dualPortrait.setCurrentPosition(currentPosition);
            showFragment(dualPortrait);
            hideFragment(singlePortrait);
            hideFragment(dualLandscape);
        }
    }

    private void useSingleMode() {
        singlePortrait.setCurrentPosition(currentPosition);
        showFragment(singlePortrait);
        hideFragment(dualLandscape);
        hideFragment(dualPortrait);
    }

    private void setupLayout() {
        int rotation = ScreenHelper.getRotation(this);
        if (isDuo) {
            if (screenHelper.isDualMode()) {
                useDualMode(rotation);
            } else {
                useSingleMode();
            }
        } else {
            useSingleMode();
        }
    }

    @Override
    public void onItemSelected(int position) {
        currentPosition = position;
    }
}
