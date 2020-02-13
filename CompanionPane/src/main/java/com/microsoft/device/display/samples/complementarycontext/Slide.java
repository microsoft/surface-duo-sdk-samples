/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.complementarycontext;

import java.util.ArrayList;

public class Slide {
    private final String title;
    private final String content;

    private Slide(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public final String getTitle() {
        return title;
    }

    public final String getContent() {
        return content;
    }

    static ArrayList<Slide> getSlides() {
        String SLIDE = "Slide ";
        String CONTENT = "Slide Content ";
        ArrayList<Slide> slides = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            slides.add(new Slide(SLIDE + (i + 1), CONTENT + (i + 1)));
        }
        return slides;
    }
}
