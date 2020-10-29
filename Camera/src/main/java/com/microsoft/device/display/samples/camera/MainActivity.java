/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */
package com.microsoft.device.display.samples.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Objects;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity {

    //region camera members
    private TextureView textureView;
    private String cameraId;
    private Size imageDimension;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    protected CameraDevice cameraDevice;
    protected CaptureRequest.Builder captureRequestBuilder;
    protected CameraCaptureSession cameraCaptureSessions;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    //endregion

    //region texture rotation
    float rotation = 0;
    float rotCorrX = 1.0f;
    float rotCorrY = 1.0f;
    //endregion

    //region UX views
    FloatingActionButton swapCamsButton;
    ToggleButton overlayToggle;
    ToggleButton feedRotationToggle;
    ToggleButton resizeableToggle;
    ToggleButton cameraToggle;
    View rotationMessageView;
    View spaceLeft;
    //endregion

    //region state handling
    PortraitLockHelper portraitHelper;
    boolean showRotationMessage = true;
    boolean rotateFeed = false;
    boolean resizeable = false;
    private int cameraOrientation = CameraMetadata.LENS_FACING_FRONT;

    final PortraitLockHelper.PortraitStateListener stateListener = new PortraitLockHelper.PortraitStateListener() {
        @Override
        public void PortraitStateChanged(int state) {
            rotation = 0;
            ViewGroup.LayoutParams layoutParams = spaceLeft.getLayoutParams();

            if ((state & PortraitLockHelper.STATE_ROTATED_90) > 0) {
                rotation = 90;
            } else if ((state & PortraitLockHelper.STATE_ROTATED_270) > 0) {
                rotation = 270;
            }

            if ((state & PortraitLockHelper.STATE_PORTRAIT_LOCKED) > 0) {
                if ((state & PortraitLockHelper.STATE_FLIPPED) > 0) {
                    rotation = 0;
                }
                if ((state & PortraitLockHelper.STATE_SPANNED) > 0 ||
                        (state & PortraitLockHelper.STATE_ROTATED_90) > 0 ||
                        (state & PortraitLockHelper.STATE_ROTATED_270) > 0) {
                    if (showRotationMessage) {
                        rotationMessageView.setVisibility(View.VISIBLE);
                    } else {
                        rotationMessageView.setVisibility(View.GONE);
                    }
                } else {
                    rotationMessageView.setVisibility(View.GONE);
                }
            } else if ((state & PortraitLockHelper.STATE_UNLOCKED) > 0) {
                //never show the message if unlocked
                rotationMessageView.setVisibility(View.GONE);
                if ((state & PortraitLockHelper.STATE_SPANNED) > 0 &&
                        (state & PortraitLockHelper.STATE_ROTATED_90) == 0 &&
                        (state & PortraitLockHelper.STATE_ROTATED_270) == 0) {

                        // move the preview UX to the right display
                        layoutParams.width=portraitHelper.hinge.right;
                }
            }

            if (!rotateFeed) {
                rotation = 0;
            }
            spaceLeft.setLayoutParams(layoutParams);
            transformTexture();
        }
    };
    //endregion

    //region activity lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        portraitHelper = new PortraitLockHelper(this);

        overlayToggle = findViewById(R.id.toggleButton);
        feedRotationToggle = findViewById(R.id.toggleButton2);
        resizeableToggle = findViewById(R.id.tbResize);
        cameraToggle = findViewById(R.id.tbCamera);
        swapCamsButton = findViewById(R.id.floatingActionButton3);
        spaceLeft = findViewById(R.id.spaceLeft);

        if (cameraToggle.isChecked()) {
            cameraOrientation = CameraMetadata.LENS_FACING_BACK;
        }

        rotationMessageView = findViewById(R.id.appCompatInfo);
        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);

        portraitHelper.StateListener = stateListener;


        swapCamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraToggle.setChecked(cameraOrientation != CameraMetadata.LENS_FACING_BACK);
            }
        });

        overlayToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showRotationMessage = isChecked;
                portraitHelper.StateListener.PortraitStateChanged(portraitHelper.getPortraitState());
            }
        });

        feedRotationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rotateFeed = isChecked;
                portraitHelper.StateListener.PortraitStateChanged(portraitHelper.getPortraitState());
            }
        });

        resizeableToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resizeable = isChecked;
                setOrientation();
                portraitHelper.StateListener.PortraitStateChanged(portraitHelper.getPortraitState());
            }
        });

        cameraToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cameraOrientation = CameraMetadata.LENS_FACING_BACK;

                } else {
                    cameraOrientation = CameraMetadata.LENS_FACING_FRONT;
                }
                restartCamera();
            }
        });

        portraitHelper.StateListener.PortraitStateChanged(portraitHelper.getPortraitState());
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            restartCamera();
        }
    }

    void setOrientation() {
        if (resizeable) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
    }

    //endregion

    //region Texture and Feed transformation

    void setScaleAndTransformation() {

        if (imageDimension == null)
            return;

        float feedAspectRatio = (float) imageDimension.getWidth() / (float) imageDimension.getHeight();
        int orientation  = getResources().getConfiguration().orientation;
        // if rotation is applied, correct the feed size
        if (rotation != 0 && rotation != 180) {
            if (resizeable) {
                // when resizeable and in portrait it means we are spanned and the Duo is 90/270 degrees rotated
                if (orientation == ORIENTATION_PORTRAIT){
                    rotCorrX = feedAspectRatio;
                    rotCorrY = feedAspectRatio;
                } else {
                    rotCorrX = 1.0f / feedAspectRatio;
                    rotCorrY = feedAspectRatio;
                }
            } else {
                rotCorrX = feedAspectRatio;
                rotCorrY = feedAspectRatio;
            }
        } else {
            rotCorrX = 1;
            rotCorrY = 1;
        }
    }

    private void transformTexture() {
        setScaleAndTransformation();

        textureView.setScaleX(rotCorrX);
        textureView.setScaleY(rotCorrY);
        textureView.setRotation(rotation);
    }


    final TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    //endregion

    //region Camera handling
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        boolean foundCamera = false;
        try {
            assert manager != null;
            String[] cameraIdList = manager.getCameraIdList();
            for (String id :
                    cameraIdList) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(id);

                Integer lens = characteristics.get(CameraCharacteristics.LENS_FACING);
                assert lens != null;
                if (lens == cameraOrientation) {
                    cameraId = id;
                    foundCamera = true;
                    break;
                }
            }
            if (!foundCamera) {
                cameraId = manager.getCameraIdList()[0];
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;

            imageDimension = map.getOutputSizes(SurfaceTexture.class)[cameraOrientation];

            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }

            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            closeCamera();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            closeCamera();
        }
    };

    protected void createCameraPreview() {
        try {
            transformTexture();

            SurfaceTexture texture = textureView.getSurfaceTexture();

            if (texture == null) {
                restartCamera();
                return;
            }
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    // handle the failed session config
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void updatePreview() {
        if (null == cameraDevice) {
            return;
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void restartCamera() {
        if (cameraDevice != null) {
            closeCamera();
        }
        stopBackgroundThread();
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    //endregion

    //region background thread
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

}