package com.exem.airquality;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DustAlert {

	private Connection conn = null;

	public DustAlert(Connection c) {
		conn = c;
	}

	//경보 발령 정보 저장
	public void insert(int dustId,String date, int grade)	{
		try {	
			String insert = "Insert Into dust_alert (dust_id, dust_grade, alert_time) Values(?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(insert);
			pstmt.setInt(1, dustId);
			pstmt.setInt(2, grade);
			pstmt.setString(3,date);
			pstmt.execute();
			pstmt.close();	
		} catch (Exception e1) {
			System.out.println("dust_alert insert:" + e1.getMessage());
		}
	}	

	//메세지 전송
	public void sendMessage(String stationCode,String Day,int hour, int grade)	{
		Station station = new Station(conn);
		AlertData alertData = new AlertData(conn);
		String stationName = station.getName(stationCode);
		String dust_alert = alertData.getAlert(grade);
		String msg = Day+" "+hour + "시에 "+stationName+"에 ";
		if(grade==1)
			msg = msg+dust_alert+"가 발령되었습니다. 건강에 매우 해로우니 주의하시기 바랍니다";
		else if(grade==2)
			msg = msg+dust_alert+"가 발령되었습니다. 건강에 매우 해로울수 있으니 주의하시기 바랍니다";
		else if(grade==3)
			msg = msg+dust_alert+"가 발령되었습니다. 건강에 해로울수 있으니 주의하시기 바랍니다";
		else if(grade==4)
			msg = msg+dust_alert+"가 발령되었습니다. 건강에 약간 해로울수 있으니 주의하시기 바랍니다";
		System.out.println(msg);
		// 제시된 외부 API가 없어 localhost/jsp/Msg.jsp에 parameter로 보냄
		try {
			URL url = new URL("https://localhost/jsp/Msg.jsp");	
			HttpURLConnection con = (HttpURLConnection)url.openConnection();;
			con.setRequestMethod("POST");
			String param = "stationCode="+stationCode+"&StaionName="+stationName+"&msg="+msg;

			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			dos.writeBytes(param);
			dos.flush();
			dos.close();
	
			int responseCode = con.getResponseCode();
			if(responseCode==200) {     // 정상 호출
				StringBuffer sb = new StringBuffer();
				sb.setLength(0);
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
			}
			con.disconnect();

		} catch (Exception e) {
			System.out.println("dust_alert sendMessage:" + e.getMessage());
		}
	}	

}
