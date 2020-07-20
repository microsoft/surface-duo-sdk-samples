/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.contentcontext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.microsoft.device.display.samples.contentcontext.model.MapPoint;
import com.microsoft.device.display.samples.contentcontext.view.MapImageView;
import com.microsoft.device.dualscreen.layout.ScreenHelper;

public class MapFragment extends Fragment {

    static MapFragment newInstance(MapPoint item) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(MapPoint.KEY, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);
        if (getArguments() != null) {
            MapPoint mapPoint = getArguments().getParcelable(MapPoint.KEY);
            if (mapPoint != null && mapPoint.getMapImageResourceID() != 0) {
                MapImageView mImageView = view.findViewById(R.id.img_view);
                mImageView.setImageResource(mapPoint.getMapImageResourceID());
                Toolbar detailToolbar = view.findViewById(R.id.detail_toolbar);
                detailToolbar.setTitle(mapPoint.getTitle());

                // Handle Toolbar visibility
                if (getActivity() != null && ScreenHelper.isDualMode(getActivity())) {
                    switch (ScreenHelper.getCurrentRotation(getActivity())) {
                        case Surface.ROTATION_0:
                        case Surface.ROTATION_180:
                            detailToolbar.setVisibility(View.VISIBLE);
                            break;
                        case Surface.ROTATION_90:
                        case Surface.ROTATION_270:
                            detailToolbar.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }
                } else {
                    detailToolbar.setTitle(mapPoint.getTitle());
                }

            }
        }
        return view;
    }

}
