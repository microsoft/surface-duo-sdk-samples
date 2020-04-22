/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package om.microsoft.device.display.samples;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "PenEventsDebug";
    TextView txt, txtAction, txtPreasure, txtOrientation, buttonState, txtCoords, txtTilt;
    TextureView texture;
    boolean canDraw = false;
    Paint paint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.txtText);
        txtAction = findViewById(R.id.txtAction);
        txtPreasure = findViewById(R.id.txtPreasure);
        txtOrientation = findViewById(R.id.txtOrientation);
        buttonState = findViewById(R.id.txtButton);
        txtCoords = findViewById(R.id.txtCoords);
        texture = findViewById(R.id.txtView);
        txtTilt = findViewById(R.id.txtTilt);

        paint = new Paint();

        texture.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                canDraw = true;
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                canDraw = false;
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        });
    }


    @SuppressLint("DefaultLocale")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        String logString;
        boolean handled;

        if (event.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS)
            logString = "Pen ";
        else if (event.getToolType(0) == MotionEvent.TOOL_TYPE_ERASER)
            logString = "Eraser ";
        else if (event.getToolType(0) == MotionEvent.TOOL_TYPE_FINGER)
            logString = "Finger ";
        else if (event.getToolType(0) == MotionEvent.TOOL_TYPE_MOUSE)
            logString = "Mouse ";
        else
            logString = "UNKNOWN device " + event.getToolType(0);

        txt.setText(logString);

        txtPreasure.setText(format("%s%s", getString(R.string.Pressure), event.getPressure()));
        txtOrientation.setText(format("%s%s", getString(R.string.Orientation), event.getOrientation()));
        buttonState.setText(format("%s%d", getString(R.string.ButtonState), event.getButtonState()));
        txtCoords.setText(format("%s%s, %s", getString(R.string.Location), event.getRawX(), event.getRawY()));
        txtTilt.setText(format("%s%s", getString(R.string.Tilt), event.getAxisValue(MotionEvent.AXIS_TILT)));
        int action = event.getActionMasked();

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                txtAction.setText(R.string.ActionDown);
                logString += " Action was DOWN ";
                handled = true;
                break;
            case (MotionEvent.ACTION_MOVE):
                txtAction.setText(R.string.ActionMove);
                logString += " Action was MOVE ";
                handled = true;
                break;
            case (MotionEvent.ACTION_UP):
                txtAction.setText(R.string.ActionUp);
                logString += " Action was UP ";
                handled = true;
                break;
            case (MotionEvent.ACTION_CANCEL):
                txtAction.setText(R.string.ActionCancel);
                logString += " Action was CANCEL ";
                handled = true;
                break;
            case (MotionEvent.ACTION_HOVER_ENTER):
                txtAction.setText(R.string.ActionHoverEnter);
                logString += " Action was HOVER_ENTER";
                handled = true;
                break;
            case (MotionEvent.ACTION_HOVER_EXIT):
                txtAction.setText(R.string.ActionHoverExit);
                logString += " Action was HOVER_EXIT";
                handled = true;
                break;
            case (MotionEvent.ACTION_HOVER_MOVE):
                txtAction.setText(R.string.ActionHoverMove);
                logString += " Action was HOVER_MOVE";
                handled = true;
                break;
            case (MotionEvent.ACTION_BUTTON_PRESS):
                txtAction.setText(R.string.ActionButtonPress);
                logString += " Action was Button Press";
                handled = true;
                break;
            case (MotionEvent.ACTION_BUTTON_RELEASE):
                txtAction.setText(R.string.ActionButtonRelease);
                logString += " Action was BUTTON_RELEASE";
                handled = true;
                break;
            default:
                logString += "DEFAULT!!! " + action;
                handled = super.onTouchEvent(event);
        }

        Log.i(DEBUG_TAG, logString);
        drawEvent(event);
        return handled;
    }

    void drawEvent(MotionEvent event) {

        final String BOLD_PINK = "#ee00FF";
        final String MAGIC_BLUE = "#05a5eC";
        final String BOLD_RED = "#CD5C5C";
        final int GRASS_GREEN = 0xAA00bb55;
        final float RAD = 57.2958f;

        Canvas canvas = texture.lockCanvas();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            texture.unlockCanvasAndPost(canvas);
            return;
        }

        if (event.getToolType(0) == MotionEvent.TOOL_TYPE_ERASER) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(20);
        }

        // Use Color.parseColor to define HTML colors
        if (event.getButtonState() != 0) {
            paint.setColor(Color.parseColor(BOLD_PINK));
        } else {
            paint.setColor(Color.parseColor(MAGIC_BLUE));
        }
        canvas.drawCircle(texture.getWidth() / 4.0f, texture.getHeight() / 4.0f, texture.getHeight() / 4.0f * event.getPressure(), paint);
        paint.setColor(Color.parseColor(BOLD_RED));
        RectF oval = new RectF();
        oval.bottom = texture.getHeight() / 2.0f;
        oval.top = 0;
        oval.left = 0;
        oval.right = 2 * (texture.getWidth() / 4.0f);
        float orientation = ((event.getOrientation() * RAD) + 90) % 360;
        canvas.drawArc(oval, orientation, 5.0f, true, paint);
        paint.setColor(GRASS_GREEN);
        int[] txtLoc = new int[2];
        texture.getLocationOnScreen(txtLoc);
        
        canvas.drawCircle(event.getRawX()- txtLoc[0], event.getRawY() - txtLoc[1], 100 * event.getPressure(), paint);

        texture.unlockCanvasAndPost(canvas);

    }

}
