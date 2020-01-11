/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.complementarycontext.Adapter;

import android.util.Log;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.microsoft.device.display.samples.complementarycontext.Fragment.SlideFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private SparseArray<SlideFragment> fragments;

    public PagerAdapter(FragmentManager fm, SparseArray<SlideFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        SlideFragment fragment = fragments.valueAt(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
