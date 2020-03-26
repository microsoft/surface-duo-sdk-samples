/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.masterdetail.model;

import java.util.ArrayList;

public class DataProvider {
    //Init items for ListView
    public static ArrayList<MovieMock> getMovieMocks() {
        ArrayList<MovieMock> items = new ArrayList<>();
        items.add(new MovieMock("Item 1", "This is the first item"));
        items.add(new MovieMock("Item 2", "This is the second item"));
        items.add(new MovieMock("Item 3", "This is the third item"));
        return items;
    }
}
