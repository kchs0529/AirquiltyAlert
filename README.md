# AirquiltyAlert


### 파일 구성

📦AirQualityAlert

 ┣ 📂lib //외부 라이브러리

 ┃ ┣ 📜json_simple.jar
 
 ┃ ┗ 📜mysql-connector-j-8.3.0.jar
 
 ┣ 📂resources
 
 ┃ ┗ 📜2023년3월_서울시_미세먼지.json //데이터 json파일
 
 ┣ 📂src
 
 ┃ ┗ 📂com
 
 ┃ ┃ ┗ 📂exem
 
 ┃ ┃ ┃ ┣ 📂airquality
 
 ┃ ┃ ┃ ┃ ┣ 📜AlertData.java

 ┃ ┃ ┃ ┃ ┣ 📜CheckStation.java 

 ┃ ┃ ┃ ┃ ┣ 📜DustAlert.java

 ┃ ┃ ┃ ┃ ┣ 📜DustData.java
 
 ┃ ┃ ┃ ┃ ┣ 📜ExeAirquality.java  // 실행 파일
 
 ┃ ┃ ┃ ┃ ┗ 📜Station.java
 
 ┃ ┃ ┃ ┣ 📂common
 
 ┃ ┃ ┃ ┃ ┗ 📜DatabaseConnection.java
 
 ┃ ┃ ┃ ┣ 📂jsp
 
 ┃ ┃ ┃ ┃ ┗ 📜Msg.jsp
 
 ┗ ┃ ┃ ┗ 📜import.sql // 데이터베이스 스크립트 문

---
## 개발환경

#### 자바 버젼 : JDK 11

#### 데이터베이스 : MySQL 8.0.21

#### IDEA : Eclipse

#### 외부 라이브러리 : json_simple.jar, mysql-connector-j-8.3.0.jar
