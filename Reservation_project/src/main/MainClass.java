package main;

import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
	
	public static final String URL = "jdbc:oracle:thin:" + "@172.30.1.19:1521:xe"; // 주소
	public static final String UID = "COM01"; // db 아이디
	public static final String UPW = "COM01"; // db 패스워드

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		int menu = 0;
		boolean flag = true;

		// 메뉴 선택
		while (flag) {
			System.out.println("===============================");
			System.out.println("          Language 펜션         ");
			System.out.println("===========  MENU  ============");
			System.out.println("1, 객실 2, 예약 3, 예약조회 4, 종료");
			System.out.println("===============================");

			// 잘못된 값을 입력했을 때 돌아가는 기능
			try {
				String tmp = scan.nextLine();
				menu = Integer.valueOf(tmp);
				if (menu > 4) {
					throw new Exception();
				}
			} catch (Exception e) {
				clear();
				System.out.println("올바른 번호를 입력해주세요.");
				continue;
			}

			switch (menu) {
			case 1:
				System.out.println("객실 정보");
				res_DAO dao = new res_DAO();
				ArrayList<RmsVO> list = dao.select();
				for(RmsVO vo : list) {
					System.out.println(vo);
				}
				break;

			case 2:
				System.out.println("예약");
				break;

			case 3:
				System.out.println("예약 조회");
				break;
			
			case 4:
				System.out.println("종료");
				
			case 486: //관리자모드
				
			default:
				
				flag = false;
				break;
			}

		} // while

	}// main

	static void clear() {
		for (int i = 0; i < 15; i++) {
			System.out.println();
		}
	}
}
