/*  1:   */ package com.nari.slsd.hd.alarmcd.type;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.util.ArrayList;
/*  5:   */ 
/*  6:   */ public class AlarmRecord
/*  7:   */   implements Serializable
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 1L;
/* 10:   */   String id;
/* 11:12 */   public long checkTime = 0L;
/* 12:13 */   public long recordTime = 0L;
/* 13:14 */   public boolean abnormal = false;
/* 14:   */   double v;
/* 15:16 */   int level = 0;
/* 16:   */   
/* 17:   */   public double getV()
/* 18:   */   {
/* 19:18 */     return this.v;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setV(double v)
/* 23:   */   {
/* 24:21 */     this.v = v;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public int getLevel()
/* 28:   */   {
/* 29:24 */     return this.level;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setLevel(int level)
/* 33:   */   {
/* 34:27 */     this.level = level;
/* 35:   */   }
/* 36:   */   
/* 37:29 */   public ArrayList<Long> abnormalIds = new ArrayList();
/* 38:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.type.AlarmRecord
 * JD-Core Version:    0.7.0.1
 */