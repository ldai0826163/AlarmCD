/*   1:    */ package com.nari.slsd.hd.alarmcd;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.RunControl;
/*   4:    */ import com.nari.slsd.hd.alarmcd.send.SendAlarmInfo;
/*   5:    */ import com.nari.slsd.hd.alarmcd.threads.CheckThread;
/*   6:    */ import com.nari.slsd.hd.alarmcd.threads.MonitorThread;
/*   7:    */ import com.nari.slsd.hd.alarmcd.threads.RecvAppAlarmThr;
/*   8:    */ import com.nari.slsd.hd.alarmcd.threads.WriteAlarmDbThread;
/*   9:    */ import com.nari.slsd.hd.alarmcd.type.MonitorQueueConfig;
/*  10:    */ import com.nari.slsd.hd.clientproxy.ClientProxy;
/*  11:    */ import com.nari.slsd.hd.config.Configuration;
/*  12:    */ import com.nari.slsd.hd.service.IPubDataService;
/*  13:    */ import java.awt.BorderLayout;
/*  14:    */ import java.awt.Dimension;
/*  15:    */ import javax.swing.JFrame;
/*  16:    */ 
/*  17:    */ public class Main
/*  18:    */ {
/*  19: 21 */   static UserLog log = new UserLog();
/*  20:    */   public static IPubDataService PubDataService;
/*  21:    */   
/*  22:    */   public static void dealException(Exception e)
/*  23:    */   {
/*  24: 25 */     e.printStackTrace();
/*  25: 26 */     log.error(e.toString());
/*  26: 27 */     if ((e.toString().indexOf("所有远程数据服务全部连接失败") >= 0) || (e.toString().toLowerCase().indexOf("pubdataservice") >= 0)) {
/*  27: 28 */       System.exit(0);
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static void main(String[] args)
/*  32:    */   {
/*  33: 33 */     RunControl.startOnce();
/*  34: 34 */     log.info("报警服务开始运行");
/*  35:    */     try
/*  36:    */     {
/*  37: 36 */       Config.readXmlFile(null);
/*  38:    */     }
/*  39:    */     catch (Exception e)
/*  40:    */     {
/*  41: 39 */       e.printStackTrace();
/*  42: 40 */       log.error("初始化系统失败" + e.toString());
/*  43: 41 */       System.exit(0);
/*  44:    */     }
/*  45:    */     try
/*  46:    */     {
/*  47: 44 */       PubDataService = ClientProxy.getPubDataServiceProxy(
/*  48: 45 */         Configuration.getLocalPlantId());
/*  49:    */     }
/*  50:    */     catch (Exception e)
/*  51:    */     {
/*  52: 49 */       e.printStackTrace();
/*  53: 50 */       log.error(e.toString());
/*  54: 51 */       System.exit(0);
/*  55:    */     }
/*  56: 55 */     int MonitorQueneNum = Config.mqconfigs.length;
/*  57: 56 */     if (MonitorQueneNum > 0)
/*  58:    */     {
/*  59: 58 */       com.nari.slsd.hd.alarmcd.type.SharedData.monitors = new MonitorThread[MonitorQueneNum];
/*  60: 59 */       int[] done = new int[MonitorQueneNum];
/*  61: 60 */       for (int i = 0; i < MonitorQueneNum; i++)
/*  62:    */       {
/*  63: 62 */         MonitorQueueConfig one = Config.mqconfigs[i];
/*  64: 63 */         done[i] = 0;
/*  65: 64 */         if (one.type == 0) {
/*  66: 65 */           com.nari.slsd.hd.alarmcd.type.SharedData.monitors[i] = new MonitorThread(one.driver, one.url, one.user, 
/*  67: 66 */             one.pass, one.queueLen, one.waitTime, done, i);
/*  68:    */         } else {
/*  69: 69 */           com.nari.slsd.hd.alarmcd.type.SharedData.monitors[i] = new MonitorThread(one.queueLen, one.waitTime, done, i);
/*  70:    */         }
/*  71: 71 */         com.nari.slsd.hd.alarmcd.type.SharedData.monitors[i].start();
/*  72: 72 */         log.info("montitor thread " + i + " start!");
/*  73:    */       }
/*  74:    */       for (;;)
/*  75:    */       {
/*  76: 76 */         boolean find = true;
/*  77: 77 */         for (int i = 0; i < MonitorQueneNum; i++) {
/*  78: 79 */           if (done[i] == 0)
/*  79:    */           {
/*  80: 81 */             find = false;
/*  81: 82 */             break;
/*  82:    */           }
/*  83:    */         }
/*  84: 85 */         if (find) {
/*  85:    */           break;
/*  86:    */         }
/*  87:    */         try
/*  88:    */         {
/*  89: 88 */           Thread.sleep(1000L);
/*  90:    */         }
/*  91:    */         catch (InterruptedException e)
/*  92:    */         {
/*  93: 91 */           e.printStackTrace();
/*  94:    */         }
/*  95:    */       }
/*  96:    */     }
/*  97: 97 */     SendAlarmInfo.init(PubDataService);
/*  98: 98 */     WriteAlarmDbThread st = new WriteAlarmDbThread(PubDataService);
/*  99: 99 */     st.start();
/* 100:100 */     CheckThread ct = new CheckThread(PubDataService);
/* 101:101 */     ct.start();
/* 102:102 */     RecvAppAlarmThr rt = new RecvAppAlarmThr(Config.appAlarmInfoPort, PubDataService);
/* 103:103 */     rt.start();
///* 104:104 */     if ((args.length < 1) || (!args[0].equals("nowin")))
///* 105:    */     {
///* 106:106 */       JFrame frame = new JFrame("报警平台");
///* 107:107 */       frame.setLayout(new BorderLayout());
///* 108:108 */       MainPane panel = new MainPane();
///* 109:109 */       frame.add(panel, "Center");
///* 110:110 */       frame.setDefaultCloseOperation(3);
///* 111:    */       
///* 112:112 */       frame.setSize(new Dimension(800, 600));
///* 113:113 */       frame.setLocationRelativeTo(null);
///* 114:114 */       frame.setVisible(true);
///* 115:    */     }
/* 116:    */   }
/* 117:    */ }


