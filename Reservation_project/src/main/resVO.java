package main;

public class resVO {
	   private String resNum;
	   private String guest_id;
	   private String name;
	   private int room_num;
	   private int numofpeople;
	   private String arrDate;
	   private String depDate;
	   private String payment;
	   
	   public resVO() {
	      
	   }

	   public resVO(String resNum, String guest_id, String name, int room_num,
	         int numofpeople, String arrDate, String depDate, String payment) {
	      super();
	      this.resNum = resNum;
	      this.guest_id = guest_id;
	      this.name = name;
	      this.room_num = room_num;
	      this.numofpeople = numofpeople;
	      this.arrDate = arrDate;
	      this.depDate = depDate;
	      this.payment = payment;
	   }

	   public String getResNum() {
	      return resNum;
	   }

	   public void setResNum(String resNum) {
	      this.resNum = resNum;
	   }

	   public String getGuest_id() {
	      return guest_id;
	   }

	   public void setGuest_id(String guest_id) {
	      this.guest_id = guest_id;
	   }

	   public String getName() {
	      return name;
	   }

	   public void setName(String name) {
	      this.name = name;
	   }

	   public int getRoom_num() {
	      return room_num;
	   }

	   public void setRoom_num(int room_num) {
	      this.room_num = room_num;
	   }

	   public int getNumofpeople() {
	      return numofpeople;
	   }

	   public void setNumofpeople(int numofpeople) {
	      this.numofpeople = numofpeople;
	   }

	   public String getArrDate() {
	      return arrDate;
	   }

	   public void setArrDate(String arrDate) {
	      this.arrDate = arrDate;
	   }

	   public String getDepDate() {
	      return depDate;
	   }

	   public void setDepDate(String depDate) {
	      this.depDate = depDate;
	   }

	   public String getPayment() {
	      return payment;
	   }

	   public void setPayment(String payment) {
	      this.payment = payment;
	   }

	   @Override
	   public String toString() {
	      
	      return resNum + "\t" + guest_id + "\t" + name + "\t" + room_num + "\t" + numofpeople + "\t" + arrDate + "\t" + depDate + "\t" + payment; 
	   }
}
