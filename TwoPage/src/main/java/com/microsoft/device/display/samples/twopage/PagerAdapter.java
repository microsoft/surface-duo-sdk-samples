/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.twopage;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class PagerAdapter extends FragmentPagerAdapter {
    private final SparseArray<TestFragment> fragments;
    private boolean showTwoPages;

    PagerAdapter(FragmentManager fm, SparseArray<TestFragment> fragments) {
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

    @Override
    public float getPageWidth(int position) {
        // 0.5f : Each pages occupy full space
        // 1.0f : Each pages occupy half space
        return showTwoPages ? 0.5f : 1.0f;
    }

    void showTwoPages(boolean showTwoPages) {
        this.showTwoPages = showTwoPages;
    }
}
