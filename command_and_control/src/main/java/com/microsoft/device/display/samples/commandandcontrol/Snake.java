/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.microsoft.device.display.samples.commandandcontrol;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.device.display.samples.utils.ScreenHelper;

/**
 * Snake: a simple game that everyone can enjoy.
 *
 * This is an implementation of the classic Game "Snake", in which you control a serpent roaming
 * around the garden looking for apples. Be careful, though, because when you catch one, not only
 * will you become longer, but you'll move faster. Running into yourself or the walls will end the
 * game.
 *
 */
public class Snake extends AppCompatActivity implements View.OnClickListener, OnTouchListener {

    /**
     * Constants for desired direction of moving the snake
     */
    private static final String TAG = Snake.class.getSimpleName();
    public static int MOVE_LEFT = 0;
    public static int MOVE_UP = 1;
    public static int MOVE_DOWN = 2;
    public static int MOVE_RIGHT = 3;

    private static String ICICLE_KEY = "snake-view";

    private SnakeView mSnakeView;
    private ScreenHelper screenHelper;
    private boolean isDuo;
    /**
     * Called when Activity is first created. Turns off the title bar, sets up the content views,
     * and fires up the SnakeView.
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screenHelper = new ScreenHelper();
        isDuo = screenHelper.initialize(this);
        setupLayout(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause the game along with the activity
        if(mSnakeView != null) {
            if (mSnakeView.getGameState() == SnakeView.RUNNING) {
                mSnakeView.setMode(SnakeView.PAUSE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Store the game state
        /*if(mSnakeView != null) {
            outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
        }*/
    }

    /**
     * Handles key events in the game. Update the direction our snake is traveling based on the
     * DPAD.
     *
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                mSnakeView.moveSnake(MOVE_UP);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mSnakeView.moveSnake(MOVE_RIGHT);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                mSnakeView.moveSnake(MOVE_DOWN);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mSnakeView.moveSnake(MOVE_LEFT);
                break;
        }

        return super.onKeyDown(keyCode, msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageLeft:
                mSnakeView.moveSnake(MOVE_LEFT);
                break;
            case R.id.imageRight:
                mSnakeView.moveSnake(MOVE_RIGHT);
                break;
            case R.id.imageUp:
                mSnakeView.moveSnake(MOVE_UP);
                break;
            case R.id.imageDown:
                mSnakeView.moveSnake(MOVE_DOWN);
                break;
        }
    }

    private void setupLayout(Bundle savedInstanceState) {
        boolean shouldAdjustTextPosition = false;
        int rotation = ScreenHelper.getRotation(this);
        if (isDuo) {
            if (screenHelper.isDualMode()) {
                if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
                    setContentView(R.layout.snake_layout_single);
                    shouldAdjustTextPosition = true;
                } else {
                    setContentView(R.layout.snake_layout_double_landscape);
                }
            } else {
                setContentView(R.layout.snake_layout_single);
            }
        } else {
            setContentView(R.layout.snake_layout_single);
        }

        mSnakeView = (SnakeView) findViewById(R.id.snake);
        mSnakeView.setDependentViews((TextView) findViewById(R.id.text),
                findViewById(R.id.arrowContainer), findViewById(R.id.background));
        mSnakeView.setOnTouchListener(this);

        if(shouldAdjustTextPosition) {
            setTextViewPositionInDualPortrait(rotation);
        }

        findViewById(R.id.imageLeft).setOnClickListener(this);
        findViewById(R.id.imageRight).setOnClickListener(this);
        findViewById(R.id.imageUp).setOnClickListener(this);
        findViewById(R.id.imageDown).setOnClickListener(this);

        if (savedInstanceState == null) {
            // We were just launched -- set up a new game
            mSnakeView.setMode(SnakeView.READY);
        } else {
            // We are being restored
            Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
            if (map != null) {
                mSnakeView.restoreState(map);
            } else {
                if(mSnakeView.getGameState() == SnakeView.READY) {
                    mSnakeView.setMode(SnakeView.READY);
                } else {
                    mSnakeView.setMode(SnakeView.PAUSE);
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mSnakeView.getGameState() != SnakeView.RUNNING) {
            // If the game is not running then on touching any part of the screen
            // we start the game by sending MOVE_UP signal to SnakeView
            mSnakeView.moveSnake(MOVE_UP);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // TextView should be placed on left pane when app is in dual portrait
    private void setTextViewPositionInDualPortrait(int rotation) {
        TextView textView = findViewById(R.id.text);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        Rect leftPane = new Rect();
        Rect rightPane = new Rect();
        screenHelper.getScreenRects(leftPane, rightPane, rotation);
        layoutParams.width = leftPane.width() - layoutParams.getMarginStart() - layoutParams.getMarginEnd();
        textView.setLayoutParams(layoutParams);
    }
}
