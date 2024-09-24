import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_zoom_videosdk/native/zoom_videosdk.dart';
import 'package:meeting_app/screens/call_screen.dart';
import 'package:meeting_app/screens/join_screen.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      title: 'Navigation Basics',
      home: ZoomVideoSdkProvider(),
    );
  }
}

class ZoomVideoSdkProvider extends StatelessWidget {
  const ZoomVideoSdkProvider({super.key});

  @override
  Widget build(BuildContext context) {
    var zoom = ZoomVideoSdk();
    InitConfig initConfig = InitConfig(
      domain: "zoom.us",
      enableLog: true,
    );
    zoom.initSdk(initConfig);
    return SafeArea(
      child: FirstRoute(),
    );
  }
}

class FirstRoute extends StatelessWidget {
   FirstRoute({super.key});

final Map<String, List<Permission>> platformPermissions = {
    "ios": [
      Permission.camera,
      Permission.microphone,
      //Permission.photos,
    ],
    "android": [
      Permission.camera,
      Permission.microphone,
      Permission.bluetoothConnect,
      Permission.phone,
      Permission.storage,
    ],
  };

  Future<void> requestFilePermission() async {
    if (!Platform.isAndroid && !Platform.isIOS) {}
    bool blocked = false;
    List<Permission>? notGranted = [];
    List<Permission>? permissions = Platform.isAndroid
        ? platformPermissions["android"]
        : platformPermissions["ios"];
    Map<Permission, PermissionStatus>? statuses = await permissions?.request();
    statuses!.forEach((key, status) {
      if (status.isDenied) {
        blocked = true;
      } else if (!status.isGranted) {
        notGranted.add(key);
      }
    });

    if (notGranted.isNotEmpty) {
      notGranted.request();
    }

    if (blocked) {
      await openAppSettings();
    }
  }

  @override
  Widget build(BuildContext context) {

    requestFilePermission();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Astropath meeting'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
              child: const Text('Audio Call'),
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const JoinScreen()),
                );
              },
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              child: const Text('Video Call'),
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const CallScreen()),
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}