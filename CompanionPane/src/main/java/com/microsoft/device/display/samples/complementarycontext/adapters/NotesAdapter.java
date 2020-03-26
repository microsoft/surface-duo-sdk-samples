/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.complementarycontext.adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.microsoft.device.display.samples.complementarycontext.R;
import com.microsoft.device.display.samples.complementarycontext.model.Slide;

public class NotesAdapter extends ListAdapter<Slide, NotesAdapter.NoteViewHolder> {
    public static int selectionPosition = 0;
    public static int oldSelectionPosition = 0;
    private ViewPager2 slidesPager;

    public void setSlidesPager(ViewPager2 slidesPager) {
        this.slidesPager = slidesPager;
    }

    public NotesAdapter() {
        super(new Comparator());
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View slide = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_notes, parent, false);

        return new NoteViewHolder(slide);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(getItem(position), position);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        final TextView slideContent;
        final TextView slideNote;
        final CardView cardView;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            slideContent = itemView.findViewById(R.id.slide_content);
            slideNote = itemView.findViewById(R.id.slide_note);
            cardView = itemView.findViewById(R.id.card_view);
        }

        void bind(Slide slide, final int position) {
            slideContent.setText(slide.getContent());
            slideNote.setText(slide.getNote());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oldSelectionPosition = selectionPosition;
                    selectionPosition = position;
                    notifyItemChanged(oldSelectionPosition);
                    cardView.setSelected(true);
                    slidesPager.setCurrentItem(position, true);
                }
            });

            if (selectionPosition == position) {
                cardView.setSelected(true);
            } else {
                cardView.setSelected(false);
            }
        }
    }
}