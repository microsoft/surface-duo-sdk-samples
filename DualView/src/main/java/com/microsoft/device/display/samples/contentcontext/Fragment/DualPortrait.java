/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.contentcontext.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.microsoft.device.display.samples.contentcontext.Item;
import com.microsoft.device.display.samples.contentcontext.R;

import java.util.ArrayList;

public class DualPortrait extends BaseFragment implements ItemsListFragment.OnItemSelectedListener {
    private int currentSelectedPosition;
    private ItemsListFragment itemListFragment;
    private ArrayList<Item> items;

    public static DualPortrait newInstance(ArrayList<Item> items) {
        DualPortrait dualPortrait = new DualPortrait();
        dualPortrait.items = items;
        return dualPortrait;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemListFragment = ItemsListFragment.newInstance(items);
        itemListFragment.registerOnItemSelectedListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dual_portrail, container, false);
        showFragment(itemListFragment, R.id.master_dual);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            showBackOnActionBar(false);
            itemListFragment.setSelectedItem(currentSelectedPosition);
        }
    }

    private void showFragment(Fragment fragment, int id) {
        final FragmentManager fragmentManager = this.getChildFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Fragment showFragment = fragmentManager.findFragmentById(id);
        if (showFragment == null) {
            fragmentTransaction.add(id, fragment);
        } else {
            fragmentTransaction.remove(showFragment).add(id, fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onItemSelected(Item item, int position) {
        if (listener != null) {
            listener.onItemSelected(position);
        }
        currentSelectedPosition = position;
        // Showing ItemDetailFragment on the right screen when the app is in spanned mode
        showFragment(ItemDetailFragment.newInstance(item), R.id.master_detail);
    }

    private void showBackOnActionBar(boolean show) {
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(show);
        actionbar.setHomeButtonEnabled(show);
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public int getCurrentSelectedPosition() {
        return currentSelectedPosition;
    }

    @Override
    public void setCurrentSelectedPosition(int position) {
        currentSelectedPosition = position;
    }
}
