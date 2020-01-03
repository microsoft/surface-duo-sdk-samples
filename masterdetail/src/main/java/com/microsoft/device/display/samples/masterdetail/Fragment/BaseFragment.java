package com.microsoft.device.display.samples.masterdetail.Fragment;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    public abstract boolean onBackPressed();
    public abstract int getCurrentSelectedPosition();
    public abstract void setCurrentSelectedPosition(int position);
}
