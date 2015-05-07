package com.cyrillrx.social;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * @author Cyril Leroux
 *         Created on 07/03/15
 */
public class GooglePlusHelper
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = GooglePlusHelper.class.getSimpleName();

    public static final int RC_SIGN_IN = 1987001;
    public static final int RC_SIGN_OUT = 1987002;

    private Activity mActivity;
    private PlusCallback mPlusCallback;

    /** Google client to communicate with Google */
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mIsSigningIn;
    private boolean mIsSigningOut;
    private ConnectionResult mConnectionResult;

    public GooglePlusHelper(Activity activity, PlusCallback callback) {
        mActivity = activity;
        mPlusCallback = callback;
    }

    public void onCreate() {
        final Plus.PlusOptions plusOptions = new Plus.PlusOptions.Builder().build();

        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(Plus.API, plusOptions)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void onStart() { mGoogleApiClient.connect(); }

    public void onStop() { mGoogleApiClient.disconnect(); }

    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        switch (requestCode) {

            case RC_SIGN_IN:
                Log.v(TAG, "onActivityResult() RC_SIGN_IN responseCode : " + responseCode);
                if (responseCode == Activity.RESULT_OK) {
                    mIsSigningIn = false;
                }
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                updateGoogleSignInState();
                break;

            case RC_SIGN_OUT:
                Log.v(TAG, "onActivityResult() RC_SIGN_OUT responseCode : " + responseCode);
                if (responseCode == Activity.RESULT_OK) {
                    mIsSigningOut = false;
                }
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                updateGoogleSignInState();
                break;

            default:
                Log.v(TAG, "onActivityResult() requestCode " + requestCode + "; responseCode : " + responseCode);

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v(TAG, "onConnected() with bundle " + bundle);
        updateGoogleSignInState();
        mIsSigningIn = false;
        mIsSigningOut = false;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        if (cause == CAUSE_SERVICE_DISCONNECTED) {
            Log.v(TAG, "Connection suspended (Service disconnected)");
        } else if (cause == CAUSE_NETWORK_LOST) {
            Log.v(TAG, "Connection suspended (Network lost)");
        } else {
            Log.v(TAG, "Connection suspended (Unspecified cause)");
        }
        mGoogleApiClient.connect();
        updateGoogleSignInState();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(TAG, "onConnectionFailed result : " + connectionResult);

        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), mActivity, RC_SIGN_IN).show();
            updateGoogleSignInState();
            return;
        }

        if (!mIntentInProgress) {
            // Store the connection result
            mConnectionResult = connectionResult;
            if (mIsSigningIn) {
                resolveSignInError();
            }
            if (mIsSigningOut) {
                resolveSignOutError();
            }
        }

        updateGoogleSignInState();
    }

    public void signIn() {
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            Log.v(TAG, "signIn() but connected or connecting");
            return;
        }
        Log.v(TAG, "signIn()");

        // start the asynchronous sign in flow
        mIsSigningIn = true;
        mGoogleApiClient.connect();
        resolveSignInError();
    }

    public void signOut() {
        if (!mGoogleApiClient.isConnected()) {
            Log.v(TAG, "signOut() but not connected");
            return;
        }
        Log.v(TAG, "signOut()");

        // start the asynchronous sign out flow
        mIsSigningOut = true;
        mGoogleApiClient
                .clearDefaultAccountAndReconnect()
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.v(TAG, "signOut() onResult " + status);
                        mGoogleApiClient.reconnect();
                    }
                });
    }

    public void revokeAccess() {
        if (!mGoogleApiClient.isConnected()) {
            Log.v(TAG, "revokeAccess() but not connected");
            return;
        }
        Log.v(TAG, "revokeAccess()");

        mIsSigningOut = true;
        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        Plus.AccountApi
                .revokeAccessAndDisconnect(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.v(TAG, "revokeAccess() onResult " + status);
                        mGoogleApiClient.reconnect();
                    }
                });
    }

    private void resolveSignInError() {
        if (mConnectionResult == null || !mConnectionResult.hasResolution()) {
            Log.v(TAG, "resolveSignInError() but no resolution");
            return;
        }
        Log.v(TAG, "resolveSignInError()");

        try {
            mIntentInProgress = true;
            mConnectionResult.startResolutionForResult(mActivity, RC_SIGN_IN);
        } catch (IntentSender.SendIntentException e) {
            mIntentInProgress = false;
            mGoogleApiClient.connect();
        }
    }

    private void resolveSignOutError() {
        if (mConnectionResult == null || !mConnectionResult.hasResolution()) {
            Log.v(TAG, "resolveSignOutError() but no resolution");
            return;
        }
        Log.v(TAG, "resolveSignOutError()");

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
            mPlusCallback.onConnected();

        } else {
            // Not signed in.
            mPlusCallback.onDisconnected();
        }
    }

    public Person getCurrentPerson() {
        try {
            return Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        } catch (Exception e) {
            Log.w(TAG, "Error while getting the current person", e);
            return null;
        }
    }

    public interface PlusCallback {

        void onConnected();

        void onDisconnected();
    }

}
