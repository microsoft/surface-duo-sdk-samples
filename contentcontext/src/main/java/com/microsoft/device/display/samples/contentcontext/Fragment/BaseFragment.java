package com.microsoft.device.display.samples.contentcontext.Fragment;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
    protected OnItemSelectedListener listener;

    public void registerOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public abstract boolean onBackPressed();
    public abstract int getCurrentSelectedPosition();
    public abstract void setCurrentSelectedPosition(int position);
}
