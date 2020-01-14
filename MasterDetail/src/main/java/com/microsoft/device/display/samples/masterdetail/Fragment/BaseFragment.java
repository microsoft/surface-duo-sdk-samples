/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.masterdetail.Fragment;

import androidx.fragment.app.Fragment;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BaseFragment extends Fragment {
    public abstract boolean onBackPressed();

    public abstract int getCurrentSelectedPosition();

    public abstract void setCurrentSelectedPosition(int position);
}
