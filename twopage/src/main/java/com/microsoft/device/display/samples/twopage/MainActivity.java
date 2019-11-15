package com.microsoft.device.display.samples.twopage;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.microsoft.device.display.samples.twopage.eos.DualScreenDetectionListener;
import com.microsoft.device.display.samples.twopage.eos.DualScreenHelper;

public class MainActivity extends AppCompatActivity implements DualScreenDetectionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private DualScreenHelper mDualScreenHelper;
    private SparseArray<TestFragment> fragments;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        if(savedInstanceState != null) {
            position = savedInstanceState.getInt("position",0);
            Log.d(TAG,"onCreate " + position);
        }

        mDualScreenHelper = new DualScreenHelper();
        boolean isDuo = mDualScreenHelper.initialize(this, getWindow().getDecorView().getRootView());
        if(!isDuo) {
            useSingleScreenMode(Surface.ROTATION_0);
        } else {
            mDualScreenHelper.addListener(this);
        }
    }

    @Override
    public void useSingleScreenMode(int rotation) {
        setContentView(R.layout.activity_main);
        setupViewPager(false);
    }

    @Override
    public void useDualScreenMode(int rotation, Rect screenRect1, Rect screenRect2) {

        switch (rotation) {
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                setContentView(R.layout.double_landscape_layout);
                setupViewPager(false);
                break;
            default:
                setContentView(R.layout.activity_main);
                setupViewPager(true);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int position = viewPager.getCurrentItem();
        Log.d(TAG,"onSaveInstanceState position " + position);
        outState.putInt("position", position);
        super.onSaveInstanceState(outState);
    }

    private void setupViewPager(boolean showTwoPages) {
        fragments = TestFragment.getFragments();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        pagerAdapter.setShowTwoPages(showTwoPages);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);
    }
}