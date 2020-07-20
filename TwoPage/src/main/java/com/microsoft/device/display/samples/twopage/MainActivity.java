/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
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

import com.microsoft.device.dualscreen.layout.ScreenHelper;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private int position;
    private boolean showTwoPages;
    private View single;
    private View dual;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SparseArray<TestFragment> fragments = TestFragment.getFragments();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        single = getLayoutInflater().inflate(R.layout.activity_main, null);
        dual = getLayoutInflater().inflate(R.layout.double_landscape_layout, null);
        setupLayout();
    }

    private void setupLayout() {
        if (ScreenHelper.isDualMode(this)) {
            useDualMode();
        } else {
            useSingleMode();
        }
    }

    private void useDualMode() {
        int rotation = ScreenHelper.getCurrentRotation(this);
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

    private void useSingleMode() {
        //Setting layout for single portrait
        setContentView(single);
        showTwoPages = false;
        setupViewPager();
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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) { }

}