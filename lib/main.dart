import 'package:flutter/material.dart';
import 'package:flutter_zoom_videosdk/native/zoom_videosdk.dart';
import 'package:meeting_app/screens/call_screen.dart';
import 'package:meeting_app/screens/join_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Navigation Basics',
      home: const ZoomVideoSdkProvider(),
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
    return const SafeArea(
      child: FirstRoute(),
    );
  }
}

class FirstRoute extends StatelessWidget {
  const FirstRoute({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Astropath meeting'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
              child: const Text('Join'),
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const JoinScreen()),
                );
              },
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              child: const Text('Create'),
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