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

public class SinglePortrait extends BaseFragment implements ItemsListFragment.OnItemSelectedListener {
    private int currentSelectedPosition = 0;
    private ItemsListFragment itemListFragment;
    private ArrayList<Item> items;

    public static SinglePortrait newInstance(ArrayList<Item> items) {
        SinglePortrait singlePortrait = new SinglePortrait();
        singlePortrait.items = items;
        return singlePortrait;
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
        View view = inflater.inflate(R.layout.fragment_single_portrait, container, false);
        showFragment(itemListFragment, R.id.master_single);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            // Current view is detail view
            if (getChildFragmentManager().getBackStackEntryCount() == 2) {
                showBackOnActionBar(true);
                onBackPressed();
            }
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
            fragmentTransaction.hide(showFragment).add(id, fragment);
        }
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    @Override
    public void onItemSelected(Item item, int position) {
        if (listener != null) {
            listener.onItemSelected(position);
        }
        currentSelectedPosition = position;
        showBackOnActionBar(true);
        showFragment(ItemDetailFragment.newInstance(item), R.id.master_single);
    }

    private void showBackOnActionBar(boolean show) {
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(show);
        actionbar.setHomeButtonEnabled(show);
    }

    @Override
    public boolean onBackPressed() {
        // Current view is listview
        if (getChildFragmentManager().getBackStackEntryCount() == 1) {
            return true;
        } else {
            getChildFragmentManager().popBackStack();
            getChildFragmentManager().executePendingTransactions();
            // Do not show back on the actionbar when current fragment is ItemsListFragment
            final Fragment showFragment = getChildFragmentManager().findFragmentById(R.id.master_single);
            if (showFragment instanceof ItemsListFragment) {
                showBackOnActionBar(false);
            }
        }
        return false;
    }

    @Override
    public int getCurrentSelectedPosition() {
        return currentSelectedPosition;
    }

    @Override
    public void setCurrentSelectedPosition(int position) {
        if (currentSelectedPosition != position) {
            currentSelectedPosition = position;
        }
    }
}
