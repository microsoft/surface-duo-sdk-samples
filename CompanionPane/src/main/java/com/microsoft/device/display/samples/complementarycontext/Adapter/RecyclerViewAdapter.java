/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.complementarycontext.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.microsoft.device.display.samples.complementarycontext.R;
import com.microsoft.device.display.samples.complementarycontext.Slide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.SlideViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final ArrayList<Slide> slides;
    private int currentPosition;

    public RecyclerViewAdapter(Context context, ArrayList<Slide> slides) {
        mLayoutInflater = LayoutInflater.from(context);
        this.slides = slides;
    }

    @Override
    @NonNull
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(mLayoutInflater.inflate(R.layout.item_slide, parent, false));
    }

    @Override
    public void onBindViewHolder(SlideViewHolder holder, int position) {
        holder.content.setText(slides.get(position).getContent());
        holder.title.setText(slides.get(position).getTitle());
        if (currentPosition == position) {
            holder.setSelected(true);
        } else {
            holder.setSelected(false);
        }
    }

    public void setCurrentPosition(int position) {
        currentPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return slides.size();
    }

    @SuppressWarnings("CanBeFinal")
    public static class SlideViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView title;
        CardView cardView;

        SlideViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.slide_title);
            content = view.findViewById(R.id.slide_content);
            cardView = view.findViewById(R.id.card_view);
        }

        @SuppressWarnings("WeakerAccess")
        public void setSelected(boolean selected) {
            cardView.setSelected(selected);
        }
    }
}
