/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.masterdetail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.device.display.samples.masterdetail.model.DataProvider;
import com.microsoft.device.dualscreen.layout.ScreenModeListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MasterDetailApp) getApplication()).getSurfaceDuoScreenManager().addScreenModeListener(
            new ScreenModeListener() {
                @Override
                public void onSwitchToSingleScreenMode() {
                    getSupportFragmentManager().beginTransaction()
                            .replace(
                                R.id.single_screen_container_id,
                                new ItemsListFragment(),
                                null
                            )
                            .commit();
                }

                @Override
                public void onSwitchToDualScreenMode() {
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
        );
    }
}