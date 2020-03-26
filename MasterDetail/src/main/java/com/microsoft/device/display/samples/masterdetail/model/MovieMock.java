/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.masterdetail.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class MovieMock implements Serializable {
    public static final String KEY = "MovieMock";
    private final String title;
    private final String body;

    MovieMock(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    @NonNull
    public String toString() {
        return getTitle();
    }

}
