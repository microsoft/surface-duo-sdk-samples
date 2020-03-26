/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.contentcontext.model;

import com.microsoft.device.display.samples.contentcontext.R;

import java.util.ArrayList;

public class DataProvider {
    public static ArrayList<MapPoint> getMapPoints() {
        ArrayList<MapPoint> mapPoints = new ArrayList<>();
        mapPoints.add(new MapPoint("New York", R.drawable.new_york));
        mapPoints.add(new MapPoint("Seattle", R.drawable.seattle));
        mapPoints.add(new MapPoint("Palo Alto", R.drawable.palo_alto));
        mapPoints.add(new MapPoint("San Francisco", R.drawable.san_francisco));
        return mapPoints;
    }
}
