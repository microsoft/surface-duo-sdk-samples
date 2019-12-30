package com.microsoft.device.display.samples.twopage;

import android.util.Log;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = PagerAdapter.class.getSimpleName();
    private SparseArray<TestFragment> fragments;
    private boolean showTwoPages = false;

    public PagerAdapter(FragmentManager fm, SparseArray<TestFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: " + position);
        TestFragment testFragment = fragments.valueAt(position);
        return testFragment;
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

    public void showTwoPages(boolean showTwoPages) {
        this.showTwoPages = showTwoPages;
    }
}
