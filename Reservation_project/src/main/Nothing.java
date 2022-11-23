package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Nothing {

	public static void main(String[] args) {
		
		String URL = "jdbc:oracle:thin:@172.30.1.19:1521:xe"; // 주소
		String UID = "COM01"; // db 아이디
		String UPW = "COM01"; // db 패스워드

		
		String sql = "select * from guest";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		System.out.println("test1");
		try {
			System.out.println("test2");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("test3");
			
			
			conn = DriverManager.getConnection(URL, UID, UPW);
			System.out.println("test4");
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("test");
			
			while(rs.next()) {
				String guest_num = rs.getString("guest_num");
				String name = rs.getString("name");
				String phone_num = rs.getString("phone_num");
				String email = rs.getString("email");
				String nation = rs.getString("nation");
				
				System.out.printf("회원번호: %s, 이름: %s, 전화번호: %s, 이메일: %s, 국적: %s%n",
						guest_num, name, phone_num, email, nation);
			}
			
			
			
		} catch (Exception e) {
			e.getLocalizedMessage();
		}finally {
			try {
				conn.close();
				pstmt.close();
				rs.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		
	}
}
