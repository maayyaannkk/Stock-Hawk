package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by maayy on 08-06-2016.
 */
public class HistoricalIntentService extends IntentService {

    public HistoricalIntentService() {
        super(HistoricalIntentService.class.getName());
    }

    public HistoricalIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(HistoricalIntentService.class.getSimpleName(), "Historical Intent Service");
        HistoricalStockService histTaskService = new HistoricalStockService(this);
        Bundle args = new Bundle();
        if (intent.getStringExtra("tag").equals("add")) {
            args.putString("symbol", intent.getStringExtra("symbol"));
        }
        // We can call OnRunTask from the intent service to force it to run immediately instead of
        // scheduling a task.
        int result = histTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
        if (result == GcmNetworkManager.RESULT_FAILURE && intent.getStringExtra("tag").equals("add")) {
            Log.e(HistoricalIntentService.class.getSimpleName(),"Task Failed");
        }
    }
}
