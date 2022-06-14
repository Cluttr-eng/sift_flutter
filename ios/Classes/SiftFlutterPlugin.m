#import "SiftFlutterPlugin.h"
#import "Sift/Sift.h"

@implementation SiftFlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"sift_flutter"
            binaryMessenger:[registrar messenger]];
  SiftFlutterPlugin* instance = [[SiftFlutterPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"setUserID" isEqualToString:call.method]) {
    Sift *sift = [Sift sharedInstance];
    [sift setAccountId:[call.arguments[0] stringValue]];
    [sift setBeaconKey:[call.arguments[1] stringValue]];
    [sift setUserId:[call.arguments[2] stringValue]];
    result(@"Setting sift user ID");
  } 
  else if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } 
  else {
    result(FlutterMethodNotImplemented);
  }
}

@end
