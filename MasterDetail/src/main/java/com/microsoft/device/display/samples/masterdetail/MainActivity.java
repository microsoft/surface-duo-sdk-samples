/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.masterdetail;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.microsoft.device.display.samples.masterdetail.fragment.DualPortrait;
import com.microsoft.device.display.samples.masterdetail.fragment.SinglePortrait;
import com.microsoft.device.display.samples.utils.ScreenHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ScreenHelper screenHelper;
    private boolean isDuo;
    private SinglePortrait singlePortrait;
    private DualPortrait dualPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenHelper = new ScreenHelper();
        isDuo = screenHelper.initialize(this);
        ArrayList<Item> items = Item.getItems();
        singlePortrait = SinglePortrait.newInstance(items);
        dualPortrait = DualPortrait.newInstance(items);
        setupLayout();
    }

    private void useSingleMode() {
        showFragment(singlePortrait);
    }

    private void useDualMode(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                // Setting layout for double landscape
                useSingleMode();
                break;
            default:
                showFragment(dualPortrait);
                break;
        }
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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupLayout();
    }

    private void showFragment(Fragment fragment) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.activity_main, fragment);
        }
        if (fragment instanceof SinglePortrait) {
            fragmentTransaction.hide(dualPortrait);
            fragmentTransaction.show(singlePortrait);
            singlePortrait.setCurrentSelectedPosition(dualPortrait.getCurrentSelectedPosition());
        } else {
            fragmentTransaction.show(dualPortrait);
            fragmentTransaction.hide(singlePortrait);
            dualPortrait.setCurrentSelectedPosition(singlePortrait.getCurrentSelectedPosition());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (singlePortrait.isVisible()) {
            if (singlePortrait.onBackPressed()) {
                this.finish();
            }
        } else {
            if (dualPortrait.onBackPressed()) {
                this.finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}