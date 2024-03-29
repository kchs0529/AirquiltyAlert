package com.exem.airquality;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AirqualityAlert {
	
	private static Map<String, Integer> previousPM10 = new HashMap<>();//HashMap의 key는 stationCode
    private static Map<String, Integer> previousPM25 = new HashMap<>();//HashMap의 key는 stationCode
	
	public static void main(String[] args) throws Exception {
        JSONParser parser = new JSONParser();
        
        
        try {
            JSONArray jsonData = (JSONArray)parser.parse(new FileReader("resources/2023년3월_서울시_미세먼지.json"));                               
           
            for (Object obj : jsonData) {
                JSONObject data = (JSONObject) obj;
                String currentDate = (String) data.get("날짜");
                String stationName = (String) data.get("측정소명");
                String stationCode = (String) data.get("측정소코드");
                String pm10Str = (String) data.get("PM10");
                String pm25Str = (String) data.get("PM2.5");
                int alertGrade = 0;
                
                int pm10 = Integer.parseInt(pm10Str != null ? pm10Str : "0");
                int pm25 = Integer.parseInt(pm25Str != null ? pm25Str : "0");
                
                int previousPM10Value = previousPM10.getOrDefault(stationCode, 0);//처음은 0으로 설정
                int previousPM25Value = previousPM25.getOrDefault(stationCode, 0);//처음은 0으로 설정

                int averagePM25 = (previousPM25Value + pm25) / 2; //미세먼지 2시간 평균 계산
                int averagePM10 = (previousPM10Value + pm10) / 2; //미세먼지 2시간 평균 계산
                
                
                if (averagePM25 >= 150 ) {
                	alertGrade = 1;
                    DatabaseManager.saveAlertOrder(stationCode, "초미세먼지 경보 발령", currentDate, alertGrade);
                }  else if (averagePM10 >= 300 ) {
                	alertGrade = 2;
                	DatabaseManager.saveAlertOrder(stationCode, "미세먼지 경보 발령", currentDate, alertGrade);
                }else if (averagePM25 >= 75 && averagePM25 < 150) {
                	alertGrade = 3;
                	DatabaseManager.saveAlertOrder(stationCode, "초미세먼지 주의보 발령", currentDate, alertGrade);
                } else if (averagePM10 >= 150 && averagePM10 < 300) {
                	alertGrade = 4;
                	DatabaseManager.saveAlertOrder(stationCode, "미세먼지 주의보 발령", currentDate, alertGrade);
                }

                // 이전 값 업데이트
                previousPM10.put(stationCode, pm10);
                previousPM25.put(stationCode, pm25);

                StationInspection(stationCode,null,currentDate,pm10,pm25);
                
                // 데이터베이스에 측정 데이터 저장
                DatabaseManager.saveData(currentDate, stationName, stationCode, String.valueOf(pm10), String.valueOf(pm25));
            }
            
            //alertGrade가 0이 아니면서 변경될때마다 경보발령 정보를 전송하는 알람 시스템을 구현
            
            
            System.out.println("데이터 입력 완료");
        } catch (IOException  e) {
            e.printStackTrace();
        }
        
    }
	
	
	//측정소 점검 확인 메소드
	public static void StationInspection(String stationCode,String inspection,String inspectionTime,int pm10,int pm25){
		if (pm10 == 0 && pm25 == 0) {
		    inspection="미세먼지와 초미세먼지 측정기가 모두 점검중입니다.";
		    DatabaseManager.saveStationInspection(stationCode, inspection,inspectionTime);
		}

		if (pm10 == 0 && pm25 != 0) {
			inspection="미세먼지 측정기가 점검중입니다.";
			DatabaseManager.saveStationInspection(stationCode, inspection,inspectionTime);
		}

		if (pm10 != 0 && pm25 == 0) {
			inspection="초미세먼지 측정기가 점검중입니다.";
			DatabaseManager.saveStationInspection(stationCode, inspection,inspectionTime);
		}
    }
	
}