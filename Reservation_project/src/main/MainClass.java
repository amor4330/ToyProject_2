package main;

import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
	
	public static final String URL = "jdbc:oracle:thin:" + "@172.30.1.19:1521:xe"; // 주소
	public static final String UID = "COM01"; // db 아이디
	public static final String UPW = "COM01"; // db 패스워드

	public static void main(String[] args) {
		
		res_DAO dao = new res_DAO();
		Scanner scan = new Scanner(System.in);
		int menu = 0;
		boolean flag = true;

		// 메뉴 선택
		while (flag) {
			clear();
			System.out.println("===============================");
			System.out.println("          Language 호텔         ");
			System.out.println("===========  MENU  ============");
			System.out.println("1, 객실 2, 예약 3, 예약조회 4, 종료");
			System.out.println("===============================");

			// 잘못된 값을 입력했을 때 돌아가는 기능
			try {
				String tmp = scan.nextLine();
				menu = Integer.valueOf(tmp);
				if (menu != 486 && menu > 4) {
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("올바른 번호를 입력해주세요.");
				continue;
			}

			switch (menu) {
			case 1:
				System.out.println("객실 정보");
				ArrayList<RmsVO> list = dao.showRooms();
				for(RmsVO vo : list) {
					System.out.println(vo);
				}
				flag = backToMenu();
				break;

			case 2:
				System.out.println("예약");
				res_DAO dao2 = new res_DAO();
				dao2.makeRes();
				flag = backToMenu();
				break;

	         case 3:
	             dao.reservationCheck();
	             flag = backToMenu();
	             break;
			
	         case 4:
	             System.out.println("종료합니다.");
	             flag = false;
	             break;
	          case 486: //관리자모드

	             ArrayList<resVO> list1 = dao.res_list();
	             System.out.println("<전체 예약 정보>");
	             System.out.println("-----------------------------------------------------------------------");
	             System.out.println(" 예약번호 | 예약자번호 | 예약자이름 | 룸번호 | 예약인원 | 체크인날짜 | 체크아웃날짜 | 결제방법 ");
	             System.out.println("-----------------------------------------------------------------------");
	             for(resVO vo1 : list1) {
	                System.out.println(vo1);
	             }
	             System.out.println("-----------------------------------------------------------------------");
	             System.out.println();

	             ArrayList<TodayVO> list2 = dao.today_list();
	             System.out.println("<오늘 예약 정보>");
	             System.out.println("-----------------------------------------------------------------------");
	             System.out.println(" 예약번호 | 예약자번호 | 예약자이름 | 룸번호 | 예약인원 | 체크인날짜 | 체크아웃날짜 | 결제방법 ");
	             System.out.println("-----------------------------------------------------------------------");
	             if(list2.isEmpty()) {
	                System.out.println("금일 예약 정보가 없습니다.");
	             } else {
	                for(TodayVO vo2 : list2) {
	                   System.out.println(vo2);
	                }
	             }
	             System.out.println("-----------------------------------------------------------------------");
	             System.out.println();
	             flag = backToMenu();
	             break;
	          default:
	             flag = false;
	             break;
	          }

		} // while

	}// main

	static void clear() {
		for (int i = 0; i < 30; i++) {
			System.out.println();
		}
	}
	static boolean backToMenu() {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("메뉴로 돌아가시겠습니까? 1 : 예, 2 : 아니오");
		try {
			Scanner scan = new Scanner(System.in);
			String tmp = scan.nextLine();
			if(Integer.valueOf(tmp) != 1) {
				System.out.println("종료합니다.");
				return false;
			}else {
				clear();
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
}
