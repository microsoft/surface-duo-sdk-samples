/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.masterdetail.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.microsoft.device.display.samples.masterdetail.Item;
import com.microsoft.device.display.samples.masterdetail.R;

public class ItemDetailFragment extends Fragment {
    private Item item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (Item) getArguments().getSerializable("item");
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_item_detail,
                container,
                false
        );
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvBody = view.findViewById(R.id.tvBody);
        RatingBar ratingBar = view.findViewById(R.id.rating);
        ImageView image = view.findViewById(R.id.image);

        tvTitle.setText(item.getTitle());
        tvBody.setText(item.getBody());
        image.setImageResource(R.drawable.ic_movie_black_24dp);
        ratingBar.setRating(2);

        return view;
    }

    static ItemDetailFragment newInstance(Item item) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragment.setArguments(args);
        return fragment;
    }
}
