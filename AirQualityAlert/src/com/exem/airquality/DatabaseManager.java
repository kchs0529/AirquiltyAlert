package com.exem.airquality;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/airquality");
        config.setUsername("root");
        config.setPassword("1234");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // 기타 설정들을 필요에 따라 추가할 수 있습니다.

        dataSource = new HikariDataSource(config);
    }

    public static void saveData(String currentDate, String stationName, String stationCode, String pm10, String pm25) {
        try (Connection conn = dataSource.getConnection()) {
            // 측정소 정보 저장
            String stationQuery = "INSERT INTO station (StationCode, StationName) VALUES (?, ?) "
                                + "ON DUPLICATE KEY UPDATE StationName = VALUES(StationName)";
            PreparedStatement stationStatement = conn.prepareStatement(stationQuery);
            stationStatement.setString(1, stationCode);
            stationStatement.setString(2, stationName);
            stationStatement.executeUpdate();
            
            // 측정 데이터 저장
            String dataQuery = "INSERT INTO stationData (currentDate, StationCode, PM10, PM25) VALUES (?, ?, ?, ?)";
            PreparedStatement dataStatement = conn.prepareStatement(dataQuery);
            dataStatement.setString(1, currentDate);
            dataStatement.setString(2, stationCode);
            dataStatement.setString(3, pm10);
            dataStatement.setString(4, pm25);
            dataStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveAlertOrder(String stationCode, String alertLevel, String orderTime, int alertGrade) {
        try (Connection conn = dataSource.getConnection()) {
            String query = "INSERT INTO AlertOrder (stationCode, alert_level, alert_time, alert_grade) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, stationCode);
            statement.setString(3, alertLevel);
            statement.setString(4, orderTime);
            statement.setInt(5, alertGrade);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveStationCheck(String stationNum, String checkTime) {
        try (Connection conn = dataSource.getConnection()) {
            String query = "INSERT INTO StationCheck (stationNum, check_time) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, stationNum);
            statement.setString(2, checkTime);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


//package com.exem.airquality;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class DatabaseManager {
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/airquality";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "1234";
//    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
//
//    static {
//        try {
//            Class.forName(JDBC_DRIVER);
//        } catch (ClassNotFoundException e) {
//            System.err.println("JDBC 드라이버 로드 실패");
//            e.printStackTrace();
//        }
//    }
//    
//    //서울시 3월 데이터를 전부 저장
//    public static void saveData(String currentDate, String stationName, String stationCode, String pm10, String pm25) {
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//            
//            // 측정소 정보 저장
//            String stationQuery = "INSERT INTO station (StationCode, StationName) VALUES (?, ?) "
//                                + "ON DUPLICATE KEY UPDATE StationName = VALUES(StationName)";
//            					//측정소 코드가 이미 테이블에 존재하면 측정소 코드
//            PreparedStatement stationStatement = conn.prepareStatement(stationQuery);
//            stationStatement.setString(1, stationCode);
//            stationStatement.setString(2, stationName);
//            stationStatement.executeUpdate();
//            
//            // 측정 데이터 저장
//            String dataQuery = "INSERT INTO stationData (currentDate, StationCode, PM10, PM25) VALUES (?, ?, ?, ?)";
//            PreparedStatement dataStatement = conn.prepareStatement(dataQuery);
//            dataStatement.setString(1, currentDate);
//            dataStatement.setString(2, stationCode);
//            dataStatement.setString(3, pm10);
//            dataStatement.setString(4, pm25);
//            dataStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void saveAlertOrder(String stationCode, String alertLevel, String orderTime, int alertGrade) {
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//            String query = "INSERT INTO AlertOrder (stationCode, alert_level, alert_time, alert_grade) VALUES (?, ?, ?, ?)";
//            PreparedStatement statement = conn.prepareStatement(query);
//            statement.setString(1, stationCode);
//            statement.setString(3, alertLevel);
//            statement.setString(4, orderTime);
//            statement.setInt(5, alertGrade);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void saveStationCheck(String stationNum, String checkTime) {
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//            String query = "INSERT INTO StationCheck (stationNum, check_time) VALUES (?, ?)";
//            PreparedStatement statement = conn.prepareStatement(query);
//            statement.setString(1, stationNum);
//            statement.setString(2, checkTime);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}
