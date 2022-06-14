package com.sift.sift_flutter;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import siftscience.android.Sift;

/** SiftFlutterPlugin */
public class SiftFlutterPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware{
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "sift_flutter");
    channel.setMethodCallHandler(this);
  }

  public void openSift(String[] siftData) {
    Sift.open(this, new Sift.Config.Builder()
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
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
