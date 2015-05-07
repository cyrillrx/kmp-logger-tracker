package com.cyrillrx.network;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cyril Leroux
 *         Created on 08/04/2015.
 */
public class NetworkServiceDiscovery {

    private static final String TAG = NetworkServiceDiscovery.class.getSimpleName();
    private static final String SERVICE_TYPE = "_http._tcp.";

    private Context mContext;

    private NsdManager.RegistrationListener mRegistrationListener;
    private NsdManager.DiscoveryListener mDiscoveryListener;
    private NsdManager.ResolveListener mResolveListener;

    private ServerSocket mServerSocket;
    private NsdManager mNsdManager;
    private NsdServiceInfo mNsdServiceInfo;
    private List<NsdServiceInfo> mDiscoveredServices;

    private String mServiceName;
    private int mLocalPort;

    public void initRegistrationListener() {

        mRegistrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed!  Put debugging code here to determine why.
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed.  Put debugging code here to determine why.
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                // Save the service name.  Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                mServiceName = mNsdServiceInfo.getServiceName();
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                // Service has been unregistered.  This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
            }
        };
    }

    public void initDiscoveryListener() {

        mDiscoveredServices = new ArrayList<>();

        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                // A service was found!  Do something with it.
                Log.d(TAG, "Service discovery success" + serviceInfo);
                if (!serviceInfo.getServiceType().equals(SERVICE_TYPE)) {
                    // Service type is the string containing the protocol and
                    // transport layer for this service.
                    Log.d(TAG, "Unknown Service Type: " + serviceInfo.getServiceType());
                } else if (serviceInfo.getServiceName().equals(mServiceName)) {
                    // The name of the service tells the user what they'd be
                    // connecting to. It could be "Bob's Chat App".
                    Log.d(TAG, "Same machine: " + mServiceName);
                } else if (serviceInfo.getServiceName().contains("NsdChat")) {
                    mNsdManager.resolveService(serviceInfo, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e(TAG, "service lost" + serviceInfo);
            }
        };
    }

    public void initResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails.  Use the error code to debug.
                Log.e(TAG, "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

                if (serviceInfo.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same IP.");
                    return;
                }
                mNsdServiceInfo = serviceInfo;
                int port = mNsdServiceInfo.getPort();
                InetAddress host = mNsdServiceInfo.getHost();
            }
        };
    }

    public void initServerSocket() {

        // Initialize a server socket on the next available port.
        try {
            mServerSocket = new ServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store the chosen port.
        mLocalPort = mServerSocket.getLocalPort();
    }

    public void registerService(String serviceName, int port) {

        mNsdServiceInfo = new NsdServiceInfo();
        mNsdServiceInfo.setServiceName(serviceName);
        mNsdServiceInfo.setServiceType(SERVICE_TYPE);
        mNsdServiceInfo.setPort(port);

        mNsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);

        // Register service on the network
        mNsdManager.registerService(
                mNsdServiceInfo,
                NsdManager.PROTOCOL_DNS_SD,
                mRegistrationListener);

    }

    /** Discover devices */
    public void discoverServices() {
        mNsdManager.discoverServices(
                SERVICE_TYPE,
                NsdManager.PROTOCOL_DNS_SD,
                mDiscoveryListener);
    }

    public void tearDown() {
        mNsdManager.unregisterService(mRegistrationListener);
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    // Activity Lifecycle

    protected void onPause(Connection connection) {
        tearDown();
    }

    protected void onResume() {
        registerService("Cyril's Nexus", mServerSocket.getLocalPort());
        discoverServices();
    }

    protected void onDestroy() {
        tearDown();
        try {
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
