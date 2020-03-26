/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.complementarycontext;

import android.app.Application;

import com.microsoft.device.dualscreen.layout.SurfaceDuoScreenManager;


public class CompanionPaneApp extends Application {
    private SurfaceDuoScreenManager surfaceDuoScreenManager;

    @Override
    public void onCreate() {
        super.onCreate();
        surfaceDuoScreenManager = SurfaceDuoScreenManager.init(this);
    }

    public SurfaceDuoScreenManager getSurfaceDuoScreenManager() {
        return surfaceDuoScreenManager;
    }
}
