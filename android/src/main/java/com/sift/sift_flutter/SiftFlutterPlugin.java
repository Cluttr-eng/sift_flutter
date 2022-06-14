package com.sift.sift_flutter;

import androidx.annotation.NonNull;
import android.util.Log;
import android.app.Activity;

import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import siftscience.android.Sift;


/** SiftFlutterPlugin */
public class SiftFlutterPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "sift_flutter");
    channel.setMethodCallHandler(this);
  }

  public void openSift(String[] siftData) {
    Sift.open(activity, new Sift.Config.Builder()
            .withAccountId(siftData[0])
            .withBeaconKey(siftData[1])
            .withDisallowLocationCollection(true)
            .build());
    Sift.setUserId(siftData[2]);
    Sift.collect();
  }

  public void unSetUserID() {
      Sift.unsetUserId();
      Sift.collect();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("setUserID")) {
      String siftDataStr = call.arguments.toString();
      Log.d("sift data string", siftDataStr);
      siftDataStr = siftDataStr.replace("[", "");
      siftDataStr = siftDataStr.replace("]", "");
      siftDataStr = siftDataStr.replace(" ", "");
      String[] siftData = siftDataStr.split(",");
      openSift(siftData);
      result.success("Success set UserID!!!");
  } else if (call.method.equals("unsetUserID")) {
      unSetUserID();
      result.success("Success unset User!!!");
  } else {
      result.notImplemented();
    }
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
    activity = activityPluginBinding.getActivity();
    // TODO: your plugin is now attached to an Activity
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    // TODO: the Activity your plugin was attached to was destroyed to change configuration.
    // This call will be followed by onReattachedToActivityForConfigChanges().
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {
    // TODO: your plugin is now attached to a new Activity after a configuration change.
  }

  @Override
  public void onDetachedFromActivity() {
    // TODO: your plugin is no longer associated with an Activity. Clean up references.
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
