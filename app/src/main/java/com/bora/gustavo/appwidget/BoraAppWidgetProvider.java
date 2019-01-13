package com.bora.gustavo.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.bora.gustavo.R;
import com.bora.gustavo.activities.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BoraAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "BoraAppWidgetProvider";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("gyms");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "Gyms retrieved from database: " + dataSnapshot.getChildrenCount());
                long gymCount = dataSnapshot.getChildrenCount();
                Log.d(TAG, "Total number of registered gyms: " + gymCount);
                updateGymCount(gymCount);
            }

            private void updateGymCount(long gymCount) {
                Intent intent;
                PendingIntent pendingIntent;
                RemoteViews views;
                for (int appWidgetId : appWidgetIds) {
                    intent = new Intent(context, MainActivity.class);
                    pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                    views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider_layout);
                    views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
                    if (gymCount >= 0) {
                        views.setTextViewText(R.id.appwidget_text, context.getString(R.string.appwidget_text, gymCount));
                    } else {
                        views.setTextViewText(R.id.appwidget_text, context.getString(R.string.appwidget_text_empty));
                    }
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "The read failed: " + databaseError.getCode());
                updateGymCount(-1);
            }
        });
    }
}
