/*  1:   */ package com.nari.slsd.hd.alarmcd.policies;
/*  2:   */ 
/*  3:   */ class RTEV
/*  4:   */ {
/*  5:   */   long id;
/*  6:   */   long t;
/*  7:   */   Double factv;
/*  8:   */   
/*  9:   */   public RTEV(long senid, long time, double d)
/* 10:   */   {
/* 11:30 */     this.id = senid;
/* 12:31 */     this.t = time;
/* 13:32 */     this.factv = Double.valueOf(d);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.policies.RTEV
 * JD-Core Version:    0.7.0.1
 */