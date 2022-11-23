package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Rooms {

	public static final String URL = "jdbc:oracle:thin:" + "@172.30.1.19:1521:xe"; // 주소
	public static final String UID = "COM01"; // db 아이디
	public static final String UPW = "COM01"; // db 패스워드

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	// 객실정보 확인하는 메서드
	public void checkRms() {
		
	}

}
