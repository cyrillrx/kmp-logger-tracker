package com.cyrillrx.notifier;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @author Cyril Leroux
 *         Created 01/11/2016.
 */
public class Alerts {

//    public static final String TAG = Alerts.class.getSimpleName();
//
//    public static void displayErrorActivity(Context context, String msg) {
//
//        try {
//            final Intent intent = new Intent(context, AlertActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(Intent.EXTRA_TEXT, msg);
//
//        } catch (Exception ignored) { }
//    }
//
//    public static void displayErrorDialog(Context context, int msgRes) {
//
//        try {
//            new AlertDialog.Builder(context)
//                    .setMessage(msgRes)
//                    .setCancelable(true)
//                    .setPositiveButton(R.string.dialog_ok, null)
//                    .create()
//                    .show();
//        } catch (Exception ignored) { }
//    }
//
//    public static void displayErrorDialog(Context context, String msg) {
//
//        try {
//            new AlertDialog.Builder(context)
//                    .setMessage(msg)
//                    .setCancelable(true)
//                    .setPositiveButton(R.string.dialog_ok, null)
//                    .create()
//                    .show();
//        } catch (Exception e) {
//            Logger.error(TAG, "displayErrorDialog", e);
//        }
//    }
//
//    public static class AlertActivity extends Activity {
//
//        public AlertActivity() { }
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_alert);
//
//            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////            if (DeviceUtils.isPhone(getResources())) {
////                NavigationUtils.setupToolbarArrow(this, toolbar);
////            } else {
////                setSupportActionBar(toolbar);
////            }
//
//            final TextView tvMessage = (TextView) findViewById(R.id.text);
//            tvMessage.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));
//
//            findViewById(R.id.btn_validate).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//
//        }
//    }
}