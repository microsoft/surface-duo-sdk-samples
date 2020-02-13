/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.intentsecondscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        Button secondActivityButton = findViewById(R.id.second_activity_button);
        secondActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentSecondActivity();
            }
        });
        final Button linkButton = findViewById(R.id.link_button);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentBrowserApp(getResources().getString(R.string.web_link));
            }
        });

        final Button mapButton = findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentBrowserApp(getResources().getString(R.string.map_link));
            }
        });
    }

    private void startIntentSecondActivity() {
        Intent intent = new Intent(this, SecondActivity.class);
        // Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT is required to launch a second activity
        // on a second display while still keeping the first activity on the first display
        // (not pausing/stopping it)
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void startIntentBrowserApp(String urlString) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
