/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.contentcontext.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.microsoft.device.display.samples.contentcontext.Item;
import com.microsoft.device.display.samples.contentcontext.R;

import java.util.ArrayList;

public class ItemsListFragment extends Fragment implements ListView.OnItemClickListener {
    private ArrayAdapter<Item> adapterItems;
    private ListView lvItems;
    private ArrayList<Item> items;
    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void onItemSelected(Item i, int position);
    }

    public void registerOnItemSelectedListener(OnItemSelectedListener l) {
        listener = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Item> items = Item.getItems();
        if (getActivity() != null) {
            adapterItems = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_activated_1, items);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);
        lvItems = view.findViewById(R.id.lvItems);
        lvItems.setAdapter(adapterItems);
        lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvItems.setOnItemClickListener(this);

        return view;
    }

    public void setSelectedItem(int position) {
        lvItems.setItemChecked(position, true);
        listener.onItemSelected(items.get(position), position);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowId) {
        Item i = adapterItems.getItem(position);
        listener.onItemSelected(i, position);
    }

    public static ItemsListFragment newInstance(ArrayList<Item> items) {
        ItemsListFragment fragment = new ItemsListFragment();
        fragment.items = items;
        return fragment;
    }
}
