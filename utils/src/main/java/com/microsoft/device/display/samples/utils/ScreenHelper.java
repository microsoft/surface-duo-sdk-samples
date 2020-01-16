/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.WindowManager;

import com.microsoft.device.display.DisplayMask;

import java.util.List;

public class ScreenHelper {
    private DisplayMask mDisplayMask;
    private Activity mActivity;

    public boolean initialize(Activity activity) {
        try {
            mActivity = activity;
            mDisplayMask = DisplayMask.fromResourcesRectApproximation(mActivity);
            if (mDisplayMask == null) {
                return false;
            }
        } catch (NoSuchMethodError | RuntimeException | NoClassDefFoundError ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private int getRotation() {
        return getRotation(mActivity);
    }

    private Rect getHinge(int rotation) {
        // Hinge's coordinates of its 4 edges in different mode
        // Double Landscape Rect(0, 1350 - 1800, 1434)
        // Double Portrait  Rect(1350, 0 - 1434, 1800)
        List<Rect> boundings = mDisplayMask.getBoundingRectsForRotation(rotation);
        return boundings.get(0);
    }

    private Rect getWindowRect() {
        Rect windowRect = new Rect();
        mActivity.getWindowManager().getDefaultDisplay().getRectSize(windowRect);
        return windowRect;
    }

    private void getScreenRects(Rect windowRect, Rect hinge, Rect screenRect1, Rect screenRect2) {
        // Hinge's coordinates of its 4 edges in different mode
        // Double Landscape Rect(0, 1350 - 1800, 1434)
        // Double Portrait  Rect(1350, 0 - 1434, 1800)
        if (hinge.left > 0) {
            screenRect1.left = 0;
            screenRect1.right = hinge.left;
            screenRect1.top = 0;
            screenRect1.bottom = windowRect.bottom;
            screenRect2.left = hinge.right;
            screenRect2.right = windowRect.right;
            screenRect2.top = 0;
            screenRect2.bottom = windowRect.bottom;
        } else {
            screenRect1.left = 0;
            screenRect1.right = windowRect.right;
            screenRect1.top = 0;
            screenRect1.bottom = hinge.top;
            screenRect2.left = 0;
            screenRect2.right = windowRect.right;
            screenRect2.top = hinge.bottom;
            screenRect2.bottom = windowRect.bottom;
        }
    }

    public void getScreenRects(Rect screenRect1, Rect screenRect2, int rotation) {
        Rect hinge = getHinge(rotation);
        Rect windowRect = getWindowRect();
        getScreenRects(windowRect, hinge, screenRect1, screenRect2);
    }

    public boolean isDualMode() {
        int rotation = getRotation();
        Rect hinge = getHinge(rotation);
        Rect windowRect = getWindowRect();

        if (windowRect.width() > 0 && windowRect.height() > 0) {
            // The windowRect doesn't intersect hinge
            return hinge.intersect(windowRect);
        }

        return false;
    }

    public static int getRotation(Activity activity) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int rotation = 0;
        if (wm != null) {
            rotation = wm.getDefaultDisplay().getRotation();
        }
        return rotation;
    }
}
