/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.contentcontext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.microsoft.device.display.samples.contentcontext.model.DataProvider;
import com.microsoft.device.display.samples.contentcontext.model.MapPoint;
import com.microsoft.device.dualscreen.layout.ScreenHelper;
import com.microsoft.device.dualscreen.layout.ScreenModeListener;

import java.util.ArrayList;

public class MapPointListFragment extends Fragment {
    private ArrayAdapter<MapPoint> adapterItems;
    private ListView lvItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<MapPoint> mapPoints = DataProvider.getMapPoints();
        if (getActivity() != null) {
            adapterItems = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_activated_1, mapPoints);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);
        lvItems = view.findViewById(R.id.lvItems);
        lvItems.setAdapter(adapterItems);
        lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        setOnLickListenerForListView(lvItems);
        handleSpannedModeSelection();

        return view;
    }

    private void setOnLickListenerForListView(final ListView lvItems) {
        // Handle OnListItemClick depending on screen mode
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvItems.setItemChecked(position, true);
                MapPoint mapPoint = adapterItems.getItem(position);
                if (getActivity() != null && ScreenHelper.isDualMode(getActivity())) {
                    if (getFragmentManager() != null) {
                        getFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.dual_screen_end_container_id,
                                        MapFragment.newInstance(mapPoint),
                                        null
                                )
                                .commit();
                    }
                } else {
                    startDetailsFragment(mapPoint);
                }
            }
        });
    }

    private void startDetailsFragment(MapPoint mapPoint) {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction()
                    .replace(
                            R.id.activity_main,
                            MapFragment.newInstance(mapPoint),
                            null
                    ).addToBackStack(null)
                    .commit();
        }
    }

    private void handleSpannedModeSelection() {
        if (getActivity() != null) {
            ((DualViewApp) getActivity().getApplication()).getSurfaceDuoScreenManager()
                    .addScreenModeListener(new ScreenModeListener() {
                        @Override
                        public void onSwitchToSingleScreenMode() {}

                        @Override
                        public void onSwitchToDualScreenMode() {
                            int position = 0;
                            lvItems.setItemChecked(position, true);

                            MapPoint mapPoint = adapterItems.getItem(position);
                            if (getFragmentManager() != null) {
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(
                                                R.id.dual_screen_end_container_id,
                                                MapFragment.newInstance(mapPoint),
                                                null
                                        )
                                        .commit();
                            }
                        }
                    });
        }
    }
}
