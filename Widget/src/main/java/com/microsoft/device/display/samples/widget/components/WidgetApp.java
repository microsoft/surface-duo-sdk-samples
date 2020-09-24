/*
 *
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License.
 *
 */

package com.microsoft.device.display.samples.widget.components;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.microsoft.device.display.samples.widget.R;
import com.microsoft.device.display.samples.widget.service.WidgetService;
import com.microsoft.device.display.samples.widget.settings.SettingsActivity;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetApp extends AppWidgetProvider {

    private static final String ACTION_UPDATE =
            "com.microsoft.device.display.samples.widget.action.ACTION_UPDATE";

    private static final String ACTION_SETTINGS =
            "com.microsoft.device.display.samples.widget.action.ACTION_SETTINGS";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            // Adding Remote Views adapter for every widget instance
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.collection_widget
            );
            Intent intent = new Intent(context, WidgetService.class);
            views.setRemoteAdapter(R.id.widgetListView, intent);

            // Adding pendingIntent for widget list View
            Intent clickIntent = new Intent(context, WidgetApp.class);
            clickIntent.setAction(WidgetFactory.ACTION_INTENT_VIEW_TAG);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widgetListView, clickPendingIntent);

            // Adding update button logic for every widget instance
            views.setOnClickPendingIntent(R.id.widgetUpdateButton,
                    getPendingSelfIntent(context, ACTION_UPDATE));

            // Adding settings button logic for every widget instance
            views.setOnClickPendingIntent(R.id.widgetSettingsButton,
                    getPendingSelfIntent(context, ACTION_SETTINGS));

            // Update all widgets instances
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // Handling intent for list item click
        if (WidgetFactory.ACTION_INTENT_VIEW_TAG.equals(intent.getAction())) {
            String url = intent.getStringExtra(WidgetFactory.ACTION_INTENT_VIEW_HREF_TAG);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            webIntent.addFlags(
                    Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT
                  | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(webIntent);
        }

        // Handing intent for list items update
        if (ACTION_UPDATE.equals(intent.getAction())) {
            onUpdate(context);
        }

        // Handling intent for settings screen launch
        if (ACTION_SETTINGS.equals(intent.getAction())) {
            Intent intentSettings = new Intent(context, SettingsActivity.class);
            intentSettings.addFlags(
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                  | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentSettings);
        }

        super.onReceive(context, intent);
    }

    // Private method used to call the generic update method from above
    private void onUpdate(Context context) {
        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        final ComponentName cn = new ComponentName(context, WidgetApp.class);
        mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widgetListView);
        Toast.makeText(context, "Your widget will be updated soon! ", Toast.LENGTH_SHORT)
                .show();
    }

    // An explicit intent directed at the current class (the "self").
    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}