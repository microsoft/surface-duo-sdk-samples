/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.complementarycontext;

import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
public class Slide {
    private String title;
    private String content;

    @SuppressWarnings("WeakerAccess")
    public Slide(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public final String getTitle() {
        return title;
    }

    public final String getContent() {
        return content;
    }

    public static ArrayList<Slide> getSildes() {
        String SLIDE = "Slide ";
        String CONTENT = "Slide Content ";
        ArrayList<Slide> slides = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            slides.add(new Slide(SLIDE + (i + 1), CONTENT + (i + 1)));
        }
        return slides;
    }
}
