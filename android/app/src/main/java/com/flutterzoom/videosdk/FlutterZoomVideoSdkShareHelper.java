package com.flutterzoom.videosdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;

import androidx.annotation.NonNull;

import com.flutterzoom.videosdk.convert.*;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import us.zoom.sdk.ZoomVideoSDK;
import us.zoom.sdk.ZoomVideoSDKShareHelper;

public class FlutterZoomVideoSdkShareHelper {

    private Activity activity;
    private Context context;

    FlutterZoomVideoSdkShareHelper(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    private ZoomVideoSDKShareHelper getShareHelper() {
        ZoomVideoSDKShareHelper shareHelper = null;
        try {
            shareHelper = ZoomVideoSDK.getInstance().getShareHelper();
            if (shareHelper == null) {
                throw new Exception("No Share Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shareHelper;
    }

    public void shareScreen() throws Exception {
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        if (getShareHelper().isSharingOut()) {
            return;
        }
        MediaProjectionManager manager =
                (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (manager != null) {
            Intent intent = manager.createScreenCaptureIntent();
            activity.startActivityForResult(intent, 0, null);
        } else {
            throw new Exception("Notification service must be implemented.");
        }
    }

    public void shareView() {
        // TODO
    }

    public void lockShare(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        boolean lock = (Boolean) args.get("lock");

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getShareHelper().lockShare(lock)));
            }
        });
    }

    public void stopShare(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getShareHelper().stopShare()));
            }
        });
    }

    public void isOtherSharing(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getShareHelper().isOtherSharing());
            }
        });
    }

    public void isScreenSharingOut(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getShareHelper().isScreenSharingOut());
            }
        });
    }

    public void isShareLocked(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getShareHelper().isShareLocked());
            }
        });
    }

    public void isSharingOut(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getShareHelper().isSharingOut());
            }
        });
    }

    public void isAnnotationFeatureSupport(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getShareHelper().isAnnotationFeatureSupport());
            }
        });
    }

    public void disableViewerAnnotation(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String, Object> args = call.arguments();
        boolean disable = (Boolean) args.get("disable");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getShareHelper().disableViewerAnnotation(disable));
            }
        });
    }

    public void isViewerAnnotationDisabled(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getShareHelper().isViewerAnnotationDisabled());
            }
        });
    }

    public void destroyAnnotationHelper(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(getShareHelper().destroyAnnotationHelper(FlutterZoomVideoSdkView.getInstance().getAnnotationHelper()));
            }
        });
    }

    public void pauseShare(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getShareHelper().pauseShare()));
            }
        });
    }

    public void resumeShare(@NonNull MethodChannel.Result result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(FlutterZoomVideoSdkErrors.valueOf(getShareHelper().resumeShare()));
            }
        });
    }

}
