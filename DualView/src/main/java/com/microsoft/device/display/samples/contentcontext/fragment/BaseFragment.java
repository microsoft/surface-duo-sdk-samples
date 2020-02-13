/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.contentcontext.fragment;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    OnItemSelectedListener listener;

    public void registerOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public abstract boolean onBackPressed();

    public abstract void setCurrentSelectedPosition(int position);
}
