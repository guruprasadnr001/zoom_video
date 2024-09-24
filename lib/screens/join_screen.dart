import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:events_emitter/events_emitter.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_zoom_videosdk/native/zoom_videosdk.dart';
import 'package:flutter_zoom_videosdk/native/zoom_videosdk_event_listener.dart';
import 'package:flutter_zoom_videosdk/native/zoom_videosdk_user.dart';

import 'package:meeting_app/components/video_view.dart';
import 'package:meeting_app/main.dart';
import 'package:meeting_app/screens/intro_screen.dart';
import 'package:meeting_app/utils/jwt.dart';

class JoinScreen extends StatefulHookWidget {
  const JoinScreen({super.key});

  @override
  State<JoinScreen> createState() => _AudioCallScreenState();
}

class _AudioCallScreenState extends State<JoinScreen> {
  double opacityLevel = 1.0;
  @override
  Widget build(BuildContext context) {
    var zoom = ZoomVideoSdk();
    var eventListener = ZoomVideoSdkEventListener();
    var isInSession = useState(false);
    var sessionName = useState('');
    var sessionPassword = useState('');
    var users = useState(<ZoomVideoSdkUser>[]);
    var fullScreenUser = useState<ZoomVideoSdkUser?>(null);
    var sharingUser = useState<ZoomVideoSdkUser?>(null);
    var isMuted = useState(true);
    var isVideoOn = useState(false);
    var isSpeakerOn = useState(false);
    var isMounted = useIsMounted();
    var audioStatusFlag = useState(false);
    var isReceiveSpokenLanguageContentEnabled = useState(false);
    var isPiPView = useState(false);

    SystemChrome.setEnabledSystemUIMode(SystemUiMode.leanBack);
    var circleButtonSize = 65.0;
    Color buttonBackgroundColor = const Color.fromRGBO(0, 0, 0, 0.6);

    useEffect(() {
      Future<void> joinSession() async {
        var token = generateJwt("sodaru-dev", "1");
        try {
          Map<String, bool> SDKaudioOptions = {
            "connect": true,
            "mute": false,
            "autoAdjustSpeakerVolume": false,
          };
          Map<String, bool> SDKvideoOptions = {
            "localVideoOn": false,
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
        }
      }

      joinSession();

      return null;
    }, []);

    useEffect(() {
      eventListener.addEventListener();
      EventEmitter emitter = eventListener.eventEmitter;

      final sessionJoinListener =
          emitter.on(EventType.onSessionJoin, (sessionUser) async {
        isInSession.value = true;
        zoom.session
            .getSessionName()
            .then((value) => sessionName.value = value!);
        sessionPassword.value = await zoom.session.getSessionPassword();
        ZoomVideoSdkUser mySelf =
            ZoomVideoSdkUser.fromJson(jsonDecode(sessionUser.toString()));
        List<ZoomVideoSdkUser>? remoteUsers =
            await zoom.session.getRemoteUsers();
        var muted = await mySelf.audioStatus?.isMuted();
        var videoOn = await mySelf.videoStatus?.isOn();
        var speakerOn = await zoom.audioHelper.getSpeakerStatus();
        fullScreenUser.value = mySelf;
        remoteUsers?.insert(0, mySelf);
        isMuted.value = muted!;
        isSpeakerOn.value = speakerOn;
        isVideoOn.value = videoOn!;
        users.value = remoteUsers!;
        isReceiveSpokenLanguageContentEnabled.value = await zoom
            .liveTranscriptionHelper
            .isReceiveSpokenLanguageContentEnabled();
      });

      final sessionLeaveListener =
          emitter.on(EventType.onSessionLeave, (data) async {
        isInSession.value = false;
        users.value = <ZoomVideoSdkUser>[];
        fullScreenUser.value = null;
        Navigator.popAndPushNamed(
          context,
          "Join",
          arguments: JoinArguments(true, "sodaru-dev", "", "Gp", "10", "1"),
        );
      });

      final userAudioStatusChangedListener =
          emitter.on(EventType.onUserAudioStatusChanged, (data) async {
        data = data as Map;
        ZoomVideoSdkUser? mySelf = await zoom.session.getMySelf();
        var userListJson = jsonDecode(data['changedUsers']) as List;
        List<ZoomVideoSdkUser> userList = userListJson
            .map((userJson) => ZoomVideoSdkUser.fromJson(userJson))
            .toList();
        for (var user in userList) {
          {
            if (user.userId == mySelf?.userId) {
              mySelf?.audioStatus
                  ?.isMuted()
                  .then((muted) => isMuted.value = muted);
            }
          }
        }
        audioStatusFlag.value = !audioStatusFlag.value;
      });

      final userJoinListener = emitter.on(EventType.onUserJoin, (data) async {
        if (!isMounted()) return;
        data = data as Map;
        ZoomVideoSdkUser? mySelf = await zoom.session.getMySelf();
        var userListJson = jsonDecode(data['remoteUsers']) as List;
        List<ZoomVideoSdkUser> remoteUserList = userListJson
            .map((userJson) => ZoomVideoSdkUser.fromJson(userJson))
            .toList();
        remoteUserList.insert(0, mySelf!);
        users.value = remoteUserList;
      });

      final userLeaveListener = emitter.on(EventType.onUserLeave, (data) async {
        if (!isMounted()) return;
        ZoomVideoSdkUser? mySelf = await zoom.session.getMySelf();
        data = data as Map;
        List<ZoomVideoSdkUser>? remoteUserList =
            await zoom.session.getRemoteUsers();
        var leftUserListJson = jsonDecode(data['leftUsers']) as List;
        List<ZoomVideoSdkUser> leftUserLis = leftUserListJson
            .map((userJson) => ZoomVideoSdkUser.fromJson(userJson))
            .toList();
        if (fullScreenUser.value != null) {
          for (var user in leftUserLis) {
            {
              if (fullScreenUser.value?.userId == user.userId) {
                fullScreenUser.value = mySelf;
              }
            }
          }
        } else {
          fullScreenUser.value = mySelf;
        }
        remoteUserList?.add(mySelf!);
        users.value = remoteUserList!;
      });

      final requireSystemPermission =
          emitter.on(EventType.onRequireSystemPermission, (data) async {
        data = data as Map;
        var permissionType = data['permissionType'];
        switch (permissionType) {
          case SystemPermissionType.Camera:
            showDialog<String>(
              context: context,
              builder: (BuildContext context) => AlertDialog(
                title: const Text("Can't Access Camera"),
                content: const Text(
                    "please turn on the toggle in system settings to grant permission"),
                actions: <Widget>[
                  TextButton(
                    onPressed: () => Navigator.pop(context, 'OK'),
                    child: const Text('OK'),
                  ),
                ],
              ),
            );
            break;
          case SystemPermissionType.Microphone:
            showDialog<String>(
              context: context,
              builder: (BuildContext context) => AlertDialog(
                title: const Text("Can't Access Microphone"),
                content: const Text(
                    "please turn on the toggle in system settings to grant permission"),
                actions: <Widget>[
                  TextButton(
                    onPressed: () => Navigator.pop(context, 'OK'),
                    child: const Text('OK'),
                  ),
                ],
              ),
            );
            break;
        }
      });

      final eventErrorListener = emitter.on(EventType.onError, (data) async {
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
        if (errorType == Errors.SessionJoinFailed ||
            errorType == Errors.SessionDisconncting) {
          Timer(const Duration(milliseconds: 1000), () {
            Navigator.popAndPushNamed(
              context,
              "Join",
              arguments: JoinArguments(true, "sodaru-dev", "", "Gp", "10", "1"),
            );
          });
        }
      });

      return () => {
            sessionJoinListener.cancel(),
            sessionLeaveListener.cancel(),
            userAudioStatusChangedListener.cancel(),
            userJoinListener.cancel(),
            userLeaveListener.cancel(),
            eventErrorListener.cancel(),
            requireSystemPermission.cancel(),
          };
    }, [zoom, users.value, isMounted]);

    void onPressAudio() async {
      ZoomVideoSdkUser? mySelf = await zoom.session.getMySelf();
      if (mySelf != null) {
        final audioStatus = mySelf.audioStatus;
        if (audioStatus != null) {
          var muted = await audioStatus.isMuted();
          if (muted) {
            await zoom.audioHelper.unMuteAudio(mySelf.userId);
          } else {
            await zoom.audioHelper.muteAudio(mySelf.userId);
          }
        }
      }
    }

    void onLeaveSession(bool isEndSession) async {
      await zoom.leaveSession(isEndSession);
      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => FirstRoute()),
      );
    }

