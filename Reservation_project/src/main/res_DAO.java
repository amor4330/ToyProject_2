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
	
	
	public ArrayList<RmsVO> select(){
		ArrayList<RmsVO> list = new ArrayList<>();
		
		String sql = "select * from rooms"; //SQL 문장
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(URL, UID, UPW);
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				int room_num = rs.getInt("room_num");
				String room_type = rs.getString("room_type");
				String room_size = rs.getString("room_size");
				int price = rs.getInt("price");
				int person_max = rs.getInt("person_max");
				
				RmsVO vo = new RmsVO(room_num, room_type, room_size, price, person_max);
				
				list.add(vo);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("값 읽어오기 실패");
			e.getMessage();
		}finally {
			try {
				conn.close();
				pstmt.close();
				rs.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.getMessage();
			}
		}
	
		
		return list;
	}
}
