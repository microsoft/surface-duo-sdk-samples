/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.twopage;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TestFragment extends Fragment {

    private TextView mTextView;
    private String text;

    public static TestFragment newInstance(String text){
        TestFragment testFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        text = getArguments().getString("text");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        mTextView = (TextView) view.findViewById(R.id.textview);
        mTextView.setText(text);
        return view;
    }

    @Override
    public String toString() {
        return text;
    }

    // Init fragments for ViewPager
    public static SparseArray<TestFragment> getFragments() {
        SparseArray<TestFragment> fragments = new SparseArray<>();
        for(int i = 0; i < 10; i++) {
            fragments.put(i, TestFragment.newInstance("Page " + Integer.toString(i + 1)));
        }
        return fragments;
    }
}
