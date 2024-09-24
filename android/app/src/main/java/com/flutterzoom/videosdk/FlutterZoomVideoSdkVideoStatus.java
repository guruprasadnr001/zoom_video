package com.flutterzoom.videosdk;

import androidx.annotation.NonNull;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import us.zoom.sdk.ZoomVideoSDKUser;

public class FlutterZoomVideoSdkVideoStatus {

    public void isOn(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String userId = (String) params.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getVideoCanvas().getVideoStatus().isOn());
        }
    }

    public void hasVideoDevice(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String userId = (String) params.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getVideoCanvas().getVideoStatus().isHasVideoDevice());
        }
    }

}
