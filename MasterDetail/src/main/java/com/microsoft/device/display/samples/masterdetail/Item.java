/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.masterdetail;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
public class Item implements Serializable {
    private static final long serialVersionUID = 8383901821872620925L;
    private String title;
    private String body;

    @SuppressWarnings("WeakerAccess")
    public Item(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    //Init items for ListView
    public static ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", "This is the first item"));
        items.add(new Item("Item 2", "This is the second item"));
        items.add(new Item("Item 3", "This is the third item"));
        return items;
    }

    @Override
    @NonNull
    public String toString() {
        return getTitle();
    }

}
