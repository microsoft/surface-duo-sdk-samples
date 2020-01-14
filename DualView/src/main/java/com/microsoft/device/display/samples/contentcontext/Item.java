/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.contentcontext;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Item implements Parcelable {
    public static final String KEY = "item";
    private String title;
    private PointF location;

    public Item(String title, PointF l) {
        this.title = title;
        this.location = l;
    }

    public String getBody() {
        return title;
    }

    public PointF getLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeFloat(location.x);
        dest.writeFloat(location.y);
    }

    @Override
    public String toString() {
        return getBody();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            String title = source.readString();
            float x = source.readFloat();
            float y = source.readFloat();
            PointF location = new PointF(x, y);
            return new Item(title, location);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    // Init items for ListView
    public static ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("New York", new PointF(40.7128f, -74.0060f)));
        items.add(new Item("Seattle", new PointF(47.6062f, -122.3425f)));
        items.add(new Item("Palo Alto", new PointF(37.444184f, -122.161059f)));
        items.add(new Item("San Francisco", new PointF(37.7542f, -122.4471f)));
        return items;
    }
}
