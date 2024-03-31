package com.exem.airquality;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class DustData {

	private Connection conn = null;
	boolean hook = true;
	public DustData(Connection c) {
		conn = c;
	}

	public void setHook(boolean h) {
		hook = h;
	}
	
	
	public void insertDustData(String stationCode,String stationName,String date, String checkDay, int checkHour, int pm10, int pm25)	{
		try {
			Station station = new Station(conn);
			if(station.getName(stationCode)==null) {
				station.insert(stationCode,stationName);
			}
			
			
			String insertStationData = "Insert Into dust_data (StationCode, check_Day, check_Hour, pm10, pm25) Values(?,?,?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(insertStationData);
			pstmt.setString(1, stationCode);
			pstmt.setString(2, checkDay);
			pstmt.setInt(3, checkHour);
			pstmt.setInt(4, pm10);
			pstmt.setInt(5, pm25);
			pstmt.execute();
			pstmt.close();	

			if ((pm10 == 0)&&(pm25 == 0)) {
				CheckStation cs = new CheckStation(conn);
				cs.check(stationCode, checkDay, checkHour);
			}

			int[] preData = getPreData(stationCode, checkDay, checkHour);
			int grade = getGrade(pm10, pm25, preData);

			if (grade != 0) {	// 미세먼지 주의보 등급이 있으면
				int id = getData(stationCode, checkDay, checkHour)[2];
				DustAlert da = new DustAlert(conn);
				da.insert(id,date, grade);
				setHook(true);
				if (hook) { 
					da.sendMessage(stationCode, checkDay, checkHour,grade);
				}
			}	

		} catch (Exception e) {
			System.out.println("dust_data insert:" + e.getMessage());
		}
	}	

	//한시간 전의 미세먼지 수치
	public int[] getPreData(String stationCode, String checkDay, int checkHour)	{
		int preHour = checkHour - 1;
		if (preHour == 0) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(checkDay.substring(0,4)), Integer.parseInt(checkDay.substring(5,7)), Integer.parseInt(checkDay.substring(8,10)));
			cal.add(Calendar.DATE, -1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			checkDay = sdf.format(cal.getTime());
			preHour = 24;
		}
		int[] data = getData(stationCode, checkDay, preHour);
		return data;
	}//Calendar class를 사용하여 계산
		
	
	//미세먼지 데이터 수치
	public int[] getData(String stationCode, String checkDay, int checkHour)	{
		int[] data = {0,0,0};	// pm10, pm2.5, id 기본값
		try {
			String query = "Select pm10, pm25, id From dust_data Where StationCode = ? and check_Day = ? and check_Hour = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1,stationCode);
			pstmt.setString(2,checkDay);
			pstmt.setInt(3,checkHour);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				data[0] = rs.getInt(1);
				data[1] = rs.getInt(2);
				data[2] = rs.getInt(3);
			}
			rs.close();
			pstmt.close(); 	
		} catch (Exception e1) {
			System.out.println("dust_data getData:" + e1.getMessage());
		}
		return data;
	}	

	//경보 등급
	public int getGrade(int pm10, int pm25, int[] preData)	{
		int grade = 0;
		try {
			if ((pm25 >= 150)&&(preData[1] >= 150))
				grade = 1;
			else if ((pm10 >= 300)&&(preData[0] >= 300))
				grade = 2;
			else if ((pm25 >= 75)&&(preData[1] >= 75))
				grade = 3;
			else if ((pm10 >= 150)&&(preData[0] >= 150))
				grade = 4;
		} catch (Exception e1) {
			System.out.println("dust_data getGrade:" + e1.getMessage());
		}
		return grade;
	}
}

