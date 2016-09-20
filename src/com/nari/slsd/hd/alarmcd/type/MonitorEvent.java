/*  1:   */ package com.nari.slsd.hd.alarmcd.type;
/*  2:   */ 
/*  3:   */ import com.nari.slsd.hd.alarmcd.policies.Policy;
/*  4:   */ import java.util.ArrayList;
/*  5:   */ import java.util.Calendar;
/*  6:   */ 
/*  7:   */ public class MonitorEvent
/*  8:   */ {
/*  9:   */   ArrayList<Policy> rp;
/* 10:   */   Calendar cal;
/* 11:   */   int type;
/* 12:   */   
/* 13:   */   public MonitorEvent(Calendar c, ArrayList<Policy> p, int t)
/* 14:   */   {
/* 15:12 */     this.cal = c;
/* 16:13 */     this.rp = p;
/* 17:14 */     this.type = t;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public ArrayList<Policy> getRp()
/* 21:   */   {
/* 22:17 */     return this.rp;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void setRp(ArrayList<Policy> rp)
/* 26:   */   {
/* 27:20 */     this.rp = rp;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Calendar getCal()
/* 31:   */   {
/* 32:24 */     return this.cal;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void setCal(Calendar cal)
/* 36:   */   {
/* 37:27 */     this.cal = cal;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public int getType()
/* 41:   */   {
/* 42:30 */     return this.type;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public void setType(int type)
/* 46:   */   {
/* 47:33 */     this.type = type;
/* 48:   */   }
/* 49:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.type.MonitorEvent
 * JD-Core Version:    0.7.0.1
 */