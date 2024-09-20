import 'dart:async';
import 'package:events_emitter/emitters/event_emitter.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_zoom_videosdk/native/zoom_videosdk.dart';
import 'package:flutter_zoom_videosdk/native/zoom_videosdk_event_listener.dart';
import 'package:meeting_app/screens/intro_screen.dart';
import 'package:meeting_app/utils/jwt.dart';

class CallScreen extends StatefulHookWidget {
  const CallScreen({super.key});

  @override
  State<CallScreen> createState() => _CallScreenState();
}

class _CallScreenState extends State<CallScreen> {
  bool _isLoading = true;

  @override
  Widget build(BuildContext context) {
    var zoom = ZoomVideoSdk();
    var eventListener = ZoomVideoSdkEventListener();

    useEffect(() {
      Future<void> joinSession() async {
        var token = generateJwt("sodaru-dev", "1");
        try {
          Map<String, bool> SDKaudioOptions = {
            "connect": true,
            "mute": true,
            "autoAdjustSpeakerVolume": false,
          };
          Map<String, bool> SDKvideoOptions = {
            "localVideoOn": true,
          };
          JoinSessionConfig joinSession = JoinSessionConfig(
            sessionName: "sodaru-dev",
            sessionPassword: "",
            token: token,
            userName: "Gp",
            audioOptions: SDKaudioOptions,
            videoOptions: SDKvideoOptions,
            sessionIdleTimeoutMins: 10,
          );
          await zoom.joinSession(joinSession);
        } catch (e) {
          showDialog(
            context: context,
            builder: (BuildContext context) => AlertDialog(
              title: const Text("Error"),
              content: const Text("Failed to join the session"),
              actions: <Widget>[
                TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('OK'),
                ),
              ],
            ),
          );
        } finally {
          setState(() {
            _isLoading = false;
          });
        }
      }

      joinSession();
      
      return null; // No cleanup needed for the event listener
    }, []);

    useEffect(() {
      EventEmitter emitter = eventListener.eventEmitter;

      emitter.on(EventType.onRequireSystemPermission, (data) async {
        data = data as Map;
        var permissionType = data['permissionType'];
        String title;
        switch (permissionType) {
          case SystemPermissionType.Camera:
            title = "Can't Access Camera";
            break;
          case SystemPermissionType.Microphone:
            title = "Can't Access Microphone";
            break;
          default:
            return; // Exit if unhandled
        }
        showDialog<String>(
          context: context,
          builder: (BuildContext context) => AlertDialog(
            title: Text(title),
            content: const Text(
                "Please turn on the toggle in system settings to grant permission."),
            actions: <Widget>[
              TextButton(
                onPressed: () => Navigator.pop(context, 'OK'),
                child: const Text('OK'),
              ),
            ],
          ),
        );
      });

      emitter.on(EventType.onError, (data) async {
        data = data as Map;
        String errorType = data['errorType'];
        showDialog<String>(
          context: context,
          builder: (BuildContext context) => AlertDialog(
            title: const Text("Error"),
            content: Text(errorType),
            actions: <Widget>[
              TextButton(
                onPressed: () => Navigator.pop(context, 'OK'),
                child: const Text('OK'),
              ),
            ],
          ),
        );
        if (errorType == Errors.SessionJoinFailed || errorType == Errors.SessionDisconncting) {
          Timer(const Duration(milliseconds: 1000), () {
            Navigator.popAndPushNamed(
              context,
              "Join",
              arguments: JoinArguments(
                true,
                "sodaru-dev",
                "",
                "Gp",
                "10",
                "1",
              ),
            );
          });
        }
      });

      return null; // No cleanup needed for the event listener
    }, []);

    return Scaffold(
      body: Center(
        child: _isLoading 
            ? const CircularProgressIndicator() 
            : const Text("Joined Call"),
      ),
    );
  }
}
