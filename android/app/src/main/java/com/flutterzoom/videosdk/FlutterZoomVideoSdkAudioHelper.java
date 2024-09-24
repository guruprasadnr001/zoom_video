package com.flutterzoom.videosdk;

import android.app.Activity;
import androidx.annotation.NonNull;

import com.flutterzoom.videosdk.convert.*;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import us.zoom.sdk.ZoomVideoSDK;
import us.zoom.sdk.ZoomVideoSDKAudioHelper;
import us.zoom.sdk.ZoomVideoSDKUser;

public class FlutterZoomVideoSdkAudioHelper {

    private Activity activity;

    FlutterZoomVideoSdkAudioHelper(Activity activity) {
        this.activity = activity;
    }

    private ZoomVideoSDKAudioHelper getAudioHelper() {
        ZoomVideoSDKAudioHelper audioHelper = null;
        try {
            audioHelper = ZoomVideoSDK.getInstance().getAudioHelper();
            if (audioHelper == null) {
                throw new Exception("No Audio Helper Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioHelper;
    }

    public void canSwitchSpeaker(@NonNull MethodChannel.Result result) {
        result.success(getAudioHelper().canSwitchSpeaker());
    }

    public void getSpeakerStatus(@NonNull MethodChannel.Result result) {
        result.success(getAudioHelper().getSpeakerStatus());
    }

    public void muteAudio(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.success(FlutterZoomVideoSdkErrors.valueOf(getAudioHelper().muteAudio(user)));
                }
            });
        }
    }

    public void unmuteAudio(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        if (user != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.success(FlutterZoomVideoSdkErrors.valueOf(getAudioHelper().unMuteAudio(user)));
                }
            });
        }
    }

    public void setSpeaker(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        boolean isOn = (Boolean) args.get("isOn");

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getAudioHelper().setSpeaker(isOn)));
            }
        });
    }

    public void startAudio(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getAudioHelper().startAudio()));
            }
        });
    }

    public void stopAudio(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getAudioHelper().stopAudio()));
            }
        });
    }

    public void subscribe(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getAudioHelper().subscribe()));
            }
        });
    }

    public void unsubscribe(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getAudioHelper().unSubscribe()));
            }
        });
    }

    public void resetAudioSession(@NonNull MethodChannel.Result result) {
        result.success(false);
    }

    public void cleanAudioSession(@NonNull MethodChannel.Result result) {
        result.success(null);
    }

}
