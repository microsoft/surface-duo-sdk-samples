/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.complementarycontext.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.microsoft.device.display.samples.complementarycontext.R;
import com.microsoft.device.display.samples.complementarycontext.model.Slide;

public class SlidesAdapter extends ListAdapter<Slide, SlidesAdapter.SlideViewHolder> {
    public SlidesAdapter() {
        super(new Comparator());
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View slide = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.item_slides, parent, false);

        return new SlideViewHolder(slide);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class SlideViewHolder extends RecyclerView.ViewHolder {
        final TextView slide;

        SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            slide = itemView.findViewById(R.id.slide);
        }

        void bind(Slide slide) {
            this.slide.setText(slide.getContent());
        }
    }
}