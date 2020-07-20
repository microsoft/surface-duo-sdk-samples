/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.draganddrop.fragment;

import android.content.ClipData;
import android.content.ContentResolver;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.DragAndDropPermissionsCompat;
import androidx.fragment.app.Fragment;

import com.microsoft.device.display.samples.draganddrop.R;

public class DropTargetFragment extends Fragment implements View.OnDragListener {

    private RelativeLayout imageDropContainer;
    private RelativeLayout textDropContainer;
    private ConstraintLayout imageHintContainer;
    private ConstraintLayout textHintContainer;

    public static DropTargetFragment newInstance() {
        return new DropTargetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drop_target_layout, container, false);

        imageHintContainer = view.findViewById(R.id.drop_image_hint);
        textHintContainer = view.findViewById(R.id.drop_text_hint);

        imageDropContainer = view.findViewById(R.id.drop_image_container);
        textDropContainer = view.findViewById(R.id.drop_text_container);
        imageDropContainer.setOnDragListener(this);
        textDropContainer.setOnDragListener(this);

        return view;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        String mimeType = "";

        if (event.getClipDescription() != null) {
            mimeType = event.getClipDescription().getMimeType(0);
        }

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (mimeType == null || "".equals(mimeType)) {
                    return false;
                }

                if (isImage(mimeType) || isText(mimeType)) {
                    setBackgroundColor(mimeType);
                    return true;
                }

                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                setBackgroundColor(mimeType);
                return true;

            case DragEvent.ACTION_DROP:
                if (isText(mimeType)) {
                    handleTextDrop(event);
                    v.setElevation(1);
                } else if (isImage(mimeType)) {
                    handleImageDrop(event);
                    v.setElevation(1);
                }
                clearBackgroundColor(mimeType);
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                clearBackgroundColor();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
            case DragEvent.ACTION_DRAG_EXITED:
                // Ignore events
                return true;

            default:
                break;
        }
        return false;
    }

    private void setBackgroundColor(String mimeType) {
        ColorFilter colorFilter = new PorterDuffColorFilter(Color.GRAY,
                PorterDuff.Mode.SRC_IN);
        if (isImage(mimeType)) {
            imageHintContainer.getBackground().setColorFilter(colorFilter);
            imageHintContainer.setElevation(4);
            imageHintContainer.invalidate();
        } else if (isText(mimeType)) {
            textHintContainer.getBackground().setColorFilter(colorFilter);
            textHintContainer.setElevation(4);
            textHintContainer.invalidate();
        }
    }

    private void clearBackgroundColor(String mimeType) {
        if (isImage(mimeType)) {
            imageHintContainer.getBackground().clearColorFilter();
            imageHintContainer.setElevation(0);
            imageHintContainer.invalidate();
        } else if (isText(mimeType)) {
            textHintContainer.getBackground().clearColorFilter();
            textHintContainer.setElevation(0);
            textHintContainer.invalidate();
        }
    }

    private void clearBackgroundColor() {
        imageHintContainer.getBackground().clearColorFilter();
        imageHintContainer.setElevation(0);
        imageHintContainer.invalidate();
        textHintContainer.getBackground().clearColorFilter();
        textHintContainer.setElevation(0);
        textHintContainer.invalidate();
    }

    private boolean isImage(String mime) {
        return mime.startsWith("image/");
    }

    private boolean isText(String mime) {
        return mime.startsWith("text/");
    }

    private void handleTextDrop(DragEvent event) {
        ClipData.Item item = event.getClipData().getItemAt(0);
        String dragData = item.getText().toString();
        View vw = (View) event.getLocalState();
        //Remove the local text view, vw is null if drop from another app
        if (vw != null) {
            ViewGroup owner = (ViewGroup) vw.getParent();
            owner.removeView(vw);
        } else {
            TextView textView = new TextView(this.getContext());
            textView.setText(dragData);
            vw = textView;
        }

        textDropContainer.removeAllViews();
        textDropContainer.addView(vw);
        vw.setVisibility(View.VISIBLE);
    }

    private void handleImageDrop(DragEvent event) {
        ClipData.Item item = event.getClipData().getItemAt(0);
        View vw = (View) event.getLocalState();
        //Remove the local image view, vw is null if drop from another app
        if (vw != null) {
            ViewGroup owner = (ViewGroup) vw.getParent();
            owner.removeView(vw);
        } else {
            ImageView imageView = new ImageView(this.getContext());
            Uri uri = item.getUri();
            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                // Accessing a "content" scheme Uri requires a permission grant.
                DragAndDropPermissionsCompat dropPermissions = ActivityCompat
                        .requestDragAndDropPermissions(this.getActivity(), event);

                if (dropPermissions == null) {
                    // Permission could not be obtained.
                    return;
                }

                imageView.setImageURI(uri);
            } else {
                // Other schemes (such as "android.resource") do not require a permission grant.
                imageView.setImageURI(uri);
            }

            vw = imageView;
        }

        imageDropContainer.removeAllViews();
        imageDropContainer.addView(vw);
        vw.setVisibility(View.VISIBLE);
    }
}
