/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.complementarycontext.model;

import androidx.annotation.Nullable;

public class Slide {
    private final String content;
    private final String note;

    Slide(String content, String note) {
        this.note = note;
        this.content = content;
    }

    public final String getNote() {
        return note;
    }

    public final String getContent() {
        return content;
    }

    public boolean equals(@Nullable Slide obj) {
        if (obj != null) {
            return this.note.equals(obj.note) && this.content.equals(obj.content);
        } else {
            return false;
        }
    }
}
