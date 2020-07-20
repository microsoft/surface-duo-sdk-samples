/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.multipleinstances;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addShortcuts();
    }

    private void addShortcuts() {
        Intent intent = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Intent intent2 = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, SecondActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id1")
                .setShortLabel("New Instance")
                .setLongLabel("New Instance")
                .setIntent(intent)
                .build();

        ShortcutInfo shortcut2 = new ShortcutInfo.Builder(this, "id2")
                .setShortLabel("Second Activity")
                .setLongLabel("Second Activity")
                .setIntent(intent2)
                .build();

        if (shortcutManager != null) {
            shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut, shortcut2));
        }
    }
}
