/*  1:   */ package com.nari.slsd.hd.alarmcd.type;
/*  2:   */ 
/*  3:   */ import com.nari.slsd.hd.alarmcd.UserLog;
/*  4:   */ import java.sql.Connection;
/*  5:   */ import java.sql.DriverManager;
/*  6:   */ import java.util.concurrent.TimeUnit;
/*  7:   */ import java.util.concurrent.locks.ReentrantLock;
/*  8:   */ 
/*  9:   */ public class SingleDataBase
/* 10:   */ {
/* 11:   */   public String driver;
/* 12:   */   public String url;
/* 13:   */   public String user;
/* 14:   */   public String password;
/* 15:   */   public Connection conn;
/* 16:20 */   private UserLog log = new UserLog();
/* 17:21 */   public ReentrantLock lock = null;
/* 18:   */   
/* 19:   */   public SingleDataBase(String driver, String url, String user, String password)
/* 20:   */   {
/* 21:24 */     this.driver = driver;
/* 22:25 */     this.url = url;
/* 23:26 */     this.user = user;
/* 24:27 */     this.password = password;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean openDb()
/* 28:   */   {
/* 29:   */     try
/* 30:   */     {
/* 31:35 */       Class.forName(this.driver);
/* 32:36 */       this.conn = DriverManager.getConnection(this.url, this.user, this.password);
/* 33:   */     }
/* 34:   */     catch (Exception e)
/* 35:   */     {
/* 36:40 */       e.printStackTrace();
/* 37:41 */       this.log.info(this.url + " connect db fail " + e.toString());
/* 38:42 */       System.exit(0);
/* 39:   */     }
/* 40:45 */     return true;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void closeDb()
/* 44:   */   {
/* 45:   */     try
/* 46:   */     {
/* 47:52 */       if (this.conn != null) {
/* 48:53 */         this.conn.close();
/* 49:   */       }
/* 50:   */     }
/* 51:   */     catch (Exception e)
/* 52:   */     {
/* 53:56 */       this.log.error("a error is failure" + e.getMessage());
/* 54:57 */       this.conn = null;
/* 55:   */     }
/* 56:   */   }
/* 57:   */   
/* 58:   */   public boolean waitDb(int i)
/* 59:   */     throws InterruptedException
/* 60:   */   {
/* 61:65 */     if (this.lock == null) {
/* 62:66 */       this.lock = new ReentrantLock();
/* 63:   */     }
/* 64:67 */     return this.lock.tryLock(i, TimeUnit.SECONDS);
/* 65:   */   }
/* 66:   */   
/* 67:   */   public void free()
/* 68:   */   {
/* 69:74 */     this.lock.unlock();
/* 70:   */   }
/* 71:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.type.SingleDataBase
 * JD-Core Version:    0.7.0.1
 */