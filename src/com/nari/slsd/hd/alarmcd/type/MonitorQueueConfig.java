/*  1:   */ package com.nari.slsd.hd.alarmcd.type;
/*  2:   */ 
/*  3:   */ public class MonitorQueueConfig
/*  4:   */ {
/*  5:   */   public String url;
/*  6:   */   public String driver;
/*  7:   */   public String user;
/*  8:   */   public String pass;
/*  9:   */   public int queueLen;
/* 10:   */   public int waitTime;
/* 11:   */   public String[] policies;
/* 12:   */   public int type;
/* 13:   */   
/* 14:   */   public MonitorQueueConfig(int t, String driver, String url, String user, String pass, int l, int w, String[] ps)
/* 15:   */   {
/* 16: 7 */     this.url = url;
/* 17: 8 */     this.driver = driver;
/* 18: 9 */     this.user = user;
/* 19:10 */     this.pass = pass;
/* 20:11 */     this.queueLen = l;
/* 21:12 */     this.waitTime = w;
/* 22:13 */     this.policies = ps;
/* 23:14 */     this.type = 0;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public MonitorQueueConfig(int t, int l, int w, String[] ps)
/* 27:   */   {
/* 28:19 */     this.queueLen = l;
/* 29:20 */     this.waitTime = w;
/* 30:21 */     this.policies = ps;
/* 31:22 */     this.type = t;
/* 32:   */   }
/* 33:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.type.MonitorQueueConfig
 * JD-Core Version:    0.7.0.1
 */