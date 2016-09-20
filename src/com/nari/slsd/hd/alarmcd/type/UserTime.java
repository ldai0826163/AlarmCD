/*  1:   */ package com.nari.slsd.hd.alarmcd.type;
/*  2:   */ 
/*  3:   */ import java.util.Calendar;
/*  4:   */ import java.util.Date;
/*  5:   */ import java.util.GregorianCalendar;
/*  6:   */ 
/*  7:   */ public class UserTime
/*  8:   */ {
/*  9:   */   public static Date getHour(Date or)
/* 10:   */   {
/* 11:12 */     Calendar hour = new GregorianCalendar();
/* 12:13 */     hour.setTime(or);
/* 13:14 */     hour.set(12, 0);
/* 14:15 */     hour.set(13, 0);
/* 15:16 */     hour.set(14, 0);
/* 16:17 */     return hour.getTime();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static Date getLastHour(Date or)
/* 20:   */   {
/* 21:21 */     Calendar hour = new GregorianCalendar();
/* 22:22 */     hour.setTimeInMillis(or.getTime() - 3600000L);
/* 23:23 */     hour.set(12, 0);
/* 24:24 */     hour.set(13, 0);
/* 25:25 */     hour.set(14, 0);
/* 26:26 */     return hour.getTime();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public static Calendar getDay(Date or)
/* 30:   */   {
/* 31:30 */     Calendar midnight = new GregorianCalendar();
/* 32:31 */     midnight.setTime(or);
/* 33:32 */     midnight.set(11, 0);
/* 34:33 */     midnight.set(12, 0);
/* 35:34 */     midnight.set(13, 0);
/* 36:35 */     midnight.set(14, 0);
/* 37:36 */     return midnight;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public static Calendar setHour(Calendar midnight, int hour)
/* 41:   */   {
/* 42:40 */     midnight.set(11, hour);
/* 43:41 */     midnight.set(12, 0);
/* 44:42 */     midnight.set(13, 0);
/* 45:43 */     midnight.set(14, 0);
/* 46:44 */     return midnight;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public static Calendar getMonth(Date or)
/* 50:   */   {
/* 51:48 */     Calendar month = new GregorianCalendar();
/* 52:49 */     month.setTime(or);
/* 53:50 */     month.set(11, 0);
/* 54:51 */     month.set(12, 0);
/* 55:52 */     month.set(13, 0);
/* 56:53 */     month.set(14, 0);
/* 57:54 */     month.set(5, 1);
/* 58:55 */     return month;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public static Calendar getDay10(Date or)
/* 62:   */   {
/* 63:59 */     Calendar day10 = new GregorianCalendar();
/* 64:60 */     day10.setTime(or);
/* 65:61 */     int day = day10.get(5);
/* 66:62 */     if (day < 11) {
/* 67:63 */       day = 1;
/* 68:65 */     } else if (day < 21) {
/* 69:66 */       day = 11;
/* 70:   */     } else {
/* 71:68 */       day = 21;
/* 72:   */     }
/* 73:69 */     day10.set(11, 0);
/* 74:70 */     day10.set(12, 0);
/* 75:71 */     day10.set(13, 0);
/* 76:72 */     day10.set(14, 0);
/* 77:73 */     day10.set(5, day);
/* 78:74 */     return day10;
/* 79:   */   }
/* 80:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.type.UserTime
 * JD-Core Version:    0.7.0.1
 */