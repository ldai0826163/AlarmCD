/*  1:   */ package com.nari.slsd.hd.alarmcd;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import java.text.SimpleDateFormat;
/*  5:   */ import java.util.Date;
/*  6:   */ import java.util.LinkedList;
/*  7:   */ import java.util.concurrent.locks.ReentrantLock;
/*  8:   */ import org.apache.log4j.Logger;
/*  9:   */ 
/* 10:   */ public class UserLog
/* 11:   */ {
/* 12:12 */   static Logger l = Logger.getLogger(UserLog.class);
/* 13:13 */   public static LinkedList<String> strs = new LinkedList();
/* 14:14 */   public static ReentrantLock ilock = new ReentrantLock();
/* 15:15 */   SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss  ");
/* 16:   */   
/* 17:   */   public void addInfo(String s)
/* 18:   */   {
/* 19:24 */     ilock.lock();
/* 20:25 */     Date d = new Date();
/* 21:26 */     while (strs.size() >= 100) {
/* 22:27 */       strs.removeFirst();
/* 23:   */     }
/* 24:28 */     strs.add(new String(this.sm.format(d) + s));
/* 25:29 */     ilock.unlock();
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void info(String s)
/* 29:   */   {
/* 30:33 */     addInfo(s);
/* 31:34 */     l.info(s);
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void error(String s)
/* 35:   */   {
/* 36:39 */     addInfo(s);
/* 37:40 */     l.error(s);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void debug(String s)
/* 41:   */   {
/* 42:45 */     System.out.println(new Date() + s);
/* 43:46 */     l.debug(s);
/* 44:   */   }
/* 45:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.UserLog
 * JD-Core Version:    0.7.0.1
 */