    void showLeaveOptions() async {
      ZoomVideoSdkUser? mySelf = await zoom.session.getMySelf();
      bool isHost = await mySelf!.getIsHost();

      Widget endSession;
      Widget leaveSession;
      Widget cancel = TextButton(
        child: const Text('Cancel'),
        onPressed: () {
          Navigator.pop(context); //close Dialog
        },
      );

      switch (defaultTargetPlatform) {
        case TargetPlatform.android:
          endSession = TextButton(
            child: const Text('End Session'),
            onPressed: () => onLeaveSession(true),
          );
          leaveSession = TextButton(
            child: const Text('Leave Session'),
            onPressed: () => {
              onLeaveSession(false),
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => FirstRoute()),
              )
            },
          );
          break;
        default:
          endSession = CupertinoActionSheetAction(
            isDestructiveAction: true,
            child: const Text('End Session'),
            onPressed: () => onLeaveSession(true),
          );
          leaveSession = CupertinoActionSheetAction(
            child: const Text('Leave Session'),
            onPressed: () => onLeaveSession(false),
          );
          break;
      }

      List<Widget> options = [
        leaveSession,
        cancel,
      ];

      if (Platform.isAndroid) {
        if (isHost) {
          options.removeAt(1);
          options.insert(0, endSession);
        }
        showDialog(
            context: context,
            builder: (context) {
              return AlertDialog(
                content: const Text("Do you want to leave this session?"),
                shape: const RoundedRectangleBorder(
                    borderRadius: BorderRadius.all(Radius.circular(2.0))),
                actions: options,
              );
            });
      } else {
        options.removeAt(1);
        if (isHost) {
          options.insert(1, endSession);
        }
        showCupertinoModalPopup(
          context: context,
          builder: (context) => CupertinoActionSheet(
            message:
                const Text('Are you sure that you want to leave the session?'),
            actions: options,
            cancelButton: cancel,
          ),
        );
      }
    }

    Widget fullScreenView;

    if (isInSession.value &&
        fullScreenUser.value != null &&
        users.value.isNotEmpty) {
      fullScreenView = AnimatedOpacity(
        opacity: opacityLevel,
        duration: const Duration(seconds: 3),
        child: VideoView(
          user: fullScreenUser.value,
          hasMultiCamera: false,
          isPiPView: isPiPView.value,
          sharing: sharingUser.value == null
              ? false
              : (sharingUser.value?.userId == fullScreenUser.value?.userId),
          preview: false,
          focused: false,
          multiCameraIndex: "0",
          videoAspect: VideoAspect.Original,
          fullScreen: true,
          resolution: VideoResolution.Resolution360,
        ),
      );
    } else {
      fullScreenView = Container(
          color: Colors.black,
          child: const Center(
            child: Text(
              "Connecting...",
              style: TextStyle(
                fontSize: 20,
                color: Colors.white,
              ),
            ),
          ));
    }

    return Scaffold(
        resizeToAvoidBottomInset: false,
        backgroundColor: Colors.black,
        body: Stack(
          children: [
            fullScreenView,
            Container(
                padding: const EdgeInsets.only(top: 35),
                child: Stack(
                  children: [
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        TextButton(
                            onPressed: (showLeaveOptions),
                            child: Container(
                              alignment: Alignment.topRight,
                              margin: const EdgeInsets.only(top: 16, right: 8),
                              padding: const EdgeInsets.only(
                                  top: 5, bottom: 5, left: 16, right: 16),
                              height: 28,
                              decoration: BoxDecoration(
                                borderRadius: const BorderRadius.all(
                                    Radius.circular(20.0)),
                                color: buttonBackgroundColor,
                              ),
                              child: const Text(
                                "LEAVE",
                                style: TextStyle(
                                  fontSize: 14,
                                  fontWeight: FontWeight.bold,
                                  color: Color(0xFFE02828),
                                ),
                              ),
                            )),
                      ],
                    ),
                    Align(
                        alignment: Alignment.centerRight,
                        child: FractionallySizedBox(
                          widthFactor: 0.2,
                          heightFactor: 0.6,
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              IconButton(
                                onPressed: onPressAudio,
                                icon: isMuted.value
                                    ? Image.asset("assets/icons/unmute@2x.png")
                                    : Image.asset("assets/icons/mute@2x.png"),
                                iconSize: circleButtonSize,
                                tooltip:
                                    isMuted.value == true ? "Unmute" : "Mute",
                              ),
                            ],
                          ),
                        )),
                  ],
                )),
          ],
        ));
  }
}
