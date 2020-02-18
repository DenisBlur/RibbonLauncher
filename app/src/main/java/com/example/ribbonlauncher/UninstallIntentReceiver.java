package com.example.ribbonlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UninstallIntentReceiver extends BroadcastReceiver {

    private static final String APP_TAG = "TEST_APP";
    private static final String ACTION_UNINSTALL_SHORTCUT =
            "com.android.launcher.action.PACKAGE_ADDED";

    @Override
    public void onReceive(Context context, Intent data) {


        Toast.makeText(context, data.getAction(), Toast.LENGTH_SHORT).show();

//
//        if (!ACTION_UNINSTALL_SHORTCUT.equals(data.getAction())) {
//            Log.e(APP_TAG, "FFFFFFFFFFF");
//            return;
//        }
//
//        Intent intent = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
//        String name = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
//        boolean duplicate = data.getBooleanExtra(MainActivity.EXTRA_SHORTCUT_DUPLICATE, true);
//
//        if (intent != null && name != null) {
//
//        }

//        try {
//            String action = intent.getAction();
//
//            if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
//                Log.e(APP_TAG, "Added");
//            } else if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
//                Log.e(APP_TAG, "REMOVED");
//            }
//
//            Log.i(APP_TAG, "Data String: "+ intent.getDataString());
//            Log.i(APP_TAG, "Package: "+ intent.getPackage());
//            Log.i(APP_TAG, "Scheme: "+ intent.getScheme());
//            Log.i(APP_TAG, "Type: "+ intent.getType());
//
//        } catch (Throwable cause) {
//            Log.e(APP_TAG, Objects.requireNonNull(cause.getMessage()));
//        }
    }
}
