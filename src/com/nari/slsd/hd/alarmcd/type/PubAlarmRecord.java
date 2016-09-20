/*  1:   */ package com.nari.slsd.hd.alarmcd.type;
/*  2:   */ 
/*  3:   */ public class PubAlarmRecord
/*  4:   */ {
/*  5:   */   String id;
/*  6:   */   String sendid;
/*  7:   */   long time;
/*  8:   */   String info;
/*  9:   */   String detail;
/* 10:   */   
/* 11:   */   public PubAlarmRecord(long t, String alarmid, String sendid, String i, String d)
/* 12:   */   {
/* 13: 7 */     this.id = alarmid;
/* 14: 8 */     this.sendid = sendid;
/* 15: 9 */     this.time = t;
/* 16:10 */     this.info = i;
/* 17:11 */     this.detail = d;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String getId()
/* 21:   */   {
/* 22:15 */     return this.id;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void setId(String id)
/* 26:   */   {
/* 27:18 */     this.id = id;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getSendid()
/* 31:   */   {
/* 32:22 */     return this.sendid;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void setSendid(String sendid)
/* 36:   */   {
/* 37:25 */     this.sendid = sendid;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public long getTime()
/* 41:   */   {
/* 42:29 */     return this.time;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public void setTime(long time)
/* 46:   */   {
/* 47:32 */     this.time = time;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public String getInfo()
/* 51:   */   {
/* 52:35 */     return this.info;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public void setInfo(String info)
/* 56:   */   {
/* 57:38 */     this.info = info;
/* 58:   */   }
/* 59:   */   
/* 60:   */   public String getDetail()
/* 61:   */   {
/* 62:41 */     return this.detail;
/* 63:   */   }
/* 64:   */   
/* 65:   */   public void setDetail(String detail)
/* 66:   */   {
/* 67:44 */     this.detail = detail;
/* 68:   */   }
/* 69:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.type.PubAlarmRecord
 * JD-Core Version:    0.7.0.1
 */