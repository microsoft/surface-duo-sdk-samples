/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.listdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.microsoft.device.display.samples.listdetail.model.DataProvider;
import com.microsoft.device.display.samples.listdetail.model.MovieMock;
import com.microsoft.device.dualscreen.layout.ScreenHelper;

import java.util.List;

public class ItemsListFragment extends Fragment implements ListView.OnItemClickListener {
    private ArrayAdapter<MovieMock> arrayAdapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<MovieMock> movieMocks = DataProvider.getMovieMocks();

        if (getActivity() != null) {
            arrayAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_list_item_activated_1,
                    movieMocks
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);
        listView = view.findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(this);
        return view;
    }

    private void setSelectedItem(int position) {
        listView.setItemChecked(position, true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowId) {
        MovieMock movieMock = arrayAdapter.getItem(position);
        setSelectedItem(position);

        if (getActivity() != null && ScreenHelper.isDualMode(getActivity())) {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.dual_screen_end_container_id,
                                ItemDetailFragment.newInstance(movieMock),
                                null
                        )
                        .commit();
            }
        } else {
            startDetailsFragment(movieMock);
        }
    }

    private void startDetailsFragment(MovieMock movieMock) {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction()
                    .replace(
                            R.id.single_screen_container_id,
                            ItemDetailFragment.newInstance(movieMock),
                            null
                    ).addToBackStack(null)
                    .commit();
        }
    }
}
