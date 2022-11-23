package main;

import java.util.Scanner;

public class MainClass {
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		int menu = 0;
		boolean flag = true;
		
		//메뉴 선택
		while(flag) {
			System.out.println("===============================");
			System.out.println("          Language 펜션         ");
			System.out.println("===========  MENU  ============");
			System.out.println("1, 객실 2, 예약 3, 예약조회 4, 종료");
			System.out.println("===============================");
			
			try {
				String tmp = scan.nextLine();
				menu = Integer.valueOf(tmp);
				if(menu > 4) {
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
				break;
			
			case 2:
					System.out.println("예약");
				break;
			
			case 3:
					System.out.println("예약 조회");
				break;
				
			default:
				System.out.println("종료");
				flag = false;
				break;
			}
			
			
		}//while
		
		
	}//main
	
	
	static void clear() {
		for(int i = 0; i < 15; i++) {
			System.out.println();
		}
	}
}
