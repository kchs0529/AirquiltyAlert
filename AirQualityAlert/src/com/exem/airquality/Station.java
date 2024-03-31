package com.exem.airquality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Station {
	private Connection conn = null;

	public Station(Connection c) {
		conn = c;
	}

	public String getName(String stationCode)	{
		String name = null;
		try {
			String query = "Select StationName From station Where StationCode = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1,stationCode);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				name = rs.getString(1);
			}
			rs.close();
			pstmt.close(); 	
		} catch (Exception e) {
			System.out.println("station getName:" + e.getMessage());
		}
		return name;
	}
	
	//측정소 추가
	public void insert(String stationCode, String stationName)	{
		try {	
			String insert = "Insert Into Station (StationCode,StationName) Values(?,?)";
			PreparedStatement pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, stationCode);
			pstmt.setString(2, stationName);
			pstmt.execute();
			pstmt.close();	
		} catch (Exception e1) {
			System.out.println("station insert:" + e1.getMessage());
		}
	}	

	//측정소명 수정	
	public void update(String stationCode, String stationName)	{
		try {	
			String update = "Update Station Set StationName = ? Where code = ?";
			PreparedStatement pstmt = conn.prepareStatement(update);
			pstmt.setString(1, stationName);
			pstmt.setString(2, stationCode);
			pstmt.execute();
			pstmt.close();	
		} catch (Exception e1) {
			System.out.println("station update:" + e1.getMessage());
		}
	}	

	//측정소 삭제 중구
	public void delete(String stationCode)	{
		try {	
			String delete = "Delete From Station Where code = ?";
			PreparedStatement pstmt = conn.prepareStatement(delete);
			pstmt.setString(1, stationCode);
			pstmt.execute();
			pstmt.close();	
		} catch (Exception e1) {
			System.out.println("Station delete:" + e1.getMessage());
		}
	}	
}
