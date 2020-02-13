/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.complementarycontext.fragment;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.microsoft.device.display.samples.complementarycontext.adapter.PagerAdapter;
import com.microsoft.device.display.samples.complementarycontext.R;
import com.microsoft.device.display.samples.complementarycontext.Slide;

import java.util.ArrayList;

public class DualLandscape extends BaseFragment implements ViewPager.OnPageChangeListener, ContextFragment.OnItemSelectedListener {
    private ArrayList<Slide> slides;
    private ViewPager viewPager;
    private ContextFragment contextFragment;
    private int currentPosition;

    public static DualLandscape newInstance(ArrayList<Slide> slides) {
        DualLandscape dualLandscape = new DualLandscape();
        dualLandscape.slides = slides;
        dualLandscape.currentPosition = 0;
        dualLandscape.contextFragment = ContextFragment.newInstance(slides);
        return dualLandscape;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dual_landscape, container, false);
        viewPager = view.findViewById(R.id.pager);
        contextFragment.addOnItemSelectedListener(this);
        showFragment(contextFragment);
        setupViewPager();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrentPosition();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setCurrentPosition();
        }
    }

    private void showFragment(Fragment fragment) {
        final FragmentManager fragmentManager = getChildFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.all_slides, fragment);
        }
        fragmentTransaction.commit();
    }

    private void setupViewPager() {
        SparseArray<SlideFragment> slideFragments = SlideFragment.getFragments(slides);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), slideFragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentPosition, false);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {
        contextFragment.setCurrentItem(position);
        currentPosition = position;
        if (listener != null) {
            listener.onItemSelected(position);
        }
    }

    @Override
    public void onItemSelected(int position) {
        viewPager.setCurrentItem(position);
        currentPosition = position;
        if (listener != null) {
            listener.onItemSelected(position);
        }
    }

    private void setCurrentPosition() {
        contextFragment.setCurrentItem(currentPosition);
        viewPager.setCurrentItem(currentPosition, false);
    }

    public void setCurrentPosition(int position) {
        currentPosition = position;
    }
}
