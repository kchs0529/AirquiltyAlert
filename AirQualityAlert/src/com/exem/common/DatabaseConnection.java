package com.exem.common;

import java.sql.Connection;

public class DatabaseConnection {
	private Connection conn = null;

	public DatabaseConnection() {
	}

	public Connection getConnection() throws Exception {
		if (conn == null) {
			//Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/airquality";
			conn = java.sql.DriverManager.getConnection(url,"root","1234");
		}
		return conn;
	}

	public void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			System.out.println("close:" + e.getMessage());
		}
	}
	
	public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        try {
            Connection conn = dbConnection.getConnection();
            if (conn != null) {
                System.out.println("연결 성공");
                dbConnection.close(); // 연결 닫기
            } else {
                System.out.println("연결 실패");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while connecting to database: " + e.getMessage());
        }
    }
}
