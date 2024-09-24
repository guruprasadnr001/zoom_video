package com.flutterzoom.videosdk;

import androidx.annotation.NonNull;

import com.flutterzoom.videosdk.convert.*;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import us.zoom.sdk.ZoomVideoSDKUser;

public class FlutterZoomVideoSdkAudioStatus {

    public void isMuted(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getAudioStatus().isMuted());
        }
    }

    public void isTalking(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(user.getAudioStatus().isTalking());
        }
    }

    public void getAudioType(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            result.success(FlutterZoomVideoSdkAudioType.valueOf(user.getAudioStatus().getAudioType()));
        }
    }
}
