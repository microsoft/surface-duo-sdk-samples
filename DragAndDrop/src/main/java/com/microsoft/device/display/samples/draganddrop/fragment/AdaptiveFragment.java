/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.draganddrop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.microsoft.device.display.samples.draganddrop.R;

public class AdaptiveFragment extends Fragment {

    public static final String KEY_LAYOUT_ID = "layoutId";
    private DragSourceFragment dragSourceFragment;
    private DropTargetFragment dropTargetFragment;

    public static AdaptiveFragment newInstance() {
        return new AdaptiveFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dragSourceFragment = DragSourceFragment.newInstance();
        dropTargetFragment = DropTargetFragment.newInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        int layoutId = bundle != null ?
                this.getArguments().getInt(KEY_LAYOUT_ID, R.layout.fragment_single_portrait)
                : R.layout.fragment_single_portrait;
        View view = inflater.inflate(layoutId, container, false);

        final FragmentManager fragmentManager = this.getChildFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.drag_source_container, dragSourceFragment)
                .add(R.id.drop_target_container, dropTargetFragment)
                .commit();
        return view;
    }
}
