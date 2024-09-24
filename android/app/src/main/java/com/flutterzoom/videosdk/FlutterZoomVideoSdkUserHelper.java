package com.flutterzoom.videosdk;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.util.Map;
import com.flutterzoom.videosdk.convert.*;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import us.zoom.sdk.ZoomVideoSDK;
import us.zoom.sdk.ZoomVideoSDKUser;
import us.zoom.sdk.ZoomVideoSDKUserHelper;

public class FlutterZoomVideoSdkUserHelper {

    private Activity activity;

    FlutterZoomVideoSdkUserHelper(Activity activity) {
        this.activity = activity;
    }

    private ZoomVideoSDKUserHelper getUserHelper() {
        ZoomVideoSDKUserHelper userHelper = null;
        try {
            userHelper = ZoomVideoSDK.getInstance().getUserHelper();
            if (userHelper == null) {
                throw new Exception("No User Helper Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userHelper;
    }

    public void changeName(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String userId = (String) params.get("userId");
        String name = (String) params.get("name");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getUserHelper().changeName(name, user));
            }
        });
    }

    public void makeHost(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String userId = (String) params.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getUserHelper().makeHost(user));
            }
        });
    }

    public void makeManager(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String userId = (String) params.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getUserHelper().makeManager(user));
            }
        });
    }

    public void revokeManager(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String userId = (String) params.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getUserHelper().revokeManager(user));
            }
        });
    }

    public void removeUser(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> params = call.arguments();
        String userId = (String) params.get("userId");

        ZoomVideoSDKUser user = FlutterZoomVideoSdkUser.getUser(userId);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getUserHelper().removeUser(user));
            }
        });
    }

}
