package com.microsoft.device.display.samples.complementarycontext.Fragment;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    public void registerOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }
}
