/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.twopage;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Surface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.microsoft.device.display.samples.utils.ScreenHelper;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ScreenHelper screenHelper;
    private SparseArray<TestFragment> fragments;
    private int position = 0;
    private boolean isDuo;
    private boolean showTwoPages = false;
    private View single;
    private View dual;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments = TestFragment.getFragments();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        screenHelper = new ScreenHelper();
        isDuo = screenHelper.initialize(this);
        single = getLayoutInflater().inflate(R.layout.activity_main, null);
        dual = getLayoutInflater().inflate(R.layout.double_landscape_layout, null);
        setupLayout();
    }

    public void useSingleMode(int rotation) {
        //Setting layout for single portrait
        setContentView(single);
        showTwoPages = false;
        setupViewPager();
    }

    public void useDualMode(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                // Setting layout for double landscape
                setContentView(dual);
                showTwoPages = false;
                break;
            default:
                // Setting layout for double portrait
                setContentView(single);
                showTwoPages = true;
                break;
        }
        setupViewPager();
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

    private void setupViewPager() {
        pagerAdapter.showTwoPages(showTwoPages);
        if (viewPager != null) {
            viewPager.setAdapter(null);
        }
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //
    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //
    }

}