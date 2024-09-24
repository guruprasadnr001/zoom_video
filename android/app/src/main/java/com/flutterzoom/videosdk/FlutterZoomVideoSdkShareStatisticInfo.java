package com.flutterzoom.videosdk;

import androidx.annotation.NonNull;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import us.zoom.sdk.ZoomVideoSDKUser;

public class FlutterZoomVideoSdkShareStatisticInfo {

    public void getUserShareBpf(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getShareStatisticInfo().getBpf());
        }
    }

    public void getUserShareFps(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getShareStatisticInfo().getFps());
        }
    }

    public void getUserShareHeight(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getShareStatisticInfo().getHeight());
        }
    }

    public void getUserShareWidth(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getShareStatisticInfo().getWidth());
        }
    }

}
