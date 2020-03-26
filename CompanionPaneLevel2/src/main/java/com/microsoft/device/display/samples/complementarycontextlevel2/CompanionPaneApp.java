/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.complementarycontextlevel2;

import android.app.Application;

import com.microsoft.device.dualscreen.layout.SurfaceDuoScreenManager;


public class CompanionPaneApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SurfaceDuoScreenManager.init(this);
    }
}
