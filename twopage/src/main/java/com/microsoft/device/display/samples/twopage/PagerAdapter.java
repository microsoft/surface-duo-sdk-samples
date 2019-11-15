package com.microsoft.device.display.samples.twopage;

import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "PagerAdapter";
    private SparseArray<TestFragment> mFragments;
    private boolean isShowTwoPages = false;

    public PagerAdapter(FragmentManager fm, SparseArray<TestFragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: " + position);
        TestFragment testFragment = mFragments.valueAt(position);
        return testFragment;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public long getItemId(int position) {
        return mFragments.keyAt(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public float getPageWidth(int position) {
        return isShowTwoPages ? 0.5f : 1.0f;
    }

    public void setShowTwoPages(boolean showTwoPages) {
        this.isShowTwoPages = showTwoPages;
    }

}
