/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.complementarycontext;

import android.os.Bundle;
import android.view.Surface;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.microsoft.device.display.samples.complementarycontext.adapters.NotesAdapter;
import com.microsoft.device.display.samples.complementarycontext.adapters.SlidesAdapter;
import com.microsoft.device.display.samples.complementarycontext.model.DataProvider;
import com.microsoft.device.dualscreen.layout.ScreenHelper;
import com.microsoft.device.dualscreen.layout.ScreenModeListener;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CompanionPaneApp) getApplication()).getSurfaceDuoScreenManager()
            .addScreenModeListener(new ScreenModeListener() {
                private void setupViewPager(ViewPager2 viewPager) {
                    SlidesAdapter slidesAdapter= new SlidesAdapter();
                    slidesAdapter.submitList(DataProvider.getSlides());
                    viewPager.setAdapter(slidesAdapter);
                }

                @Override
                public void onSwitchToSingleScreenMode() {
                    final ViewPager2 slidesPager= findViewById(R.id.slides_pager);
                    setupViewPager(slidesPager);
                }

                @Override
                public void onSwitchToDualScreenMode() {
                    final ViewPager2 slidesPager= findViewById(R.id.slides_pager);
                    setupViewPager(slidesPager);

                    // Handle DualScreenEndLayout Toolbar visibility
                    Toolbar toolbar = findViewById(R.id.dual_screen_end_toolbar);
                    switch (ScreenHelper.getCurrentRotation(MainActivity.this)) {
                        case Surface.ROTATION_0:
                        case Surface.ROTATION_180:
                            toolbar.setVisibility(View.VISIBLE);
                            break;
                        case Surface.ROTATION_90:
                        case Surface.ROTATION_270:
                            toolbar.setVisibility(View.GONE);
                            break;
                    }

                    final RecyclerView notesRecyclerView = findViewById(R.id.notes_recycler_view);
                    notesRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    final NotesAdapter notesAdapter = new NotesAdapter();
                    notesAdapter.submitList(DataProvider.getSlides());
                    notesRecyclerView.setAdapter(notesAdapter);
                    notesAdapter.setSlidesPager(slidesPager);

                    slidesPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                        }

                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            notesRecyclerView.scrollToPosition(position);

                            NotesAdapter.oldSelectionPosition = NotesAdapter.selectionPosition;
                            NotesAdapter.selectionPosition = position;
                            notesAdapter.notifyItemChanged(NotesAdapter.oldSelectionPosition);
                            notesAdapter.notifyItemChanged(position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                            super.onPageScrollStateChanged(state);
                        }
                    });
                }
            });
    }
}
