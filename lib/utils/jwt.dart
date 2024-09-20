import 'dart:math';

import 'package:dart_jsonwebtoken/dart_jsonwebtoken.dart';

String makeId(int length) {
  String result = "";
  String characters =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  int charactersLength = characters.length;
  for (var i = 0; i < length; i++) {
    result += characters[Random().nextInt(charactersLength)];
  }
  return result;
}

String generateJwt(String sessionName, String roleType) {
  try {
    var iat = DateTime.now();
    var exp = DateTime.now().add(Duration(days: 2));
    final jwt = JWT(
      {
        'app_key': "fTe6vaXKfWR9IrnyR4AJKMRr7TxlbQLzas9Y",
        'version': 1,
        'user_identity': makeId(10),
        'iat': (iat.millisecondsSinceEpoch / 1000).round(),
        'exp': (exp.millisecondsSinceEpoch / 1000).round(),
        'tpc': sessionName,
        'role_type': int.parse(roleType),
        'cloud_recording_option': 1,
      },
    );
    var token = jwt.sign(SecretKey('i0gDNqCtsR71w8vxIELyPJMwZq98U409fEmX'));
    return token;
  } catch (e) {
    print(e);
    return '';
  }
}
