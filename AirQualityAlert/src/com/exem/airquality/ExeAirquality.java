package com.exem.airquality;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.exem.common.DatabaseConnection;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ExeAirquality {
	
	public static void main(String[] args) throws Exception {
        JSONParser parser = new JSONParser();
        DatabaseConnection dbc = new DatabaseConnection();
        
        try {
        	Connection conn = dbc.getConnection();
        	DustData dd = new DustData(conn);
        	dd.setHook(false);
        	
            JSONArray jsonData = (JSONArray)parser.parse(new FileReader("resources/2023년3월_서울시_미세먼지.json"));                               
           
            for (Object obj : jsonData) {
                JSONObject data = (JSONObject) obj;
                String date = (String) data.get("날짜");
                String stationName = (String) data.get("측정소명");
                String stationCode = (String) data.get("측정소코드");
                String checkDay = date.substring(0,10);
				int checkHour = Integer.parseInt(date.substring(11,13));
                String pm10Str = (String) data.get("PM10");
                String pm25Str = (String) data.get("PM2.5");
                
                int pm10 = Integer.parseInt(pm10Str != null ? pm10Str : "0");
                int pm25 = Integer.parseInt(pm25Str != null ? pm25Str : "0");

                dd.insertDustData(stationCode,stationName,date, checkDay, checkHour, pm10, pm25);
                
            }
            
            System.out.println("데이터 입력 완료");
            
        } catch (IOException  e) {
            e.printStackTrace();
        }finally {
			dbc.close();
		}
        
    }
	
}
