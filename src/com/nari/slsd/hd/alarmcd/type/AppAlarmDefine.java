/*  1:   */ package com.nari.slsd.hd.alarmcd.type;
/*  2:   */ 
/*  3:   */ public class AppAlarmDefine
/*  4:   */ {
/*  5:   */   public static final int mp1 = 101;
/*  6:   */   public static final int mp2 = 102;
/*  7:   */   public static final int mp3 = 103;
/*  8:   */   public static final int calcjob = 111;
/*  9:   */   public static final int securityClient = 113;
/* 10:   */   public static final int securityServer = 114;
/* 11:   */   public static final int comClient = 115;
/* 12:   */   public static final int comServer = 116;
/* 13:   */   public static final int dataChange = 117;
/* 14:   */   public static final int user = 118;
/* 15:   */   public static final int swcalc = 119;
/* 16:   */   public static final int collect = 120;
/* 17:   */   
/* 18:   */   public static String getAppName(int type)
/* 19:   */   {
/* 20:20 */     switch (type)
/* 21:   */     {
/* 22:   */     case 101: 
/* 23:   */     case 102: 
/* 24:   */     case 103: 
/* 25:24 */       return "管理平台";
/* 26:   */     case 111: 
/* 27:25 */       return "计算调度";
/* 28:   */     case 113: 
/* 29:26 */       return "隔离客户端";
/* 30:   */     case 114: 
/* 31:27 */       return "隔离服务端";
/* 32:   */     case 115: 
/* 33:28 */       return "通信客户端";
/* 34:   */     case 116: 
/* 35:29 */       return "通信服务端";
/* 36:   */     case 117: 
/* 37:30 */       return "监控数据交换";
/* 38:   */     case 118: 
/* 39:31 */       return "用户行为分析";
/* 40:   */     case 119: 
/* 41:32 */       return "水务计算";
/* 42:   */     case 120: 
/* 43:33 */       return "采集";
/* 44:   */     }
/* 45:34 */     return "未定义";
/* 46:   */   }
/* 47:   */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.type.AppAlarmDefine
 * JD-Core Version:    0.7.0.1
 */