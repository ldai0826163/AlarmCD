/*  1:   */ package com.nari.slsd.hd.alarmcd.policies;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ 
/*  5:   */ public class PolicyArg
/*  6:   */ {
/*  7:   */   String value;
/*  8:   */   String name;
/*  9:   */   
/* 10:   */   public static String getValue(String argName, ArrayList<PolicyArg> args)
/* 11:   */     throws Exception
/* 12:   */   {
/* 13:11 */     for (int i = 0; i < args.size(); i++) {
/* 14:12 */       if (((PolicyArg)args.get(i)).name.equals(argName)) {
/* 15:13 */         return ((PolicyArg)args.get(i)).value;
/* 16:   */       }
/* 17:   */     }
/* 18:14 */     throw new Exception("no arg " + argName);
/* 19:   */   }
/* 20:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.policies.PolicyArg
 * JD-Core Version:    0.7.0.1
 */