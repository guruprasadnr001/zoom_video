package com.flutterzoom.videosdk;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.flutterzoom.videosdk.convert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import us.zoom.sdk.ZoomVideoSDK;
import us.zoom.sdk.ZoomVideoSDKCameraDevice;
import us.zoom.sdk.ZoomVideoSDKVideoHelper;
import us.zoom.sdk.ZoomVideoSDKVideoPreferenceMode;
import us.zoom.sdk.ZoomVideoSDKVideoPreferenceSetting;

public class FlutterZoomVideoSdkVideoHelper {

    private Activity activity;

    FlutterZoomVideoSdkVideoHelper(Activity activity) {
        this.activity = activity;
    }

    private ZoomVideoSDKVideoHelper getVideoHelper() {
        ZoomVideoSDKVideoHelper videoHelper = null;
        try {
            videoHelper = ZoomVideoSDK.getInstance().getVideoHelper();
            if (videoHelper == null) {
                throw new Exception("No Video Helper Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoHelper;
    }

    public void getCameraList(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkCameraDevice.jsonCameraArray(getVideoHelper().getCameraList()));
            }
        });
    }

    public void getNumberOfCameras(@NonNull MethodChannel.Result result) {
        result.success(getVideoHelper().getNumberOfCameras());
    }

    public void rotateMyVideo(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        double rotation = (Double) params.get("rotation");

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getVideoHelper().rotateMyVideo((int) rotation));
            }
        });
    }

    public void startVideo(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getVideoHelper().startVideo()));
            }
        });
    }

    public void stopVideo(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getVideoHelper().stopVideo()));
            }
        });
    }

    public void switchCamera(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String deviceId = (String) params.get("deviceId");

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (deviceId != null) {
                    ZoomVideoSDKCameraDevice device = getVideoHelper().getCameraList()
                            .stream()
                            .filter(c -> c.getDeviceId().equals(deviceId))
                            .findAny()
                            .orElse(null);
                    if (device != null) {
                        result.success(getVideoHelper().switchCamera(device));
                    }
                }
                result.success(getVideoHelper().switchCamera());
            }
        });
    }

    public void mirrorMyVideo(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        boolean enable = (boolean) params.get("enable");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getVideoHelper().mirrorMyVideo(enable)));
            }
        });
    }

    public void isMyVideoMirrored(@NonNull MethodChannel.Result result) {
        result.success(getVideoHelper().isMyVideoMirrored());
    }

    public void isOriginalAspectRatioEnabled(@NonNull MethodChannel.Result result) {
        result.success(getVideoHelper().isOriginalAspectRatioEnabled());
    }

    public void enableOriginalAspectRatio(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        boolean enable = (boolean) params.get("enable");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getVideoHelper().enableOriginalAspectRatio(enable));
            }
        });
    }

    public void turnOnOrOffFlashlight(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        boolean isOn = (boolean) params.get("isOn");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getVideoHelper().turnOnOrOffFlashlight(isOn));
            }
        });
    }


    public void isSupportFlashlight(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getVideoHelper().isSupportFlashlight());
            }
        });
    }

    public void isFlashlightOn(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getVideoHelper().isFlashlightOn());
            }
        });
    }
}