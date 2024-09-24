package com.flutterzoom.videosdk;

import androidx.annotation.NonNull;

import com.flutterzoom.videosdk.convert.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import us.zoom.sdk.ZoomVideoSDK;
import us.zoom.sdk.ZoomVideoSDKSession;
import us.zoom.sdk.ZoomVideoSDKUser;

public class FlutterZoomVideoSdkUser {

    public static ZoomVideoSDKUser getUser(String userId) {
        ZoomVideoSDKSession session = ZoomVideoSDK.getInstance().getSession();
        ZoomVideoSDKUser myUser = session.getMySelf();

        if (myUser.getUserID().equals(userId)) {
            return myUser;
        }

        return session.getRemoteUsers()
                .stream()
                .filter(u -> u.getUserID().equals(userId))
                .findAny()
                .orElse(null);
    }

    public static String jsonUserArray(List<ZoomVideoSDKUser> userList) {
        JsonArray mappedUserArray = new JsonArray();
        for (ZoomVideoSDKUser user : userList) {
            JsonElement jsonUser = new Gson().fromJson(jsonUser(user), JsonElement.class);
            mappedUserArray.add(jsonUser);
        }

        return mappedUserArray.toString();
    }

    public static String jsonUser(ZoomVideoSDKUser user) {
        Map<String, Object> mappedUser = new HashMap<>();
        mappedUser.put("userId", user.getUserID());
        mappedUser.put("customUserId", user.getCustomIdentity());
        mappedUser.put("userName", user.getUserName());
        mappedUser.put("isHost", user.isHost());
        mappedUser.put("isManager", user.isManager());

        Gson gson = new GsonBuilder().create();
        String jsonUser = gson.toJson(mappedUser);

        return jsonUser;
    }

    public void getUserName(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        ZoomVideoSDKUser user = getUser(userId);
        if (user != null) {
            result.success(user.getUserName());
        }
    }

    public void getShareStatus(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        ZoomVideoSDKUser user = getUser(userId);
        if (user != null) {
            result.success(FlutterZoomVideoSdkShareStatus.valueOf(user.getShareCanvas().getShareStatus()));
        }
    }

    public void isHost(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        ZoomVideoSDKUser user = getUser(userId);
        if (user != null) {
            result.success(user.isHost());
        }
    }

    public void isManager(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        ZoomVideoSDKUser user = getUser(userId);
        if (user != null) {
            result.success(user.isManager());
        }
    }

    public void getMultiCameraCanvasList(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        ZoomVideoSDKUser user = getUser(userId);
        if (user != null) {
            result.success(user.getMultiCameraCanvasList());
        }
    }

    public void hasIndividualRecordingConsent(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        ZoomVideoSDKUser user = getUser(userId);
        if (user != null) {
            result.success(user.hasIndividualRecordingConsent());
        }
    }

    public void setUserVolume(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        float volume = ((Double) args.get("volume")).floatValue();
        boolean isShareAudio = (boolean) args.get("isShareAudio");
        ZoomVideoSDKUser user = getUser(userId);
        if (user != null) {
            result.success(user.setUserVolume(volume, isShareAudio));
        }
    }

    public void getUserVolume(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        boolean isShareAudio = (boolean) args.get("isShareAudio");
        ZoomVideoSDKUser user = getUser(userId);
        if (user != null) {
            result.success(user.getUserVolume(isShareAudio));
        }
    }

    public void canSetUserVolume(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        boolean isShareAudio = (boolean) args.get("isShareAudio");
        ZoomVideoSDKUser user = getUser(userId);
        if (user != null) {
            result.success(user.canSetUserVolume(isShareAudio));
        }
    }

    public void getUserGUID(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        String userId = (String) args.get("userId");
        ZoomVideoSDKUser user = getUser(userId);
        if (user != null) {
            result.success(user.getUserGUID());
        }
    }

}
