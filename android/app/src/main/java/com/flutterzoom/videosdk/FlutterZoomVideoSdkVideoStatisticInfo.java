package com.flutterzoom.videosdk;

import androidx.annotation.NonNull;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import us.zoom.sdk.ZoomVideoSDKUser;

public class FlutterZoomVideoSdkVideoStatisticInfo {

    public void getUserVideoBpf(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String userId = (String) params.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getVideoStatisticInfo().getBpf());
        }
    }

    public void getUserVideoFps(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String userId = (String) params.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getVideoStatisticInfo().getFps());
        }
    }

    public void getUserVideoHeight(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String userId = (String) params.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getVideoStatisticInfo().getHeight());
        }
    }

    public void getUserVideoWidth(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String userId = (String) params.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getVideoStatisticInfo().getWidth());
        }
    }

}
