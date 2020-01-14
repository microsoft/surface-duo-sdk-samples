/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.contentcontext;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.microsoft.device.display.samples.contentcontext.Fragment.BaseFragment;
import com.microsoft.device.display.samples.contentcontext.Fragment.DualLandscape;
import com.microsoft.device.display.samples.contentcontext.Fragment.DualPortrait;
import com.microsoft.device.display.samples.contentcontext.Fragment.SinglePortrait;
import com.microsoft.device.display.samples.utils.ScreenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnItemSelectedListener {

    private ScreenHelper screenHelper;
    private boolean isDuo;
    private Map<String, BaseFragment> fragmentMap;
    private int currentSelectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenHelper = new ScreenHelper();
        isDuo = screenHelper.initialize(this);
        ArrayList<Item> items = Item.getItems();
        fragmentMap = new HashMap<>();

        SinglePortrait singlePortrait = SinglePortrait.newInstance(items);
        singlePortrait.registerOnItemSelectedListener(this);
        fragmentMap.put(SinglePortrait.class.getSimpleName(), singlePortrait);

        DualPortrait dualPortrait = DualPortrait.newInstance(items);
        dualPortrait.registerOnItemSelectedListener(this);
        fragmentMap.put(DualPortrait.class.getSimpleName(), dualPortrait);

        DualLandscape dualLandscape = DualLandscape.newInstance(items);
        dualLandscape.registerOnItemSelectedListener(this);
        fragmentMap.put(DualLandscape.class.getSimpleName(), dualLandscape);

        setupLayout();
    }

    private void useSingleMode(int rotation) {
        BaseFragment baseFragment = fragmentMap.get(SinglePortrait.class.getSimpleName());
        if (baseFragment != null) {
            showFragment(baseFragment, R.id.activity_main);
        }
    }

    private void useDualMode(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                // Setting layout for double landscape
                BaseFragment baseFragment = fragmentMap.get(DualLandscape.class.getSimpleName());
                if (baseFragment != null) {
                    showFragment(baseFragment, R.id.activity_main);
                }
                break;
            default:
                BaseFragment baseFragment1 = fragmentMap.get(DualPortrait.class.getSimpleName());
                if (baseFragment1 != null) {
                    showFragment(baseFragment1, R.id.activity_main);
                }
                break;
        }
    }

    private void setupLayout() {
        int rotation = ScreenHelper.getRotation(this);
        if (isDuo) {
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

    private void showFragment(BaseFragment fragment, int id) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(id, fragment);
        }

        fragmentTransaction.show(fragment);

        if (currentSelectedPosition != -1) {
            fragment.setCurrentSelectedPosition(currentSelectedPosition);
        }

        for (Map.Entry<String, BaseFragment> stringBaseFragmentEntry : fragmentMap.entrySet()) {
            if (((Map.Entry) stringBaseFragmentEntry).getValue() != fragment) {
                fragmentTransaction.hide((Fragment) ((Map.Entry) stringBaseFragmentEntry).getValue());
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        setTitle(R.string.app_name);
        for (Map.Entry<String, BaseFragment> stringBaseFragmentEntry : fragmentMap.entrySet()) {
            BaseFragment fragment = (BaseFragment) ((Map.Entry) stringBaseFragmentEntry).getValue();
            if (fragment.isVisible()) {
                if (fragment.onBackPressed()) {
                    this.finish();
                }
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

    @Override
    public void onItemSelected(int position) {
        currentSelectedPosition = position;
    }
}