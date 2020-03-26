/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.contentcontext.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MapPoint implements Parcelable {
    public static final String KEY = "MapPoint";
    private final String title;
    private final int mapImageResourceID;

    MapPoint(String title, int mapImageResourceID) {
        this.title = title;
        this.mapImageResourceID = mapImageResourceID;
    }

    public String getTitle() {
        return title;
    }

    public int getMapImageResourceID() { return mapImageResourceID; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(mapImageResourceID);
    }

    @Override
    @NonNull
    public String toString() {
        return getTitle();
    }

    public static final Parcelable.Creator<MapPoint> CREATOR = new Parcelable.Creator<MapPoint>() {
        @Override
        public MapPoint createFromParcel(Parcel source) {
            String title = source.readString();
            int mapImageResourceID = source.readInt();
            return new MapPoint(title, mapImageResourceID);
        }

        @Override
        public MapPoint[] newArray(int size) {
            return new MapPoint[size];
        }
    };
}
