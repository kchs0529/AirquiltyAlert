package com.exem.airquality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlertData {
	private Connection conn = null;

	public AlertData(Connection c) {
		conn = c;
	}
	public String getAlert(int grade) {
		String dust_alert = null;
		
		try {
			String query = "select dust_alert from alert_data where dust_grade=?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1,grade);
			ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // 결과에서 dust_alert 컬럼의 값을 가져와서 dust_alert 변수에 할당
	            dust_alert = rs.getString("dust_alert");
	        }
	        rs.close();
	        pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dust_alert;
	}
}
