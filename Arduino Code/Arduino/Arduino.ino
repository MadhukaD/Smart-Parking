#include <Wire.h>
#include <Servo.h>
#include <LiquidCrystal_I2C.h>

// Set the LCD address to 0x27 for a 16 chars and 2 line display
LiquidCrystal_I2C lcd(0x27, 16, 2);

const int pinIRd1 = 8; //Slot IR
const int pinIRd2 = 10; //Slot IR
const int pinIRd3 = 12; //Slot IR

int LEDred = 13;
int LEDgreen = 6;
int LED = 7;

int IRvalue1;
int IRvalue2;
int IRvalue3;

int booked1;
int booked2;
int booked3;
int dataSensor;

int slots;
String str;
String str1;

Servo myservo1;
int IR1 = 5; // Gate IR Sensor 1
int IR2 = 4; // Gate IR Sensor 2
int isOpen = 0;

void breakString(String sentence);
void calcValues();
void openGate(int sensor1, int sensor2, int databaseSensor);

void setup()
{
  Serial.begin(115200);
  Serial1.begin(9600);
  Serial2.begin(19200);

  Serial.print(char(169));

  // initialize the LCD
  lcd.begin();

  pinMode(LED, OUTPUT);
  pinMode(LEDred, OUTPUT);
  pinMode(LEDgreen, OUTPUT);

  // Turn on the blacklight and print a message.
  lcd.backlight();
  lcd.setCursor(0,0);
  lcd.print("WELCOME");
  lcd.setCursor(8,1);
  lcd.print("Noobz 69");

  delay(5000);

  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print("Slots:3");
  lcd.setCursor(9,0);
  lcd.print("SlotA:E");
  lcd.setCursor(0,1);
  lcd.print("SlotB:E");
  lcd.setCursor(9,1);
  lcd.print("SlotC:E");

  pinMode(pinIRd1,INPUT);
  pinMode(pinIRd2,INPUT);
  pinMode(pinIRd3,INPUT);

  slots = IRvalue1 + IRvalue2 + IRvalue3;
  
  pinMode(IR1, INPUT);
  pinMode(IR2, INPUT);
  
  myservo1.attach(3);
  myservo1.write(100);

  while (!Serial1) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  
  delay (2000);

}
void loop(){

  
  if (Serial1.available()) {
    str1 = Serial1.readString();
  }

  breakString(str1);
  calcValues();

  str =String(digitalRead(pinIRd1))+String(",")+String(digitalRead(pinIRd2))+String(",")+String(digitalRead(pinIRd3))+String(",");
  Serial1.println(str);

  if (slots == 0)
  {
    digitalWrite(LED, HIGH);
  }
  else
  {
    digitalWrite(LED, LOW);
  }

  lcd.setCursor(6,0);
  lcd.print(slots);
  
  if(IRvalue1 == 0)
  {
    lcd.setCursor(15,0);
    lcd.print("F");
  }
  else
  {
    lcd.setCursor(15,0);
    lcd.print("E");
  }
  if(IRvalue2 == 0)
  {
    lcd.setCursor(6,1);
    lcd.print("F");
  }
  else
  {
    lcd.setCursor(6,1);
    lcd.print("E"); 
  }
  if(IRvalue3 == 0)
  {
    lcd.setCursor(15,1);
    lcd.print("F");
  }
  else
  {
    lcd.setCursor(15,1);
    lcd.print("E");
  }
  
  delay(1000);

  slots = IRvalue1 + IRvalue2 + IRvalue3;

  openGate(digitalRead(IR1), digitalRead(IR2), dataSensor);
}

void openGate(int sensor1, int sensor2, int databaseSensor)
{
  if (isOpen == 1)
  {
    if(sensor1 == 0 || sensor2 == 0)
    {
      myservo1.write(0);
      digitalWrite(LEDgreen, LOW);
      digitalWrite(LEDred, HIGH);
      isOpen = 0;
      delay(1000);
    }
    
  }
  else if (isOpen == 0)
  {
    if(sensor1 == 0 && slots!= 0)
    {
      myservo1.write(90);
      digitalWrite(LEDgreen, HIGH);
      digitalWrite(LEDred, LOW);
      isOpen = 1;
      delay(1000);
    }
    else if(sensor2 == 0)
    {
      myservo1.write(90);
      digitalWrite(LEDgreen, HIGH);
      digitalWrite(LEDred, LOW);
      isOpen = 1;
      delay(1000);
    }
    else if (sensor1 == 0 && databaseSensor == 1)
    {
      myservo1.write(90);
      digitalWrite(LEDgreen, HIGH);
      digitalWrite(LEDred, LOW);
      isOpen = 1;
      delay(1000);
    }
  }
}

void breakString(String sentence)
{
  int commaIndex = sentence.indexOf(',');
  int secondCommaIndex = sentence.indexOf(',', commaIndex + 1);
  int thirdCommaIndex = sentence.indexOf(',', secondCommaIndex + 1);
  int forthCommaIndex = sentence.indexOf(',', thirdCommaIndex + 1);

  String firstValue = sentence.substring(0, commaIndex);
  String secondValue = sentence.substring(commaIndex + 1, secondCommaIndex);
  String thirdValue = sentence.substring(secondCommaIndex + 1, thirdCommaIndex);
  String forthValue = sentence.substring(thirdCommaIndex + 1, forthCommaIndex);
  
  booked1 = firstValue.toInt();
  booked2 = secondValue.toInt();
  booked3 = thirdValue.toInt();
  dataSensor = forthValue.toInt();
}

void calcValues()
{
  if (digitalRead(pinIRd1) == 1 && booked1 == 1)
  {
    IRvalue1 = 1;
  }
  else 
  {
    IRvalue1 = 0;
  }
  if (digitalRead(pinIRd2) == 1 && booked2 == 1)
  {
    IRvalue2 = 1;
  }
  else 
  {
    IRvalue2 = 0;
  }
  if (digitalRead(pinIRd3) == 1 && booked3 == 1)
  {
    IRvalue3 = 1;
  }
  else 
  {
    IRvalue3 = 0;
  }
}
