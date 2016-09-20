/*   1:    */ package com.nari.slsd.hd.alarmcd.policies;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.UserLog;
/*   4:    */ import com.nari.slsd.hd.alarmcd.threads.WriteAlarmDbThread;
/*   5:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecord;
/*   6:    */ import com.nari.slsd.hd.alarmcd.type.AlarmRecordManager;
/*   7:    */ import com.nari.slsd.hd.alarmcd.type.SingleDataBase;
/*   8:    */ import com.nari.slsd.hd.service.IWdsHisDataService;
/*   9:    */ import java.sql.SQLException;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Calendar;
/*  12:    */ import java.util.List;
/*  13:    */ 
/*  14:    */ public abstract class Policy
/*  15:    */ {
/*  16:    */   String id;
/*  17:    */   String sendid;
/*  18:    */   int type;
/*  19:    */   int interval;
/*  20:    */   
/*  21:    */   public String getId()
/*  22:    */   {
/*  23: 19 */     return this.id;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setId(String id)
/*  27:    */   {
/*  28: 22 */     this.id = id;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public int getType()
/*  32:    */   {
/*  33: 25 */     return this.type;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setType(int type)
/*  37:    */   {
/*  38: 28 */     this.type = type;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public int getInterval()
/*  42:    */   {
/*  43: 31 */     return this.interval;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setInterval(int interval)
/*  47:    */   {
/*  48: 34 */     this.interval = interval;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String getSendid()
/*  52:    */   {
/*  53: 38 */     return this.sendid;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setSendid(String sendid)
/*  57:    */   {
/*  58: 41 */     this.sendid = sendid;
/*  59:    */   }
/*  60:    */   
/*  61: 45 */   String name = "";
/*  62:    */   
/*  63:    */   public String getName()
/*  64:    */   {
/*  65: 47 */     return this.name;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setName(String name)
/*  69:    */   {
/*  70: 50 */     this.name = name;
/*  71:    */   }
/*  72:    */   
/*  73: 52 */   UserLog log = new UserLog();
/*  74:    */   
/*  75:    */   public ArrayList<Policy> needCheck(Calendar c, Policy[] ps)
/*  76:    */   {
/*  77: 54 */     ArrayList<Policy> rps = new ArrayList();
/*  78: 55 */     for (int i = 0; i < ps.length; i++)
/*  79:    */     {
/*  80: 56 */       Policy one = ps[i];
/*  81: 57 */       if (one.type == this.type)
/*  82:    */       {
/*  83: 59 */         AlarmRecord ar = AlarmRecordManager.getRecord(one.id);
/*  84: 61 */         if ((ar != null) && ((int)((c.getTimeInMillis() - ar.checkTime) / 1000L) >= one.interval * 60)) {
/*  85: 62 */           rps.add(one);
/*  86:    */         }
/*  87:    */       }
/*  88:    */     }
/*  89: 66 */     if (rps.size() == 0) {
/*  90: 67 */       return null;
/*  91:    */     }
/*  92: 68 */     return rps;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public abstract void check(Calendar paramCalendar, IWdsHisDataService paramIWdsHisDataService);
/*  96:    */   
/*  97:    */   public abstract void check(Calendar paramCalendar, SingleDataBase paramSingleDataBase);
/*  98:    */   
/*  99:    */   public void sendToDB(long t, String info, String detail)
/* 100:    */   {
/* 101: 74 */     WriteAlarmDbThread.insertData(t, this.id, this.sendid, info, detail);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public abstract boolean getConfig(byte[] paramArrayOfByte);
/* 105:    */   
/* 106:    */   public abstract void setName(List paramList);
/* 107:    */   
/* 108:    */   public void dealDBException(Exception e, SingleDataBase db)
/* 109:    */   {
/* 110: 80 */     e.printStackTrace();
/* 111: 81 */     this.log.error(e.toString());
/* 112: 98 */     if ((e instanceof SQLException))
/* 113:    */     {
/* 114:100 */       db.closeDb();
/* 115:101 */       db.openDb();
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void dealWdsServiceException(Exception e)
/* 120:    */   {
/* 121:107 */     e.printStackTrace();
/* 122:108 */     this.log.error(e.toString());
/* 123:109 */     if (e.toString().indexOf("所有远程数据服务全部连接失败") > 0) {
/* 124:110 */       System.exit(0);
/* 125:    */     }
/* 126:    */   }
/* 127:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.policies.Policy
 * JD-Core Version:    0.7.0.1
 */