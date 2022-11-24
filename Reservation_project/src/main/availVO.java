package main;

public class availVO {
	private int room_num;
	private String room_type;
	private String room_size;
	private int price;
	private int person_max;
	
	public availVO() {
		// TODO Auto-generated constructor stub
	}

	public availVO(int room_num, String room_type, String room_size, int price, int person_max) {
		super();
		this.room_num = room_num;
		this.room_type = room_type;
		this.room_size = room_size;
		this.price = price;
		this.person_max = person_max;
	}

	int getRoom_num() {
		return room_num;
	}

	void setRoom_num(int room_num) {
		this.room_num = room_num;
	}

	String getRoom_type() {
		return room_type;
	}

	void setRoom_type(String room_type) {
		this.room_type = room_type;
	}

	String getRoom_size() {
		return room_size;
	}

	void setRoom_size(String room_size) {
		this.room_size = room_size;
	}

	int getPrice() {
		return price;
	}

	void setPrice(int price) {
		this.price = price;
	}

	int getPerson_max() {
		return person_max;
	}

	void setPerson_max(int person_max) {
		this.person_max = person_max;
	}

	@Override
	public String toString() {
		return "[객실번호: " + room_num + ", 객실타입: " + room_type + ", 객실크기=" + room_size + ", 가격="
				+ price + ", 최대인원=" + person_max + "]";
	}
	
	
}
