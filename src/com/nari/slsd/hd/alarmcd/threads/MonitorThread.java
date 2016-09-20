/*   1:    */ package com.nari.slsd.hd.alarmcd.threads;
/*   2:    */ 
/*   3:    */ import com.nari.slsd.hd.alarmcd.UserLog;
/*   4:    */ import com.nari.slsd.hd.alarmcd.policies.Policy;
/*   5:    */ import com.nari.slsd.hd.alarmcd.type.MonitorEvent;
/*   6:    */ import com.nari.slsd.hd.alarmcd.type.SingleDataBase;
/*   7:    */ import com.nari.slsd.hd.clientproxy.ClientProxy;
/*   8:    */ import com.nari.slsd.hd.config.Configuration;
/*   9:    */ import com.nari.slsd.hd.service.IWdsHisDataService;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Calendar;
/*  12:    */ import java.util.Iterator;
/*  13:    */ import java.util.LinkedList;
/*  14:    */ import java.util.concurrent.locks.ReentrantLock;
/*  15:    */ 
/*  16:    */ public class MonitorThread
/*  17:    */   extends Thread
/*  18:    */ {
/*  19: 18 */   static int waitCount = 0;
/*  20: 19 */   UserLog log = new UserLog();
/*  21: 20 */   SingleDataBase db = null;
/*  22: 21 */   LinkedList<MonitorEvent> cqList = new LinkedList();
/*  23: 22 */   ReentrantLock lock = new ReentrantLock();
/*  24: 23 */   int maxLength = 5;
/*  25: 24 */   long lastfree = 0L;
/*  26: 25 */   long waitTime = 600000L;
/*  27: 26 */   boolean threadDone = false;
/*  28:    */   int type;
/*  29:    */   IWdsHisDataService wdsService;
/*  30:    */   
/*  31:    */   public MonitorThread(String driver, String url, String user, String password, int ml, int waitMin, int[] done, int i)
/*  32:    */   {
/*  33: 33 */     this.db = new SingleDataBase(driver, url, user, password);
/*  34: 34 */     if (!this.db.openDb())
/*  35:    */     {
/*  36: 36 */       this.log.info("open db " + url + " fail ,exit");
/*  37: 37 */       System.exit(0);
/*  38:    */     }
/*  39: 39 */     this.maxLength = ml;
/*  40: 40 */     this.waitTime = (waitMin * 60000);
/*  41: 41 */     this.type = 0;
/*  42: 42 */     done[i] = 1;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public MonitorThread(int ml, int waitMin, int[] done, int i)
/*  46:    */   {
/*  47: 46 */     this.type = 1;
/*  48: 47 */     this.wdsService = ClientProxy.getWdsHisDataService(Configuration.getLocalPlantId());
/*  49: 48 */     done[i] = 1;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void insertMonitor(Calendar cal, ArrayList<Policy> rp, int type)
/*  53:    */   {
/*  54: 53 */     MonitorEvent one = new MonitorEvent(cal, rp, type);
/*  55: 54 */     this.lock.lock();
/*  56: 55 */     if (this.cqList.size() >= this.maxLength)
/*  57:    */     {
/*  58: 56 */       if (System.currentTimeMillis() - this.lastfree > this.waitTime) {
/*  59: 58 */         System.exit(0);
/*  60:    */       }
/*  61: 61 */       boolean find = true;
/*  62: 62 */       while (find)
/*  63:    */       {
/*  64: 64 */         find = false;
/*  65: 65 */         Iterator<MonitorEvent> it = this.cqList.iterator();
/*  66: 66 */         while (it.hasNext())
/*  67:    */         {
/*  68: 68 */           MonitorEvent cq = (MonitorEvent)it.next();
/*  69: 69 */           if (cq.getType() == type)
/*  70:    */           {
/*  71: 71 */             it.remove();
/*  72: 72 */             find = true;
/*  73: 73 */             break;
/*  74:    */           }
/*  75:    */         }
/*  76:    */       }
/*  77:    */     }
/*  78: 80 */     this.cqList.addLast(one);
/*  79: 81 */     this.lastfree = System.currentTimeMillis();
/*  80: 82 */     this.lock.unlock();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void run()
/*  84:    */   {
/*  85: 87 */     while (!this.threadDone) {
/*  86:    */       try
/*  87:    */       {
/*  88: 89 */         MonitorEvent one = null;
/*  89: 90 */         this.lock.lock();
/*  90: 91 */         if (this.cqList.size() > 0) {
/*  91: 92 */           one = (MonitorEvent)this.cqList.removeFirst();
/*  92:    */         }
/*  93: 94 */         this.lock.unlock();
/*  94: 95 */         if (one == null) {
/*  95:    */           try
/*  96:    */           {
/*  97: 97 */             sleep(3000L);
/*  98:    */           }
/*  99:    */           catch (InterruptedException e)
/* 100:    */           {
/* 101:100 */             e.printStackTrace();
/* 102:    */           }
/* 103:    */         } else {
/* 104:104 */           for (int i = 0; i < one.getRp().size(); i++)
/* 105:    */           {
/* 106:105 */             Policy qone = (Policy)one.getRp().get(i);
/* 107:106 */             if (this.type == 0) {
/* 108:107 */               qone.check(one.getCal(), this.db);
/* 109:    */             } else {
/* 110:109 */               qone.check(one.getCal(), this.wdsService);
/* 111:    */             }
/* 112:    */           }
/* 113:    */         }
/* 114:    */       }
/* 115:    */       catch (Exception e)
/* 116:    */       {
/* 117:112 */         e.printStackTrace();
/* 118:113 */         this.log.error(e.toString());
/* 119:    */       }
/* 120:    */     }
/* 121:    */   }
/* 122:    */ }


/* Location:           C:\Users\lauren\Desktop\HD.AlarmCD.jar
 * Qualified Name:     com.nari.slsd.hd.alarmcd.threads.MonitorThread
 * JD-Core Version:    0.7.0.1
 */