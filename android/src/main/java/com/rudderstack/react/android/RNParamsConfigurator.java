package com.rudderstack.react.android;

import com.facebook.react.bridge.ReadableMap;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.util.Utils;

import java.util.Map;

class RNParamsConfigurator {
    private final ReadableMap config;
    private RudderConfig.DBEncryption dbEncryption;
    boolean trackLifeCycleEvents = true;
    boolean recordScreenViews = false;
    long sessionTimeout = 300000L;
    boolean autoSessionTracking = true;
    String writeKey;

    RNParamsConfigurator(ReadableMap config) {
        this.config = config;
    }

    RudderConfig.Builder handleConfig() {
        setConfigValues();
        setWriteKey();
        RudderConfig.Builder configBuilder = buildConfig();
        addDBEncryptionPluginIfAvailable(configBuilder);
        disableAutoConfigFlagsForNativeSDK(configBuilder);
        return configBuilder;
    }

    private void setWriteKey() {
        if (this.config.hasKey("writeKey")) {
            writeKey = config.getString("writeKey");
        } else {
            throw new IllegalArgumentException("writeKey is required");
        }
    }

    private void setConfigValues() {
        if (this.config.hasKey("trackAppLifecycleEvents")) {
            trackLifeCycleEvents = config.getBoolean("trackAppLifecycleEvents");
        }
        if (this.config.hasKey("recordScreenViews")) {
            recordScreenViews = config.getBoolean("recordScreenViews");
        }
        if (this.config.hasKey("sessionTimeout")) {
            sessionTimeout = (long) config.getDouble("sessionTimeout");
        }
        if (this.config.hasKey("autoSessionTracking")) {
            autoSessionTracking = config.getBoolean("autoSessionTracking");
        }
    }

    private RudderConfig.Builder buildConfig() {
        RudderConfig.Builder configBuilder = new RudderConfig.Builder();
        if (config.hasKey("dataPlaneUrl")) {
            configBuilder.withDataPlaneUrl(config.getString("dataPlaneUrl"));
        }
        if (config.hasKey("controlPlaneUrl")) {
            configBuilder.withControlPlaneUrl(config.getString("controlPlaneUrl"));
        }
        if (config.hasKey("flushQueueSize")) {
            configBuilder.withFlushQueueSize(config.getInt("flushQueueSize"));
        }
        if (config.hasKey("dbCountThreshold")) {
            configBuilder.withDbThresholdCount(config.getInt("dbCountThreshold"));
        }
        if (config.hasKey("sleepTimeOut")) {
            configBuilder.withSleepCount(config.getInt("sleepTimeOut"));
        }
        if (config.hasKey("configRefreshInterval")) {
            configBuilder.withConfigRefreshInterval(config.getInt("configRefreshInterval"));
        }
        if (config.hasKey("autoCollectAdvertId")) {
            configBuilder.withAutoCollectAdvertId(config.getBoolean("autoCollectAdvertId"));
        }
        if (config.hasKey("logLevel")) {
            configBuilder.withLogLevel(config.getInt("logLevel"));
        }
        if (config.hasKey("collectDeviceId")) {
            configBuilder.withCollectDeviceId(config.getBoolean("collectDeviceId"));
        }
        if(config.hasKey("enableGzip")) {
            configBuilder.withGzip(config.getBoolean("enableGzip"));
        }
        return configBuilder;
    }

    private void addDBEncryptionPluginIfAvailable(RudderConfig.Builder configBuilder) {
        this.dbEncryption = RNRudderAnalytics.getDBEncryption();
        if (this.dbEncryption != null) {
            configBuilder.withDbEncryption(this.dbEncryption);
        }
    }

    private void disableAutoConfigFlagsForNativeSDK(RudderConfig.Builder configBuilder) {
        // We are relying on the RN implementation in RNLifeCycleEventListener and RNUserSessionPlugin hence we are explicitly setting all flags to false in Native Android SDK
        configBuilder.withRecordScreenViews(false);
        configBuilder.withTrackLifecycleEvents(false);
        configBuilder.withAutoSessionTracking(false);
    }
}
