/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */
package com.microsoft.device.display.samples.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.Surface;
import android.view.View;

import androidx.core.util.Consumer;
import androidx.window.DeviceState;
import androidx.window.WindowLayoutInfo;
import androidx.window.WindowManager;

import java.util.concurrent.Executor;

public class PortraitLockHelper {

    public interface PortraitStateListener {
        void PortraitStateChanged(int state);
    }

    //region  public constants
    public static final int STATE_NOT_SUPPORTED = 0x00;
    public static final int STATE_PORTRAIT_LOCKED = 0x01;
    public static final int STATE_ROTATED_90 = 0x02;
    public static final int STATE_ROTATED_270 = 0x04;
    public static final int STATE_SPANNED = 0x08;
    public static final int STATE_FLIPPED = 0x10;
    public static final int STATE_LETTERBOXED = 0x20;
    public static final int STATE_UNLOCKED = 0x40;


    //endregion


    int currentState = STATE_NOT_SUPPORTED;

    public PortraitStateListener StateListener;

    final Activity activity;
    final View rootView;
    final WindowManager wm;
    final DisplayManager displayManager;
    final DisplayManager.DisplayListener displayListener;

    //region Window Manager
    final Handler handler = new Handler(Looper.getMainLooper());
    final Executor mainThreadExecutor = new Executor() {
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };
    final Consumer<WindowLayoutInfo> layoutInfoChangeConsumer = new Consumer<WindowLayoutInfo>() {
        @Override
        public void accept(WindowLayoutInfo windowLayoutInfo) {
            if (windowLayoutInfo.getDisplayFeatures().size() > 0){
                isAppSpanned = true;
                hinge = windowLayoutInfo.getDisplayFeatures().get(0).getBounds();
            } else {
                isAppSpanned = false;
                hinge = null;
            }

            updateState();
        }
    };
    //endregion

    //region state trackers
    Boolean isInFlippedMode = false;
    Boolean isInDevicePortraitOrientation = false;
    Boolean isInDeviceReversePortraitOrientation = false;
    Boolean isAppSpanned = false;
    public Rect hinge;
    //endregion

    public PortraitLockHelper(final Activity activityInput) {

        activity = activityInput;
        rootView = activity.getWindow().getDecorView().getRootView();

        //Initialize Window Manager
        wm = new WindowManager(activity, null);
        wm.registerDeviceStateChangeCallback(mainThreadExecutor, new Consumer<DeviceState>() {
            @Override
            public void accept(DeviceState deviceState) {
                isInFlippedMode = deviceState.getPosture() == DeviceState.POSTURE_FLIPPED;
                updateState();
            }
        });

        // Get display changes to detect device rotation
        displayManager = (DisplayManager) activity.getSystemService(Context.DISPLAY_SERVICE);
        displayListener = new DisplayManager.DisplayListener() {
            @Override
            public void onDisplayAdded(int displayId) {
            }

            @Override
            public void onDisplayRemoved(int displayId) {
            }

            @Override
            public void onDisplayChanged(int displayId) {
                if (displayId == rootView.getDisplay().getDisplayId()) {
                    updateState();
                }
            }
        };

        // Connect to the attach state listener to detect being spanned
        if (rootView != null) {
            rootView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    wm.registerLayoutChangeCallback(mainThreadExecutor, layoutInfoChangeConsumer);
                    displayManager.registerDisplayListener(displayListener, handler);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    wm.unregisterLayoutChangeCallback(layoutInfoChangeConsumer);
                    displayManager.unregisterDisplayListener(displayListener);
                }
            });
        }

    }

    void updateState() {
        Display display = rootView.getDisplay();
        if (display == null) {
            return;
        }

        int rotation = rootView.getDisplay().getRotation();

        isInDevicePortraitOrientation = false;
        isInDeviceReversePortraitOrientation = false;
        if (rotation == Surface.ROTATION_270) {
            isInDevicePortraitOrientation = true;
        } else if (rotation == Surface.ROTATION_90) {
            isInDeviceReversePortraitOrientation = true;
        }
        int state = getPortraitState();
        if (state != currentState) {
            currentState = state;
            if (StateListener != null) {
                StateListener.PortraitStateChanged(currentState);
            }

        }
    }

    public int getPortraitState() {

        int state = 0;
        int requestedOrientation = activity.getRequestedOrientation();

        switch (requestedOrientation) {
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
            case ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT:
            case ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT:

                state |= STATE_PORTRAIT_LOCKED;
                break;

            case ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED:
            case ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR:
            case ActivityInfo.SCREEN_ORIENTATION_FULL_USER:
            case ActivityInfo.SCREEN_ORIENTATION_USER:
            case ActivityInfo.SCREEN_ORIENTATION_SENSOR:

                state |= STATE_UNLOCKED;
                break;
            case ActivityInfo.SCREEN_ORIENTATION_BEHIND:
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
            case ActivityInfo.SCREEN_ORIENTATION_LOCKED:
            case ActivityInfo.SCREEN_ORIENTATION_NOSENSOR:
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
            case ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE:
            case ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE:
                break;
        }

        if (isAppSpanned) {
            state |= STATE_SPANNED;
        }
        if (isInFlippedMode) {
            state |= STATE_FLIPPED;
        }
        if (isInDevicePortraitOrientation) {
            state |= STATE_ROTATED_90;
            if ((state & STATE_PORTRAIT_LOCKED) > 0) {
                state |= STATE_LETTERBOXED;
            }
        }
        if (isInDeviceReversePortraitOrientation) {
            state |= STATE_ROTATED_270;
            if ((state & STATE_PORTRAIT_LOCKED) > 0) {
                state |= STATE_LETTERBOXED;
            }
        }

        return state;
    }
}
