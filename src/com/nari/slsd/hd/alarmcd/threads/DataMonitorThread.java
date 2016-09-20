/*  1:   */ package com.nari.slsd.hd.alarmcd.threads;
/*  2:   */ 
/*  3:   */ import com.nari.slsd.hd.alarmcd.UserLog;
/*  4:   */ import com.nari.slsd.hd.alarmcd.policies.Policy;
/*  5:   */ import com.nari.slsd.hd.alarmcd.type.SingleDataBase;
/*  6:   */ import java.util.ArrayList;
/*  7:   */ import java.util.Calendar;
/*  8:   */ 
/*  9:   */ public class DataMonitorThread
/* 10:   */   extends Thread
/* 11:   */ {
/* 12:12 */   static int waitCount = 0;
/* 13:13 */   UserLog log = new UserLog();
/* 14:14 */   SingleDataBase db = null;
/* 15:   */   ArrayList<Policy> rp;
/* 16:   */   Calendar cal;
/* 17:   */   
/* 18:   */   public DataMonitorThread(Calendar cal, ArrayList<Policy> rp, SingleDataBase db)
/* 19:   */   {
/* 20:20 */     boolean r = false;
/* 21:   */     try
/* 22:   */     {
/* 23:22 */       r = db.waitDb(60);
/* 24:   */     }
/* 25:   */     catch (InterruptedException e)
/* 26:   */     {
/* 27:25 */       e.printStackTrace();
/* 28:26 */       this.log.info(e.toString());
/* 29:   */     }
/* 30:28 */     if (r)
/* 31:   */     {
/* 32:30 */       waitCount = 0;
/* 33:31 */       this.db = db;
/* 34:32 */       this.rp = rp;
/* 35:33 */       this.cal = cal;
/* 36:   */     }
/* 37:   */     else
/* 38:   */     {
/* 39:37 */       waitCount += 1;
/* 40:38 */       if (waitCount >= 10)
/* 41:   */       {
/* 42:40 */         this.log.info("Datacheck thread can't get database,system exit!");
/* 43:41 */         System.exit(0);
/* 44:   */       }
/* 45:   */     }
/* 46:   */   }
/* 47:   */   
/* 48:   */   public void run()
/* 49:   */   {
/* 50:49 */     if (this.db == null) {
/* 51:50 */       return;
/* 52:   */     }
/* 53:51 */     for (int i = 0; i < this.rp.size(); i++) {
/* 54:53 */       ((Policy)this.rp.get(i)).check(this.cal, this.db);
/* 55:   */     }
/* 56:55 */     this.db.free();
/* 57:   */   }
/* 58:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.threads.DataMonitorThread
 * JD-Core Version:    0.7.0.1
 */