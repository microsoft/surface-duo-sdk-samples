/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.complementarycontext.Fragment;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.microsoft.device.display.samples.complementarycontext.Adapter.PagerAdapter;
import com.microsoft.device.display.samples.complementarycontext.R;
import com.microsoft.device.display.samples.complementarycontext.Slide;

import java.util.ArrayList;

public class SinglePortrait extends BaseFragment implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private SparseArray<SlideFragment> fragments;
    private int currentPosition;

    public static SinglePortrait newInstance(ArrayList<Slide> slides) {
        SinglePortrait singlePortrait = new SinglePortrait();
        singlePortrait.fragments = SlideFragment.getFragments(slides);
        singlePortrait.currentPosition = 0;
        return singlePortrait;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_portrait, container, false);
        viewPager = view.findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentPosition);
        viewPager.addOnPageChangeListener(this);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            viewPager.setCurrentItem(currentPosition, false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        if (listener != null) {
            listener.onItemSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //
    }

    public void setCurrentPosition(int position) {
        currentPosition = position;
    }
}
