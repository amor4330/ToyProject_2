package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class res_DAO {


	public static final String URL = "jdbc:oracle:thin:" 
						+ "@172.30.1.19:1521:xe"; // 주소
	public static final String UID = "COM01"; //db 아이디
	public static final String UPW = "COM01"; //db 패스워드
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	
	public ArrayList<res_VO> select(){
		ArrayList<res_VO> vo = new ArrayList<>();
		
		return vo;
	}
	
	//객실정보 확인 메서드
	public void checkRms() {
		
		String sql = "select * from rooms"; //SQL 문장
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(URL, UID, UPW);
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("값 읽어오기 실패");
			e.getMessage();
		}
		
	}
	
}
