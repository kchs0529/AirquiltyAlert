package com.exem.airquality;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.IOException;

public class ReadJSON {
	public static void main(String[] args) throws Exception {
        JSONParser parser = new JSONParser();
        
        try {
            // JSON 파일에서 데이터 읽어오기
            JSONArray jsonData = (JSONArray)parser.parse(new FileReader("resources/2023년3월_서울시_미세먼지.json"));                               
           
            for (Object obj : jsonData) {
                JSONObject data = (JSONObject) obj;
                String currentDate = (String) data.get("날짜");
                String stationName = (String) data.get("측정소명");
                String stationCode = (String) data.get("측정소코드");
                String pm10 = (String) data.get("PM10");
                String pm25 = (String) data.get("PM2.5");
                
                if (pm10 == null) {
                    pm10 = "0"; // 또는 다른 기본값으로 설정
                }
                if (pm25 == null) {
                    pm25 = "0"; // 또는 다른 기본값으로 설정
                }
                
                DatabaseManager.saveData(currentDate, stationName, stationCode, pm10, pm25);
            }
            
            
            
            System.out.println("데이터 입력완료");
        } catch (IOException  e) {
            e.printStackTrace();
        }
        
    }
	
}
