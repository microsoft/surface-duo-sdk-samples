package com.microsoft.device.display.samples.complementarycontext.Adapter;

import android.util.Log;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.microsoft.device.display.samples.complementarycontext.Fragment.SlideFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = PagerAdapter.class.getSimpleName();
    private SparseArray<SlideFragment> fragments;

    public PagerAdapter(FragmentManager fm, SparseArray<SlideFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: " + position);
        SlideFragment fragment = fragments.valueAt(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
