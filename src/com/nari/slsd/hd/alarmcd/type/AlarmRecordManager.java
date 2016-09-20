/*  1:   */ package com.nari.slsd.hd.alarmcd.type;
/*  2:   */ 
/*  3:   */ import com.nari.slsd.hd.alarmcd.policies.Policy;
/*  4:   */ import java.io.Serializable;
/*  5:   */ 
/*  6:   */ public class AlarmRecordManager
/*  7:   */   implements Serializable
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 1L;
/* 10:   */   static AlarmRecord[] records;
/* 11:   */   
/* 12:   */   public static void init(Policy[] ps)
/* 13:   */   {
/* 14:19 */     records = new AlarmRecord[ps.length];
/* 15:20 */     for (int i = 0; i < ps.length; i++)
/* 16:   */     {
/* 17:22 */       AlarmRecord ar = new AlarmRecord();
/* 18:23 */       ar.id = ps[i].getId();
/* 19:24 */       records[i] = ar;
/* 20:   */     }
/* 21:   */   }
/* 22:   */   
/* 23:   */   public static AlarmRecord getRecord(String id)
/* 24:   */   {
/* 25:29 */     for (int i = 0; i < records.length; i++) {
/* 26:30 */       if (records[i].id.equals(id)) {
/* 27:31 */         return records[i];
/* 28:   */       }
/* 29:   */     }
/* 30:32 */     return null;
/* 31:   */   }
/* 32:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.type.AlarmRecordManager
 * JD-Core Version:    0.7.0.1
 */