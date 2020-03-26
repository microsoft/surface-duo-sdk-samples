/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.complementarycontext.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.microsoft.device.display.samples.complementarycontext.model.Slide;

public class Comparator extends DiffUtil.ItemCallback<Slide> {
    @Override
    public boolean areItemsTheSame(@NonNull Slide oldItem, @NonNull Slide newItem) {
        return oldItem.getNote().equals(newItem.getNote());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Slide oldItem, @NonNull Slide newItem) {
        return oldItem.equals(newItem);
    }
}