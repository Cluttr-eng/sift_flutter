import 'dart:async';

import 'package:flutter/services.dart';

class SiftFlutter {
  static const MethodChannel _channel = MethodChannel('sift_flutter');

  SiftFlutter._privateConstructor();

  static final SiftFlutter _instance = SiftFlutter._privateConstructor();
  static SiftFlutter get instance => _instance;

  String? siftAccountId;
  String? siftBeaconKey;

  initialize(
    String siftAccountId,
    String siftBeaconKey,
  ) {
    this.siftAccountId = siftAccountId;
    this.siftBeaconKey = siftBeaconKey;
  }

  Future<void> setUserId(String userId) async {
    await _channel.invokeMethod(
      'setUserID',
      [
        siftAccountId,
        siftBeaconKey,
        userId,
      ],
    );
  }

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
