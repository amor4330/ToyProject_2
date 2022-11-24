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
			
		} catch (Exception e) { //숫자가 아닌 다른 문자 입력받으면 오류
			System.out.println("숫자만 입력해주세요");
		}
		
		//reservation, guest, rooms 테이블을 조인하여 원하는 데이터 선택
		//예약번호 부분은 ? 표시
		String sql = "select r.reservation_num, -- 예약번호\r\n"
				+ "          to_char(r.arrival_date, 'yyyy-mm-dd') as arrDate,\r\n"
				+ "          to_char(r.departure_date, 'yyyy-mm-dd') as depDate,\r\n"
				+ "          r.room_num,\r\n"
				+ "          ro.room_type,\r\n"
				+ "          r.numofpeople,\r\n"
				+ "          r.payment,\r\n"
				+ "          ro.price,\r\n"
				+ "          g.name,\r\n"
				+ "			 g.phone_num,\r\n"
				+ "          g.email\r\n"
				+ "from reservation r\r\n"
				+ "join rooms ro\r\n"
				+ "on  r.room_num = ro.room_num\r\n"
				+ "join guest g\r\n"
				+ "on r.guest_id = g.guest_id\r\n"
				+ "where reservation_num = ?";
		
		// throws exception을 던지니까 try-catch
		try {
			//드라이버 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//connection
			conn = DriverManager.getConnection(URL, UID, UPW);
			
			//statement 객체생성후 쿼리문을 pstmt에 저장
			pstmt = conn.prepareStatement(sql);
			
			//값 세팅 - 입력받은 객실 번호
			pstmt.setInt(1, resnum2);
			
			rs = pstmt.executeQuery(); //pstmt의 쿼리를 실행하여 ResultSet rs에 넣음

				
			int count = 0; //데이터가 있는지 확인하는 변수
			
			// sql문 실행
			while(rs.next()) {
				int reservation_num = rs.getInt("reservation_num");
				String arrDate = rs.getString("arrDate");
				String depDate = rs.getString("depDate");
				int room_num = rs.getInt("room_num");
				String room_type = rs.getString("room_type");
				int numofpeople = rs.getInt("numofpeople");
				String payment = rs.getNString("payment");
				int price = rs.getInt("price");
				String name = rs.getString("name");
				String phone_num = rs.getString("phone_num");
				String email = rs.getString("email");
				
				System.out.println("===========  예약 조회 ============");
				System.out.println("============ 객실 정보 ============");
				System.out.printf("예약번호: %d%n"
								+ "체크인날짜: %s%n"
								+ "체크아웃날짜: %s%n"
								+ "객실번호: %d%n"
								+ "객실타입: %s%n"
								+ "인원수: %d인%n"
								+ "결제방법: %s%n"
								+ "가격: %d원%n"
						        ,reservation_num, arrDate, depDate, room_num, room_type, numofpeople, payment, price);
				System.out.println("=========== 예약자 정보 ============");
				System.out.printf("이름: %s%n"
								+ "전화전호: %s%n"
								+ "이메일: %s%n"
								,name, phone_num, email);
				count = 1; 
				}
			
			
			try {
				if(count != 1) { //데이터 값이 있으면
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("등록되지 않은 예약번호 입니다.");
				reservationCheck();
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