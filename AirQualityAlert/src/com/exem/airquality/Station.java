package com.exem.airquality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Station {
	private Connection conn = null;

	public Station(Connection c) {
		conn = c;
	}

	public String getName(String spotCode)	{
		String name = null;
		try {
			String query = "Select StationName From station Where StationCode = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1,spotCode);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				name = rs.getString(1);
			}
			rs.close();
			pstmt.close(); 	
		} catch (Exception e1) {
			System.out.println("station getName:" + e1.getMessage());
		}
		return name;
	}	
}
