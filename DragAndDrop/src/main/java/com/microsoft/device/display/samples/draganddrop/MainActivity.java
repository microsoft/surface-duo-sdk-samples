/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.draganddrop;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.microsoft.device.display.samples.draganddrop.fragment.DragSourceFragment;
import com.microsoft.device.display.samples.draganddrop.fragment.DropTargetFragment;
import com.microsoft.device.dualscreen.layout.ScreenModeListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.reset_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.recreate();
            }
        });

        ((DragAndDropApp) getApplication()).getSurfaceDuoScreenManager().addScreenModeListener(
                new ScreenModeListener() {
                    @Override
                    public void onSwitchToSingleScreenMode() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(
                                    R.id.drag_source_container,
                                    DragSourceFragment.newInstance()
                                ).commit();
                        getSupportFragmentManager().beginTransaction()
                                .replace(
                                    R.id.drop_target_container,
                                    DropTargetFragment.newInstance()
                                ).commit();
                    }

                    @Override
                    public void onSwitchToDualScreenMode() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(
                                    R.id.dual_screen_start_container_id,
                                    DragSourceFragment.newInstance()
                                ).commit();
                        getSupportFragmentManager().beginTransaction()
                                .replace(
                                    R.id.dual_screen_end_container_id,
                                    DropTargetFragment.newInstance()
                                ).commit();
                    }
        });
    }
}
