 #include <ESP8266WiFi.h>
 //#include <BlynkSimpleEsp8266.h>
 #include "DHT.h"

 #include <ESP8266HTTPClient.h>

 #include <WiFiClient.h>

 #include "MQ135.h"

 #define DHTTYPE DHT11

 // DHT CONFIG
 # define PIN_DHT 5
 DHT dht(PIN_DHT, DHTTYPE);
 // MQ135 CONFIG
 # define PIN_MQ135 4
 MQ135 mq135_sensor = MQ135(PIN_MQ135); //Khai báo đối tượng thư viện

 #define RAIN_SENSOR_PIN 12

 // WIFI CONFIG
 char ssid[] = "Thanh12b";
 char password[] = "12344321";
 // SERVER CONFIG
 String serverName = "http://air-quality-server2.herokuapp.com/air-qualities";
 unsigned long lastRequestTimeStamp = 0;
 unsigned long timerDelay = 1000*30;
 int deviceLocationID = 1;

 float humidity = 0.0;
 float temperature = 0.0;
 float rzero = 0.0;
 float correctedRZero = 0.0;
 float resistance = 0.0;
 float ppm = 0.0;
 float correctedPPM = 0.0;
 String weatherStatus = ""; 
 

 void setup() {
   setupWifiConnection();
   Serial.begin(9600);
   pinMode(RAIN_SENSOR_PIN, INPUT);
   dht.begin();
 }

 void setupWifiConnection() {
   WiFi.begin(ssid, password);
   Serial.println("Đang kết nối");
   while (WiFi.status() != WL_CONNECTED) {
     delay(500);
     Serial.print("Thử kết nối lại...");
   }
   Serial.println("");
   Serial.print("Đã kết nối. Địa chỉ IP hiện tại: ");
   Serial.println(WiFi.localIP());
 }

 void readDHTData() {
   humidity = dht.readHumidity();
   temperature = dht.readTemperature();
   if (isnan(humidity) || isnan(temperature)) {
     Serial.println("Không đọc được dữ liệu!");
   }
 }

 void postQuality() {
   // Your Domain name with URL path or IP address with path
   HTTPClient http;
   http.begin(serverName);
   http.addHeader("Content-Type", "application/json");
   String payload = "{";
   payload += "\"ppm\":\"" + String(ppm, 6) + "\",";
   payload += "\"correctedPPM\":\"" + String(correctedPPM, 6) + "\",";
   payload += "\"temperature\":\"" + String(temperature, 6) + "\",";
   payload += "\"humidity\":\"" + String(humidity, 6) + "\",";
   payload += "\"weather\":\"" + weatherStatus + "\",";
   payload += "\"location\": {";
   payload += "\"id\":\"" + String(deviceLocationID) + "\"}";
   payload += "}";

   int httpResponseCode = http.POST(payload);

   if (httpResponseCode < 0) {
     Serial.println("Upload Error. Debug payload:");
     Serial.println(payload);
   }
 }

 void loop() {
   if ((millis() - lastRequestTimeStamp) >= timerDelay) {
     //Check WiFi connection status
     if (WiFi.status() == WL_CONNECTED) {
       readDHTData();
       rzero = mq135_sensor.getRZero();
       correctedRZero = mq135_sensor.getCorrectedRZero(temperature, humidity);
       resistance = mq135_sensor.getResistance();
       ppm = mq135_sensor.getPPM();
       correctedPPM = mq135_sensor.getCorrectedPPM(temperature, humidity);

       int value = digitalRead(RAIN_SENSOR_PIN);
       if (value == HIGH) { // Cảm biến đang không mưa
        weatherStatus = "Không mưa";
       } else {
        weatherStatus = "Mưa";
       }

       Serial.print("MQ135 RZero: ");
       Serial.print(rzero);
       Serial.print("\t Corrected RZero: ");
       Serial.println(correctedRZero);
       Serial.print("\t Resistance: ");
       Serial.print(resistance);
       Serial.print("\t PPM: ");
       Serial.print(ppm);
       Serial.print("\t Corrected PPM: ");
       Serial.print(correctedPPM);
       Serial.println("ppm @ temp/hum: ");
       Serial.print(temperature);
       Serial.print("/");
       Serial.print(humidity);
       Serial.println("%");
       Serial.print("Weather Status:\t");
       Serial.println(weatherStatus);

       postQuality();
       //      
     } else {
       Serial.println("WiFi Disconnected");
     }
     lastRequestTimeStamp = millis();
   }
 }
