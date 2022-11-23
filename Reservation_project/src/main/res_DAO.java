package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class res_DAO {

	Scanner scan = new Scanner(System.in);
	
	public static final String URL = "jdbc:oracle:thin:" 
						+ "@172.30.1.19:1521:xe"; // 주소
	public static final String UID = "COM01"; //db 아이디
	public static final String UPW = "COM01"; //db 패스워드
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	
	public ArrayList<RmsVO> showRooms(){
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

	//예약 조회 메서드
	public void reservationCheck() {
		
		System.out.println("예약 번호 입력> ");
		String tmp = scan.nextLine();
		
		int resnum2 = 0;
		try {
			resnum2 = Integer.parseInt(tmp); //예약번호를 String으로 입력받아 int형 변수에 저장
			
		} catch (Exception e) {
			System.out.println("숫자만 입력해주세요");
		}
		
		
		String sql = "select r.reservation_num,\r\n"
				+ "          g.name,\r\n"
				+ "          g.phone_num,\r\n"
				+ "          g.email,\r\n"
				+ "          g.nation,\r\n"
				+ "          r.room_num,\r\n"
				+ "          r.numofpeople,\r\n"
				+ "          to_char(r.arrival_date, 'yyyy-mm-dd') as arrDate,\r\n"
				+ "          to_char(r.departure_date, 'yyyy-mm-dd') as depDate,\r\n"
				+ "          r.payment\r\n"
				+ "from guest g\r\n"
				+ "inner join ((select *\r\n"
				+ "                from reservation\r\n"
				+ "                where reservation_num = ?) r)\r\n"
				+ "on r.guest_id = g.guest_id"; // sql문
		// throws exception을 던지니까 try-catch
		try {
			//드라이버 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//connection
			conn = DriverManager.getConnection(URL, UID, UPW);
			
			//statement 객체
			pstmt = conn.prepareStatement(sql);
			
			//값 세팅 - 입력받은 객실 번호
			pstmt.setInt(1, resnum2);
			
			try {
				rs = pstmt.executeQuery();
				if(rs.wasNull()) {
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("등록되지 않은 예약번호 입니다.");
				reservationCheck();
			}
			
			
			// sql문 실행
			while(rs.next()) {
				System.out.println("2");
				int reservation_num = rs.getInt("reservation_num");
				String name = rs.getString("name");
				String phone_num = rs.getString("phone_num");
				String email = rs.getString("email");
				String nation = rs.getString("nation");
				int room_num = rs.getInt("room_num");
				int numofpeople = rs.getInt("numofpeople");
				String arrDate = rs.getString("arrDate");
				String depDate = rs.getString("depDate");
				String payment = rs.getNString("payment");
				
				System.out.println("===========  예약 조회 ============");
				System.out.println("=========== 예약자 정보 ============");
				System.out.printf("예약번호: %d%n"
								+ "이름: %s%n"
								+ "전화전호: %s%n"
								+ "이메일: %s%n"
								+ "국적: %s%n"
								,reservation_num, name, phone_num, email, nation);
				System.out.println("============ 객실 정보 ============");
				System.out.printf("객실 번호: %d%n"
						+ "인원수: %d명%n"
						+ "예약날짜: %s%n"
						+ "퇴실날짜: %s%n"
						+ "결제방법: %s%n"
						,room_num, numofpeople, arrDate, depDate, payment);
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try { //객체 계속 쌓여서 close 필수로 해줌
				conn.close();
				pstmt.close();
				rs.close();
			} catch (Exception e2) {
				
			}
		
		}
	}// 메서드 끝

	
	
	
	
}