package main;

public class TodayVO {

   private String resNum;
   private String guest_id;
   private String name;
   private String phone_num;
   private String email;
   private int room_num;
   private int numofpeople;
   private String arrDate;
   private String depDate;
   private String payment;

   public TodayVO() {   }

   public TodayVO(String resNum, String guest_id, String name, String phone_num, String email, int room_num,
         int numofpeople, String arrDate, String depDate, String payment) {
      super();
      this.resNum = resNum;
      this.guest_id = guest_id;
      this.name = name;
      this.phone_num = phone_num;
      this.email = email;
      this.room_num = room_num;
      this.numofpeople = numofpeople;
      this.arrDate = arrDate;
      this.depDate = depDate;
      this.payment = payment;
   }

   @Override
   public String toString() {
      return  resNum + "\t" + guest_id + "\t" + name + "\t" + room_num + "\t" + numofpeople + "\t" + arrDate + "\t" + depDate + "\t" + payment;
   }

   




}