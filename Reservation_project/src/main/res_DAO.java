package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class res_DAO {

	Scanner scan = new Scanner(System.in);

	public static final String URL = "jdbc:oracle:thin:" + "@172.30.1.19:1521:xe"; // 주소
	public static final String UID = "COM01"; // db 아이디
	public static final String UPW = "COM01"; // db 패스워드

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public ArrayList<RmsVO> showRooms() {

		ArrayList<RmsVO> list = new ArrayList<>();

		String sql = "select * from rooms"; // SQL 문장

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(URL, UID, UPW);
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {

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
		} finally {
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

	public void makeRes() {

		//날짜 입력받을 임시변수 String
		String tmp = "";
		//체크인 날짜 입력받기
		System.out.println("입실하실 날짜를 입력해주세요.(yyyy-mm-dd 형식으로 입력)");
		v_date(tmp);
		String arrDate = tmp;
		//체크아웃 날짜 입력받기
		System.out.println("퇴실하실 날짜를 입력해주세요.(yyyy-mm-dd 형식으로 입력)");
		v_date(tmp);
		String depDate = tmp;
		System.out.println(arrDate);
		System.out.println(depDate);

		// 체크인. 체크아웃 날짜 유효성 검사, 체크아웃이 체크인보다 빠르면 예외발생
		try {
			System.out.println(depDate);
			System.out.println(arrDate);
			LocalDate tmpArr = LocalDate.parse(arrDate);
			LocalDate tmpDep = LocalDate.parse(depDate);
			if (tmpDep.isBefore(tmpArr) || tmpDep.isEqual(tmpArr)) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("체크아웃 날짜를 올바르게 입력해주세요.");
			makeRes();
		}
		
		
		
		//예약가능한 객실을 담을 int 배열
		ArrayList <Integer> avRms = new ArrayList<>();
		//날짜 입력받은 후 예약가능한 객실 보여주기
		ArrayList<availVO> list = showAvailRms(arrDate, depDate);
		for(availVO vo : list) {
			avRms.add(vo.getRoom_num());
			System.out.println(vo);
		}
		
		//예약가능한 객실을 선택
		selectRoom(avRms);
		
		//고객 정보를 입력받기
		
		
		

	}// makeRes 끝
	
	//객실 선택 메서드
	//잘못된 값 입력 시 예외 발생 후 재귀호출하여 다시 입력받음
	public void selectRoom(ArrayList<Integer> avRms) {
		System.out.println("예약하실 객실을 선택하세요.");
		String strtmp = scan.nextLine();
		try {
			int room_num = Integer.parseInt(strtmp);
			boolean check = false;
			for(int i = 0; i < avRms.size(); i++) {
				if(avRms.get(i) == room_num) {
					check = true;
				}
			}
			if(check == false) {
				throw new Exception();
			}
			
			if(!(101<= room_num && room_num <= 104)) {
				throw new Exception();
			}
			
		} catch(Exception e) {
			System.out.println("예약가능한 방 번호만을 입력해주세요.");
			selectRoom(avRms);
			e.getMessage();
		}
	}
	
	public void v_date(String tmp) {
		System.out.println(tmp);
		tmp = scan.nextLine();
		try {
			int m = Integer.parseInt(tmp.substring(5, 7));
			int d = Integer.parseInt(tmp.substring(8));
			if (!(1 <= m && m <= 12)) { // 만약 월을 입력한 부분이 1~12가 아니라면 예외처리
				throw new Exception();
			}
			if (!(1 <= d && d <= 31)) { // 1~31 사이의 값이 아니면 예외처리
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("올바른 날짜를 입력해주세요.");
			v_date(tmp);
		}
	}
	
	//예약되어있는 방을 보여주는 메서드
	public ArrayList<availVO> showAvailRms(String arrDate, String depDate) {
		ArrayList<availVO> list = new ArrayList<>();

		String sql = "select r.room_num, r.room_type, r.room_size, r.price, r.person_max\r\n"
				+ "from rooms r\r\n"
				+ "left join  (select *\r\n"
				+ "            from reservation res\r\n"
				+ "            where arrival_date >= ? \r\n"
				+ "            and arrival_date < ?\r\n"
				+ "            or(arrival_date < ? and departure_date >= ?)) a \r\n"
				+ "on r.room_num = a.room_num\r\n"
				+ "where arrival_date is null"; // SQL 문장

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(URL, UID, UPW);
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, arrDate);
			pstmt.setString(2, depDate);
			pstmt.setString(3, arrDate);
			pstmt.setString(4, depDate);
			
			rs = pstmt.executeQuery();
			
			

			while (rs.next()) {

				int room_num = rs.getInt("room_num");
				String room_type = rs.getString("room_type");
				String room_size = rs.getString("room_size");
				int price = rs.getInt("price");
				int person_max = rs.getInt("person_max");

				availVO vo = new availVO(room_num, room_type, room_size, price, person_max);

				list.add(vo);
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("값 읽어오기 실패");
			e.getMessage();
		} finally {
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

	public void getGstInfo() {
		
		System.out.println("예약하실 분의 성함을 입력하세요.");
		String name = scan.nextLine();
		System.out.println("연락처를 입력하세요.");
		System.out.print("010-");
		String phone_num = scan.nextLine();
		
	}

}// class
