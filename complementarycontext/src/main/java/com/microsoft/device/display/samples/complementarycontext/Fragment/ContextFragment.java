/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.complementarycontext.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.microsoft.device.display.samples.complementarycontext.Adapter.RecyclerViewAdapter;
import com.microsoft.device.display.samples.complementarycontext.R;
import com.microsoft.device.display.samples.complementarycontext.Slide;

import java.util.ArrayList;

public class ContextFragment extends Fragment {
    private static final String TAG = ContextFragment.class.getSimpleName();
    private ArrayList<Slide> slides;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private OnItemSelectedListener listener;
    private int prevSelectedPosition = 0;

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    public static ContextFragment newInstance(ArrayList<Slide> slides) {
        ContextFragment contextFragment = new ContextFragment();
        contextFragment.slides = slides;
        return contextFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_context_layout, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        setupRecyclerView();
        return view;
    }

    public void addOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    private void setupRecyclerView() {
        recyclerViewAdapter = new RecyclerViewAdapter(recyclerView.getContext(), slides);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));

        final GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    int position = recyclerView.getChildLayoutPosition(childView);
                    if(listener != null) {
                        listener.onItemSelected(position);
                    }
                    return true;
                }
                return super.onSingleTapUp(e);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (gestureDetector.onTouchEvent(e)) {
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                //
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                //
            }
        });
    }

    public void setCurrentItem(int position) {
        if(prevSelectedPosition != position) {
            int scrollTo = 0;
            if (prevSelectedPosition - position > 0) {
                scrollTo = position - 1;
            } else {
                scrollTo = position + 1;
            }
            if (scrollTo < 0) {
                scrollTo = 0;
            }
            prevSelectedPosition = position;
            recyclerView.smoothScrollToPosition(scrollTo);
            recyclerViewAdapter.setCurrentPosition(position);
        }
    }
}
