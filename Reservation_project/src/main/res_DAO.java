package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

		//체크인 날짜 입력받기
		System.out.println("입실하실 날짜를 입력해주세요.(yyyy-mm-dd 형식으로 입력)");
		String arrDate = v_date();
		//체크아웃 날짜 입력받기
		System.out.println("퇴실하실 날짜를 입력해주세요.(yyyy-mm-dd 형식으로 입력)");
		String depDate = v_date();

		// 체크인. 체크아웃 날짜 유효성 검사, 체크아웃이 체크인보다 빠르면 예외발생
		try {
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
		int room_num = selectRoom(avRms);
		
		//고객 정보를 입력받기
			//이름 입력
		System.out.println("예약하실 분의 성함을 입력하세요.");
		String name = scan.nextLine();
			//연락처 유효성 검사 후 입력
		String phone_num = v_phoneNum();
			//이메일 유효성 검사 후 입력
		String email = v_email();
			//국적 입력
		System.out.println("국적을 입력하세요.");
		String nation = scan.nextLine();
			//숙박 인원 수 입력받기
		int numOfPeople = v_numpeople(room_num);
			//결제방법 입력받기
		System.out.println("결제방법을 선택하세요. 현금/카드");
		String payment = scan.nextLine();
		
		insertGuest(name, phone_num, email, nation, 
				room_num, numOfPeople, arrDate, depDate, payment);
		
		
		

	}// makeRes 끝
	
	//객실 선택 메서드 - 잘못된 값 입력 시 예외 발생 후 재귀호출하여 다시 입력받음
		public int selectRoom(ArrayList<Integer> avRms) {
		int room_num = 0;
		System.out.println("예약하실 객실을 선택하세요.");
		String strtmp = scan.nextLine();
		try {
			room_num = Integer.parseInt(strtmp);
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
		
		return room_num;
	}
	
	//유효성 검사 메서드들
		//예약 날짜 유효성 검사 메서드
		public String v_date() {
			String tmp = scan.nextLine();
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
				v_date();
			}
			
			return tmp;
		}
		//연락처 유효성 검사 메서드
		public String v_phoneNum() {
			System.out.println("연락처를 입력하세요. ex)010-0000-0000");
			System.out.print("010-");
			String phone_num = scan.nextLine();
			try {
				int length = phone_num.length();
				if(length == 8) {
					if(phone_num.charAt(3) != '-') {
						throw new Exception();
					}
				}else if(length == 9) {
					if(phone_num.charAt(4) != '-') {
						throw new Exception();
					}
				}else {
					throw new Exception();
				}
			}catch (Exception e) {
				System.out.println("올바른 전화번호 양식을 입력하세요.");
				v_phoneNum();
			}
			return phone_num;
		}
		//이메일 유효성 검사 메서드
		public String v_email() {
			System.out.println("이메일을 입력하세요.(gmail, naver, kakao만 허용");
			String email = scan.nextLine();
			try {
				String[] pattern = {"@naver.com", "@gmail.com", "@kakao.com"};
				
				for(int i = 0; i < pattern.length; i++) {
					email = email.replace(pattern[i], "1");
					if(email.indexOf("1") != email.length()-1) {
						throw new Exception();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("올바른 이메일을 입력하세요.");
				v_email();
			}
			return email;
		}
		//인원선택 유효성 검사 메서드
		public int v_numpeople(int room_num) {
		int numOfPeople = 0;
		try {
			System.out.println("투숙하실 인원을 선택하세요");
			String tmp = scan.nextLine();
			numOfPeople = Integer.valueOf(tmp);
			if(room_num <=103 && numOfPeople > 3) {
				System.out.println("투숙 가능한 인원을 초과하였습니다.");
				throw new Exception();
			}else if(room_num == 104 && numOfPeople > 4){
				System.out.println("투숙 가능한 인원을 초과하였습니다.");
				throw new Exception();
			}
		} catch (NumberFormatException e1) {
			System.out.println("문자가 아닌 숫자로 입력하세요.");
			v_numpeople(room_num);
		}catch (Exception e2) {
			v_numpeople(room_num);
		}
		return numOfPeople;
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
	
	//Guest 테이블 insert
		public void insertGuest(String name, String phone_num, String email, String nation
							,int room_num, int numOfPeople, String arrDate, String depDate, String payment){

			String sql = "insert into guest "
					+ "values (lpad(guest_seq.nextval, 5, '0') as guest_id,"
					+ " ?, ?, ?, ?)";
			
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(URL, UID, UPW);
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, name);
				pstmt.setString(2, phone_num);
				pstmt.setString(3, email);
				pstmt.setString(4, nation);
				
				int result = pstmt.executeUpdate();
				
				
				if(result == 1) {
					System.out.println("예약이 생성되었습니다.");
				}
				
			} catch (Exception e) {
				e.getMessage();
			} 
			
			String sql2 = "insert into reservation "
					+ "values (reservation_seq.nextval, "
					+ "lpad(guest_seq.currval, 5, '0'), "
					+ "?, ?, ?, ?, ?)";
			
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(URL, UID, UPW);
				pstmt = conn.prepareStatement(sql2);
				
				pstmt.setInt(1, room_num);
				pstmt.setInt(2, numOfPeople);
				pstmt.setString(3, arrDate);
				pstmt.setString(4, depDate);
				pstmt.setString(5, payment);
				
				int result = pstmt.executeUpdate();
				
				if(result == 1) {
					System.out.println("예약정보 업데이트");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				e.getMessage();
			}finally {
				try {
					conn.close();
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		
			
		}

	//Reservaion 테이블 insert
		public void insertRes(int room_num, int numOfPeople, String arrDate, String depDate, String payment) {
			String sql = "insert into reservation "
					+ "values (reservation_seq.nextval, "
					+ "lpad(guest_seq.currval, 5, '0'), "
					+ "?, ?, ?, ?, ?)";
			
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(URL, UID, UPW);
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, room_num);
				pstmt.setInt(2, numOfPeople);
				pstmt.setString(3, arrDate);
				pstmt.setString(4, depDate);
				pstmt.setString(5, payment);
				
				pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
			
		}
	//Reserved room 테이블 insert 
		public void insertResRm(String arrDate, String depDate, int room_num) {
			String sql = "insert into reserved_room "
					+ "values (?, ?, ?)";
			
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(URL, UID, UPW);
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, arrDate);
				pstmt.setString(3, depDate);
				pstmt.setInt(2, room_num);
				
				pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}


}// class
