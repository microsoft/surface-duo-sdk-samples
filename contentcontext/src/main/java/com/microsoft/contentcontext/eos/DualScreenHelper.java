package com.microsoft.contentcontext.eos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.microsoft.device.display.DisplayMask;

import java.util.ArrayList;
import java.util.List;

public class DualScreenHelper {

    private final static String TAG = "DualScreenHelper";
    private DisplayMask mDisplayMask;
    private Activity mActivity;
    private List<DualScreenDetectionListener> mListeners;
    private int mMode = 0;
    private static final int SINGLE_SCREEN_MODE = 1;
    private static final int DUAL_SCREEN_MODE = 2;

    public DualScreenHelper() {
        mListeners = new ArrayList<>();
    }

    public boolean initialize(Activity activity, final View root) {
        try {
            mActivity = activity;
            mDisplayMask = DisplayMask.fromResourcesRectApproximation(mActivity);
            if (mDisplayMask == null) {
                return false;
            }
            root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    changeLayout();
                }
            });
        } catch (NoSuchMethodError ex) {
            ex.printStackTrace();
            return false;
        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
            return false;
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public void addListener(DualScreenDetectionListener listener) {
        switch (mMode) {
            case SINGLE_SCREEN_MODE:
                listener.useSingleScreenMode(getRotation());
                break;
            case DUAL_SCREEN_MODE:
                int rotation = getRotation();
                Rect hinge = getHinge(rotation);
                Rect drawingRect = getDrawingRect();

                Rect screenRect1 = new Rect();
                Rect screenRect2 = new Rect();
                getScreenRects(drawingRect, hinge, screenRect1, screenRect2);
                listener.useDualScreenMode(rotation, screenRect1, screenRect2);
                break;
        }
        mListeners.add(listener);
    }

    private int getRotation() {
        WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        int rotation = wm.getDefaultDisplay().getRotation();
        Log.d(TAG, "rotation: " + rotation);
        return rotation;
    }

    private Rect getHinge(int rotation) {
        // TODO: use getBoundingRects instead when it works fine
        List<Rect> boundings = mDisplayMask.getBoundingRectsForRotation(rotation);
        Rect hinge = boundings.get(0);
        for (Rect r:boundings) {
            Log.d(TAG, "hinge: " + r);
        }
        return hinge;
    }

    private Rect getDrawingRect() {
        View rootView = mActivity.getWindow().getDecorView().getRootView();
        Rect drawingRect = new Rect();
        rootView.getDrawingRect(drawingRect);
        Log.d(TAG, "drawingRect: " + drawingRect);
        return drawingRect;
    }

    private void getScreenRects(Rect drawingRect, Rect hinge, Rect screenRect1, Rect screenRect2) {
        if (hinge.left > 0) {
            screenRect1.left = 0;
            screenRect1.right = hinge.left;
            screenRect1.top = 0;
            screenRect1.bottom = drawingRect.bottom;
            screenRect2.left = hinge.right;
            screenRect2.right = drawingRect.right;
            screenRect2.top = 0;
            screenRect2.bottom = drawingRect.bottom;
        } else {
            screenRect1.left = 0;
            screenRect1.right = drawingRect.right;
            screenRect1.top = 0;
            screenRect1.bottom = hinge.top;
            screenRect2.left = 0;
            screenRect2.right = drawingRect.right;
            screenRect2.top = hinge.bottom;
            screenRect2.bottom = drawingRect.bottom;
        }
    }

    private void changeLayout() {
        int rotation = getRotation();
        Rect hinge = getHinge(rotation);
        Rect drawingRect = getDrawingRect();

        if (drawingRect.width() > 0 && drawingRect.height() > 0) {
            if (!hinge.intersect(drawingRect)) {
                if (mMode != SINGLE_SCREEN_MODE) {
                    Log.d(TAG, "changeLayout to single");
                    for (DualScreenDetectionListener listener : mListeners) {
                        listener.useSingleScreenMode(rotation);
                    }
                    Log.d(TAG, "not intersect: " + drawingRect);
                    mMode = SINGLE_SCREEN_MODE;
                }
            } else {
                if (mMode != DUAL_SCREEN_MODE) {
                    Log.d(TAG, "changeLayout to dual");
                    Rect screenRect1 = new Rect();
                    Rect screenRect2 = new Rect();
                    getScreenRects(drawingRect, hinge, screenRect1, screenRect2);
                    for (DualScreenDetectionListener listener : mListeners) {
                        listener.useDualScreenMode(rotation, screenRect1, screenRect2);
                    }
                    mMode = DUAL_SCREEN_MODE;
                }
            }
        }
    }

}