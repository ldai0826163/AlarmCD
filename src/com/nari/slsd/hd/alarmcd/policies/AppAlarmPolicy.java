/*  1:   */ package com.nari.slsd.hd.alarmcd.policies;
/*  2:   */ 
/*  3:   */ import com.nari.slsd.hd.alarmcd.type.SingleDataBase;
/*  4:   */ import com.nari.slsd.hd.service.IWdsHisDataService;
/*  5:   */ import java.util.Calendar;
/*  6:   */ import java.util.List;
/*  7:   */ 
/*  8:   */ public class AppAlarmPolicy
/*  9:   */   extends Policy
/* 10:   */ {
/* 11:   */   public AppAlarmPolicy() {}
/* 12:   */   
/* 13:   */   public AppAlarmPolicy(Integer t)
/* 14:   */   {
/* 15:18 */     this.type = t.intValue();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void check(Calendar c, IWdsHisDataService wdsService) {}
/* 19:   */   
/* 20:   */   public void check(Calendar cal, SingleDataBase db) {}
/* 21:   */   
/* 22:   */   public boolean getConfig(byte[] bs)
/* 23:   */   {
/* 24:36 */     return false;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setName(List lists) {}
/* 28:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.policies.AppAlarmPolicy
 * JD-Core Version:    0.7.0.1
 */