/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.draganddrop.fragment;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.microsoft.device.display.samples.draganddrop.R;

public class DragSourceFragment extends Fragment implements View.OnLongClickListener {

    public static DragSourceFragment newInstance() {
        return new DragSourceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drag_source_layout, container, false);

        TextView dragTextView = view.findViewById(R.id.drag_text_view);
        ImageView dragImageView = view.findViewById(R.id.drag_image_view);

        dragTextView.setTag("text_view");
        dragImageView.setTag("image_view");
        dragTextView.setOnLongClickListener(this);
        dragImageView.setOnLongClickListener(this);

        return view;
    }

    @Override
    public boolean onLongClick(View v) {
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        String[] mimeTypes = new String[1];

        if (v instanceof ImageView) {
            mimeTypes[0] = "image/jpeg";
        } else if (v instanceof TextView) {
            mimeTypes[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;
        }
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
        v.startDragAndDrop(data, dragShadowBuilder, v, 0);
        return true;
    }
}