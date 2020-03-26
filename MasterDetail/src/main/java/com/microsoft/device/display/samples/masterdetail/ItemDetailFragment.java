/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.masterdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.microsoft.device.display.samples.masterdetail.model.MovieMock;

public class ItemDetailFragment extends Fragment {
    private MovieMock movieMock;

    static ItemDetailFragment newInstance(MovieMock movieMock) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(MovieMock.KEY, movieMock);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieMock = (MovieMock) getArguments().getSerializable(MovieMock.KEY);
        }
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

        if (movieMock != null) {
            tvTitle.setText(movieMock.getTitle());
            tvBody.setText(movieMock.getBody());
            ratingBar.setRating(2);
        }
        image.setImageResource(R.drawable.ic_movie_black_24dp);

        return view;
    }
}
