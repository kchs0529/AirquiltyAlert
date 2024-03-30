package com.exem.airquality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckStation {
	private Connection conn = null;

	public CheckStation(Connection c) {
		conn = c;
	}


	//점검시간 등록
	public void check(String stationCode, String chkDay, int chkHour)	{
		if (!existCheckDate(stationCode, chkDay, chkHour)) {
			insert(stationCode, chkDay, chkHour);
		} else {
			update(stationCode, chkDay, chkHour);
		}
	}

	
	//한시간전에 점검한내역이 없는경우 점검시작시간과 종료시간을 현재시간으로 설정
	public void insert(String spotCode, String checkDay, int checkHour)	{
		try {	
			String insert = "Insert Into check_station (stationCode, check_startDate, check_endDate) Values(?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(insert);
			String date = checkDay+" "+String.format("%02d", checkHour);
			pstmt.setString(1, spotCode);
			pstmt.setString(2, date);
			pstmt.setString(3, date);
			pstmt.execute();
			pstmt.close();	
		} catch (Exception e1) {
			System.out.println("check_station insert:" + e1.getMessage());
		}
	}	

	
	//한시간전에 점검한내역이 있는경우 점검종료시간을 현재시간으로 설정
	public void update(String stationCode, String checkDay, int checkHour)	{
		try {	
			String insert = "Update check_station Set check_endDate = ? Where StationCode = ? and check_endDate = ?";
			PreparedStatement pstmt = conn.prepareStatement(insert);
			String date = checkDay+" "+String.format("%02d", checkHour);
			pstmt.setString(1, date);
			pstmt.setString(2, stationCode);
			pstmt.setString(3, getPreDate(checkDay, checkHour));
			pstmt.execute();
			pstmt.close();	
		} catch (Exception e) {
			System.out.println("check_station update:" + e.getMessage());
		}
	}

	//한시간 전 시간 계산
	public String getPreDate(String checkDay, int checkHour) {
		int preHour = checkHour - 1;
		if (preHour == 0) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(checkDay.substring(0,4)), Integer.parseInt(checkDay.substring(5,7)), Integer.parseInt(checkDay.substring(8,10)));
			//년 월 일 설정
			cal.add(Calendar.DATE, -1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			checkDay = sdf.format(cal.getTime());
			preHour = 24;
		}//preHour이 0이면 전날의 24시로 설정
		String preDate = checkDay + " " + String.format("%02d", preHour);
		return preDate;
	}
	
	//한시간 전에 점검을 했는지 확인
	public boolean existCheckDate(String stationCode, String checkDay, int checkHour) {
		boolean exist = false;
		try {
			String query = "Select 1 From check_station Where StationCode = ? and check_endDate = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1,stationCode);
			pstmt.setString(2,getPreDate(checkDay, checkHour));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				exist = true;
			}
			rs.close();
			pstmt.close(); 	
		} catch (Exception e) {
			System.out.println("check_station existCheckDate:" + e.getMessage());
		}
		return exist;
	}	
}
