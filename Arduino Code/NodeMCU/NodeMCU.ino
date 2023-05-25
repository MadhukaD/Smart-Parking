#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <SoftwareSerial.h>

#define FIREBASE_HOST "smartcarpark-fae71-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "GgZZUtjSxEZ24fEI6VbarMMnFT9CoZabK0we1Qsj"
#define WIFI_SSID "Home"
#define WIFI_PASSWORD "Cobalt123"

String str1;

void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  Serial1.begin(19200);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }
}

void loop() { // run over and over
  String BookedSlot1 = Firebase.getString("BookedSlot1");
  String BookedSlot2 = Firebase.getString("BookedSlot2");
  String BookedSlot3 = Firebase.getString("BookedSlot3");
  String Gate = Firebase.getString("Gate");
  String all = String(BookedSlot1)+String(",")+String(BookedSlot2)+String(",")+String(BookedSlot3)+String(",")+String(Gate)+String(",");
  Serial.println(all);
  
  if (Serial.available()) {
    str1 = Serial.readString();
  }

  int commaIndex = str1.indexOf(',');
  int secondCommaIndex = str1.indexOf(',', commaIndex + 1);
  int thirdCommaIndex = str1.indexOf(',', secondCommaIndex + 1);

  String firstValue = str1.substring(0, commaIndex);
  String secondValue = str1.substring(commaIndex + 1, secondCommaIndex);
  String thirdValue = str1.substring(secondCommaIndex + 1, thirdCommaIndex);

  Firebase.setString("Slot1", firstValue);
  Firebase.setString("Slot2", secondValue);
  Firebase.setString("Slot3", thirdValue);


  
}
