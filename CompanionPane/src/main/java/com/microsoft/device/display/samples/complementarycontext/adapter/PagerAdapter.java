/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.complementarycontext.adapter;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.microsoft.device.display.samples.complementarycontext.fragment.SlideFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private final SparseArray<SlideFragment> fragments;

    public PagerAdapter(FragmentManager fm, SparseArray<SlideFragment> fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        return fragments.valueAt(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
