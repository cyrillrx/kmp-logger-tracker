package com.cyrilleroux.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

/**
 * @author Cyril Leroux
 *         Created on 07/03/15
 */
public class GooglePlusHelper
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_SIGN_IN = 1001;

    private Activity mActivity;
    private Context mApplicationContext;
    private PlusCallback mPlusCallback;

    /** Google client to communicate with Google */
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignedInUser;
    private ConnectionResult mConnectionResult;

    public GooglePlusHelper(Activity activity, PlusCallback callback) {
        mActivity = activity;
        mApplicationContext = activity.getApplicationContext();
        mPlusCallback = callback;
    }

    public void onCreate() {
        final Plus.PlusOptions plusOptions = new Plus.PlusOptions.Builder()
//                .addActivityTypes("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(Plus.API, plusOptions)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void onStart() {
        mGoogleApiClient.connect();
    }

    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        switch (requestCode) {
            case RC_SIGN_IN:
                Toast.makeText(mApplicationContext, "onActivityResult RC_SIGN_IN", Toast.LENGTH_LONG).show();
                if (responseCode == Activity.RESULT_OK) {
                    mSignedInUser = false;
                }
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignedInUser = false;
        Toast.makeText(mApplicationContext, "Google onConnected", Toast.LENGTH_LONG).show();
        updateGoogleSignInState();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(mApplicationContext, "Google onConnectionSuspended", Toast.LENGTH_LONG).show();
        mGoogleApiClient.connect();
        updateGoogleSignInState();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            Toast.makeText(mApplicationContext, "Google onConnectionFailed : No resolution", Toast.LENGTH_LONG).show();
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), mActivity, RC_SIGN_IN).show();
            updateGoogleSignInState();
            return;
        }
        if (!mIntentInProgress) {
            Toast.makeText(mApplicationContext, "Google onConnectionFailed : no intent in progress", Toast.LENGTH_LONG).show();
            // Store the connection result
            mConnectionResult = connectionResult;
            if (mSignedInUser) {
                resolveSignInError();
            }
        }
        updateGoogleSignInState();
    }

    public void signIn() {
        // start the asynchronous sign in flow
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
            mSignedInUser = true;
            resolveSignInError();
        }
    }

    public void signOut() {
        // sign out.
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
        }
        // show sign-in button, hide the sign-out button
        updateGoogleSignInState();
    }

    public void revokeAccess() {
        Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
    }

    private void resolveSignInError() {
        if (mConnectionResult == null || !mConnectionResult.hasResolution()) {
            return;
        }

        try {
            mIntentInProgress = true;
            mConnectionResult.startResolutionForResult(mActivity, RC_SIGN_IN);
        } catch (IntentSender.SendIntentException e) {
            mIntentInProgress = false;
            mGoogleApiClient.connect();
        }
    }

    /** Calls PusCallback if defined. */
    public void updateGoogleSignInState() {
        // No callback to call : return
        if (mPlusCallback == null) {
            return;
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            // Signed in.
            mPlusCallback.onPlusSignIn();

        } else {
            // Not signed in.
            mPlusCallback.onPlusSignOut();
        }
    }

    public static interface PlusCallback {

        void onPlusSignIn();

        void onPlusSignOut();
    }

}
