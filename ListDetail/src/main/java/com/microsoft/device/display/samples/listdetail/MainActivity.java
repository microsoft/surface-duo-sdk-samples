/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.listdetail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.device.display.samples.listdetail.model.DataProvider;
import com.microsoft.device.dualscreen.layout.ScreenHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!ScreenHelper.isDualMode(this)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(
                            R.id.single_screen_container_id,
                            new ItemsListFragment(),
                            null
                    )
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.dual_screen_start_container_id,
                            new ItemsListFragment(),
                            null
                    ).commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.dual_screen_end_container_id,
                            ItemDetailFragment.newInstance(DataProvider.getMovieMocks().get(0)),
                            null
                    ).commit();
        }
    }
}