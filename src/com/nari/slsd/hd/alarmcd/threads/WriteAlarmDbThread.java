/*   1:    */ package com.nari.slsd.hd.alarmcd.threads;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.Config;
/*   4:    */ import com.nari.slsd.hd.alarmcd.UserLog;
/*   5:    */ import com.nari.slsd.hd.alarmcd.send.SendAlarmInfo;
/*   6:    */ import com.nari.slsd.hd.alarmcd.type.PubAlarmRecord;
/*   7:    */ import com.nari.slsd.hd.alarmcd.type.SendAlarmMethod;
/*   8:    */ import com.nari.slsd.hd.model.PubDataAlarmdb;
/*   9:    */ import com.nari.slsd.hd.model.PubDataAlarmdbId;
/*  10:    */ import com.nari.slsd.hd.model.PubDefAlarmchange;
/*  11:    */ import com.nari.slsd.hd.service.IPubDataService;
/*  12:    */ import java.sql.Timestamp;
/*  13:    */ import java.util.Date;
/*  14:    */ import java.util.HashMap;
/*  15:    */ import java.util.LinkedList;
/*  16:    */ import java.util.List;
/*  17:    */ import java.util.concurrent.locks.ReentrantLock;
/*  18:    */ 
/*  19:    */ public class WriteAlarmDbThread
/*  20:    */   extends Thread
/*  21:    */ {
/*  22: 24 */   UserLog log = new UserLog();
/*  23: 25 */   static LinkedList<PubAlarmRecord> dataList = new LinkedList();
/*  24: 26 */   static ReentrantLock lock = new ReentrantLock();
/*  25: 27 */   boolean threadDone = false;
/*  26:    */   IPubDataService pubService;
/*  27:    */   
/*  28:    */   public WriteAlarmDbThread(IPubDataService pds)
/*  29:    */   {
/*  30: 31 */     this.pubService = pds;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static void insertData(long t, String id, String sendid, String info, String detail)
/*  34:    */   {
/*  35: 35 */     lock.lock();
/*  36: 36 */     PubAlarmRecord one = new PubAlarmRecord(t, id, sendid, info, detail);
/*  37: 37 */     dataList.add(one);
/*  38: 38 */     lock.unlock();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void run()
/*  42:    */   {
/*  43: 43 */     long last = System.currentTimeMillis();
/*  44: 44 */     SendAlarmMethod method = null;
/*  45: 45 */     if (Config.sendPort > 0) {
/*  46:    */       try
/*  47:    */       {
/*  48: 48 */         method = new SendAlarmMethod("255.255.255.255", Config.sendPort);
/*  49:    */       }
/*  50:    */       catch (Exception e)
/*  51:    */       {
/*  52: 51 */         e.printStackTrace();
/*  53: 52 */         this.log.info(e.toString());
/*  54:    */       }
/*  55:    */     }
/*  56: 56 */     while (!this.threadDone)
/*  57:    */     {
/*  58: 58 */       PubAlarmRecord one = null;
/*  59: 59 */       lock.lock();
/*  60: 60 */       if (dataList.size() > 0) {
/*  61: 62 */         one = (PubAlarmRecord)dataList.removeFirst();
/*  62:    */       }
/*  63: 64 */       lock.unlock();
/*  64: 65 */       if (one == null)
/*  65:    */       {
/*  66:    */         try
/*  67:    */         {
/*  68: 68 */           sleep(3000L);
/*  69:    */         }
/*  70:    */         catch (InterruptedException e)
/*  71:    */         {
/*  72: 71 */           e.printStackTrace();
/*  73:    */         }
/*  74: 73 */         if ((Config.rebootTime > 0) && 
/*  75: 74 */           (System.currentTimeMillis() - last >= Config.rebootTime * 60000L))
/*  76:    */         {
/*  77: 76 */           List<PubDefAlarmchange> palList = this.pubService
/*  78: 77 */             .getAllList(PubDefAlarmchange.class);
/*  79: 78 */           if ((palList != null) && (palList.size() >= 1)) {
/*  80: 79 */             for (int i = 0; i < palList.size(); i++)
/*  81:    */             {
/*  82: 80 */               PubDefAlarmchange pal = (PubDefAlarmchange)palList.get(i);
/*  83: 81 */               if (pal.getFlag().byteValue() == 1)
/*  84:    */               {
/*  85: 82 */                 this.pubService.delete(palList.get(i));
/*  86: 83 */                 last = System.currentTimeMillis();
/*  87: 84 */                 System.exit(0);
/*  88:    */               }
/*  89:    */             }
/*  90:    */           }
/*  91:    */         }
/*  92:    */       }
/*  93:    */       else
/*  94:    */       {
/*  95: 92 */         PubDataAlarmdb db = new PubDataAlarmdb();
/*  96: 93 */         PubDataAlarmdbId dbid = new PubDataAlarmdbId(one.getId(), new Timestamp(one.getTime()));
/*  97: 94 */         db.setId(dbid);
/*  98:    */         try
/*  99:    */         {
/* 100: 96 */           if (one.getDetail() != null) {
/* 101: 97 */             db.setKeys(one.getDetail().getBytes("UTF-8"));
/* 102:    */           }
/* 103: 98 */           if (one.getInfo().getBytes().length < 2000)
/* 104:    */           {
/* 105: 99 */             db.setContent(one.getInfo());
/* 106:    */           }
/* 107:    */           else
/* 108:    */           {
/* 109:102 */             byte[] bs = one.getInfo().getBytes();
/* 110:103 */             byte[] newbs = new byte[2000];
/* 111:104 */             for (int i = 0; i < 2000; i++) {
/* 112:105 */               newbs[i] = bs[i];
/* 113:    */             }
/* 114:106 */             db.setContent(new String(newbs));
/* 115:107 */             db.setKeys(one.getInfo().getBytes("UTF-8"));
/* 116:    */           }
/* 117:109 */           HashMap inparam = new HashMap();
/* 118:110 */           inparam.put("Op_Type", "OP_INSERTORUPDATE");
/* 119:    */           
/* 120:112 */           this.pubService.dmlPubDataAlarmdb(inparam, db);
/* 121:115 */           if (one.getSendid() != null) {
/* 122:118 */             SendAlarmInfo.sendAlarmInfo(one.getSendid(), one.getId(), one.getInfo(), new Date(one.getTime()));
/* 123:    */           }
/* 124:    */         }
/* 125:    */         catch (Exception e)
/* 126:    */         {
/* 127:122 */           e.printStackTrace();
/* 128:123 */           this.log.error(e.toString());
/* 129:124 */           if ((e.toString().indexOf("所有远程数据服务全部连接失败") >= 0) || (e.toString().toLowerCase().indexOf("pubdataservice") >= 0)) {
/* 130:125 */             System.exit(0);
/* 131:    */           }
/* 132:    */         }
/* 133:    */       }
/* 134:    */     }
/* 135:    */   }
/* 136:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.threads.WriteAlarmDbThread
 * JD-Core Version:    0.7.0.1
 */